package net.chesstango.search;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.EPDEntry;
import net.chesstango.board.representations.move.SANEncoder;
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
    private final int INFINITE_DEPTH = 100;

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


    public EpdSearchResult run(EPDEntry epdEntry) {
        return timeOut == null ? run(searchMoveSupplier.get(), epdEntry) : run(List.of(epdEntry)).get(0);
    }

    public List<EpdSearchResult> run(List<EPDEntry> edpEntries) {
        AtomicInteger pendingJobsCounter = new AtomicInteger(edpEntries.size());
        List<SearchJob> activeJobs = new ArrayList<>(SEARCH_THREADS);
        ExecutorService executorService = Executors.newFixedThreadPool(SEARCH_THREADS);

        BlockingQueue<SearchMove> searchMovePool = new LinkedBlockingDeque<>(SEARCH_THREADS);
        for (int i = 0; i < SEARCH_THREADS; i++) {
            searchMovePool.add(searchMoveSupplier.get());
        }

        List<Future<EpdSearchResult>> futures = new LinkedList<>();
        for (EPDEntry epdEntry : edpEntries) {
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
                    if (epdSearchResult.epdResult()) {
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


    private EpdSearchResult run(SearchMove searchMove, EPDEntry epdEntry) {

        searchMove.setSearchParameter(SearchParameter.MAX_DEPTH, depth);

        SearchMoveResult searchResult = searchMove.search(epdEntry.game);

        searchResult.setEpdID(epdEntry.id);

        searchMove.reset();

        Move bestMove = searchResult.getBestMove();

        boolean epdSearchResult;
        if (epdEntry.bestMoves != null && !epdEntry.bestMoves.isEmpty()) {
            epdSearchResult = epdEntry.bestMoves.contains(bestMove);
        } else if (epdEntry.avoidMoves != null && !epdEntry.avoidMoves.isEmpty()) {
            epdSearchResult = !epdEntry.avoidMoves.contains(bestMove);
        } else {
            throw new RuntimeException("Undefined expected EPD result");
        }

        String bestMoveFoundStr = sanEncoder.encode(bestMove, epdEntry.game.getPossibleMoves());
        return new EpdSearchResult(epdEntry, searchResult, bestMoveFoundStr, epdSearchResult);
    }

    private record SearchJob(Instant startInstant, SearchMove searchMove) {
        public long elapsedMillis() {
            return Duration.between(startInstant, Instant.now()).toMillis();
        }
    }
}
