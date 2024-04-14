package net.chesstango.tools.search;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.EpdEntry;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.board.representations.move.SANEncoder;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.SearchParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * @author Mauricio Coria
 */
public class EpdSearch {
    private final int INFINITE_DEPTH = 25;

    private static final Logger logger = LoggerFactory.getLogger(EpdSearch.class);

    private static final int SEARCH_THREADS = 4;

    private static final SANEncoder sanEncoder = new SANEncoder();

    @Setter
    @Accessors(chain = true)
    private Supplier<SearchMove> searchMoveSupplier;

    @Setter
    @Accessors(chain = true)
    private int depth;

    @Setter
    @Accessors(chain = true)
    private Integer timeOut;


    public EpdSearchResult run(EpdEntry epdEntry) {
        return timeOut == null ? run(searchMoveSupplier.get(), epdEntry) : run(List.of(epdEntry)).get(0);
    }

    public List<EpdSearchResult> run(List<EpdEntry> edpEntries) {
        AtomicInteger pendingJobsCounter = new AtomicInteger(edpEntries.size());
        List<SearchJob> activeJobs = new ArrayList<>(SEARCH_THREADS);
        ExecutorService executorService = Executors.newFixedThreadPool(SEARCH_THREADS);

        BlockingQueue<SearchMove> searchMovePool = new LinkedBlockingDeque<>(SEARCH_THREADS);
        for (int i = 0; i < SEARCH_THREADS; i++) {
            searchMovePool.add(searchMoveSupplier.get());
        }

        List<Future<EpdSearchResult>> futures = new LinkedList<>();
        for (EpdEntry epdEntry : edpEntries) {
            Future<EpdSearchResult> future = executorService.submit(() -> {
                SearchJob searchJob = null;
                try {
                    Instant startInstant = Instant.now();

                    SearchMove searchMove = searchMovePool.take();

                    searchJob = new SearchJob(startInstant, searchMove);

                    synchronized (EpdSearch.this) {
                        activeJobs.add(searchJob);
                    }

                    EpdSearchResult epdSearchResult = run(searchMove, epdEntry);
                    if (epdSearchResult.isSearchSuccess()) {
                        //logger.info(String.format("Success %s", epdEntry.fen));
                    } else {
                        String failedTest = String.format("Fail [%s] - best move found %s",
                                epdEntry.text,
                                epdSearchResult.bestMoveFoundStr()
                        );
                        logger.info(failedTest);
                    }

                    return epdSearchResult;
                } catch (RuntimeException e) {
                    e.printStackTrace(System.err);
                    logger.error(String.format("Error processing: %s", epdEntry.text));
                    throw e;
                } finally {
                    assert searchJob != null;

                    synchronized (EpdSearch.this) {
                        activeJobs.remove(searchJob);
                    }

                    searchMovePool.put(searchJob.searchMove);
                    pendingJobsCounter.decrementAndGet();
                }
            });

            futures.add(future);
        }

        try {

            if (timeOut != null) {
                while (pendingJobsCounter.get() > 0) {
                    Thread.sleep(500);
                    synchronized (this) {
                        activeJobs.forEach(searchJob -> {
                            if (searchJob.elapsedMillis() >= timeOut) {
                                searchJob.searchMove.stopSearching();
                            }
                        });
                    }
                }
            }

            executorService.shutdown();
            while (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
            }
        } catch (InterruptedException e) {
            logger.error("Stopping executorService....");
            executorService.shutdownNow();
        }

        List<EpdSearchResult> epdSearchResults = new LinkedList<>();
        futures.forEach(future -> {
            try {
                EpdSearchResult epdSearchResult = future.get();
                epdSearchResults.add(epdSearchResult);
            } catch (ExecutionException e) {
                logger.error("Future exception");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });


        if (epdSearchResults.isEmpty()) {
            throw new RuntimeException("No edp entry was processed");
        }

        epdSearchResults.sort(Comparator.comparing(o -> o.epdEntry().id));

        return epdSearchResults;
    }


    private EpdSearchResult run(SearchMove searchMove, EpdEntry epdEntry) {

        searchMove.setSearchParameter(SearchParameter.MAX_DEPTH, depth);

        Game game = FENDecoder.loadGame(epdEntry.fen);

        SearchMoveResult searchResult = searchMove.search(game);

        searchResult.setId(epdEntry.id);

        searchMove.reset();

        Move bestMove = searchResult.getBestMove();

        String bestMoveFoundStr = sanEncoder.encode(bestMove, game.getPossibleMoves());

        return new EpdSearchResult(epdEntry, searchResult, bestMoveFoundStr);
    }

    private record SearchJob(Instant startInstant, SearchMove searchMove) {
        public long elapsedMillis() {
            return Duration.between(startInstant, Instant.now()).toMillis();
        }
    }
}
