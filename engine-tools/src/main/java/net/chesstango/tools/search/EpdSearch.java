package net.chesstango.tools.search;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.epd.EpdEntry;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.board.representations.move.SANEncoder;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.SearchParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * @author Mauricio Coria
 */
@Setter
public class EpdSearch {
    private static final Logger logger = LoggerFactory.getLogger(EpdSearch.class);

    private static final SANEncoder sanEncoder = new SANEncoder();

    @Accessors(chain = true)
    private Supplier<SearchMove> searchMoveSupplier;

    @Accessors(chain = true)
    private int depth;

    @Accessors(chain = true)
    private Integer timeOut;


    public EpdSearchResult run(EpdEntry epdEntry) {
        return timeOut == null ? run(searchMoveSupplier.get(), epdEntry) : run(List.of(epdEntry)).getFirst();
    }

    public List<EpdSearchResult> run(List<EpdEntry> edpEntries) {
        final int availableCores = Runtime.getRuntime().availableProcessors();

        AtomicInteger pendingJobsCounter = new AtomicInteger(edpEntries.size());
        List<SearchJob> activeJobs = Collections.synchronizedList(new LinkedList<>());

        List<EpdSearchResult> epdSearchResults = Collections.synchronizedList(new LinkedList<>());

        BlockingQueue<SearchMove> searchMovePool = new LinkedBlockingDeque<>(availableCores);
        for (int i = 0; i < availableCores; i++) {
            searchMovePool.add(searchMoveSupplier.get());
        }

        try (ExecutorService executorService = Executors.newFixedThreadPool(availableCores)) {

            for (EpdEntry epdEntry : edpEntries) {
                executorService.submit(() -> {
                    SearchJob searchJob = null;
                    try {
                        Instant startInstant = Instant.now();

                        SearchMove searchMove = searchMovePool.take();

                        searchJob = new SearchJob(startInstant, searchMove);

                        activeJobs.add(searchJob);

                        EpdSearchResult epdSearchResult = run(searchMove, epdEntry);

                        epdSearchResults.add(epdSearchResult);

                        if (!epdSearchResult.isSearchSuccess()) {
                            String failedTest = String.format("Fail [%s] - best move found %s",
                                    epdEntry.text,
                                    epdSearchResult.bestMoveFoundAlgNot()
                            );
                            logger.info(failedTest);
                        }

                        searchMovePool.put(searchJob.searchMove);

                    } catch (RuntimeException e) {
                        e.printStackTrace(System.err);
                        logger.error(String.format("Error processing: %s", epdEntry.text));
                        throw e;
                    } catch (InterruptedException e) {
                        logger.error(String.format("Thread interrupted while processing: %s", epdEntry.text));
                        e.printStackTrace(System.err);
                        throw new RuntimeException(e);
                    } finally {
                        assert searchJob != null;

                        activeJobs.remove(searchJob);

                        pendingJobsCounter.decrementAndGet();
                    }
                });
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
            } catch (InterruptedException e) {
                logger.error("Stopping executorService....");
                executorService.shutdownNow();
            }
        }


        if (epdSearchResults.isEmpty()) {
            throw new RuntimeException("No edp entry was processed");
        }

        if (pendingJobsCounter.get() > 0) {
            throw new RuntimeException(String.format("Todavia siguen pendiente %d busquedas", pendingJobsCounter.get()));
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

        String bestMoveFoundStr = sanEncoder.encodeAlgebraicNotation(bestMove, game.getPossibleMoves());

        return new EpdSearchResult(epdEntry, searchResult, bestMoveFoundStr);
    }

    private record SearchJob(Instant startInstant, SearchMove searchMove) {
        public long elapsedMillis() {
            return Duration.between(startInstant, Instant.now()).toMillis();
        }
    }
}
