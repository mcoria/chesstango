package net.chesstango.search;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.EPDEntry;
import net.chesstango.board.representations.SANEncoder;
import net.chesstango.evaluation.DefaultEvaluator;
import net.chesstango.search.builders.AlphaBetaBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author Mauricio Coria
 */
public class EpdSearch {
    private static final Logger logger = LoggerFactory.getLogger(EpdSearch.class);

    private static final int SEARCH_THREADS = 4;

    protected static final SANEncoder sanEncoder = new SANEncoder();

    @Setter
    @Accessors(chain = true)
    private SearchMove searchMove;

    @Setter
    @Accessors(chain = true)
    private int depth;


    public EpdSearchResult run(EPDEntry epdEntry) {
        return run(searchMove, epdEntry);
    }

    public List<EpdSearchResult> run(List<EPDEntry> edpEntries) {
        ExecutorService executorService = Executors.newFixedThreadPool(SEARCH_THREADS);

        BlockingQueue<SearchMove> blockingQueue = new LinkedBlockingDeque<>(SEARCH_THREADS);

        for (int i = 0; i < SEARCH_THREADS; i++) {
            blockingQueue.add(buildSearchMove());
        }

        List<Future<EpdSearchResult>> futures = new LinkedList<>();
        for (EPDEntry epdEntry : edpEntries) {
            Future<EpdSearchResult> future = executorService.submit(new Callable<>() {
                @Override
                public EpdSearchResult call() throws Exception {
                    SearchMove searchMove = null;
                    try {
                        searchMove = blockingQueue.take();
                        EpdSearchResult epdSearchResult = new EpdSearch()
                                .setSearchMove(searchMove)
                                .setDepth(depth)
                                .run(epdEntry);
                        if (epdSearchResult.bestMoveFound()) {
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
                        logger.error(String.format("Error processing: %s", epdEntry.fen));
                        throw e;
                    } finally {
                        blockingQueue.put(searchMove);
                    }
                }
            });

            futures.add(future);
        }

        executorService.shutdown();
        try {
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

        Instant start = Instant.now();

        searchMove.setParameter(SearchParameter.MAX_DEPTH, depth);
        SearchMoveResult searchResult = searchMove.search(epdEntry.game);

        long duration = Duration.between(start, Instant.now()).toMillis();

        searchResult.setEpdID(epdEntry.id);

        Move bestMove = searchResult.getBestMove();

        String bestMoveFoundStr = sanEncoder.encode(bestMove, epdEntry.game.getPossibleMoves());

        boolean bestMoveFound = epdEntry.bestMoves.contains(bestMove);

        return new EpdSearchResult(epdEntry, searchResult, bestMoveFoundStr, bestMoveFound, duration);
    }


    private static SearchMove buildSearchMove() {
        return new AlphaBetaBuilder()
                .withGameEvaluator(new DefaultEvaluator())
                //.withGameEvaluatorCache()

                .withQuiescence()

                //.withTranspositionTable()
                //.withQTranspositionTable()

                //.withTranspositionMoveSorter()
                //.withQTranspositionMoveSorter()

                .withIterativeDeepening()
                .withAspirationWindows()
                .withTriangularPV()

                .withStatistics()
                //.withTrackEvaluations() // Consume demasiada memoria

                .build();
    }
}
