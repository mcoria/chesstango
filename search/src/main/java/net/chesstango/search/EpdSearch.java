package net.chesstango.search;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.EPDEntry;
import net.chesstango.board.representations.SANEncoder;
import net.chesstango.evaluation.DefaultEvaluator;
import net.chesstango.evaluation.GameEvaluatorCache;
import net.chesstango.search.builders.AlphaBetaBuilder;

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
    private static final int SEARCH_THREADS = 3;

    @Setter
    @Accessors(chain = true)
    private SearchMove searchMove;

    @Setter
    @Accessors(chain = true)
    private int depth;

    protected static final SANEncoder sanEncoder = new SANEncoder();


    public EpdSearchResult run(EPDEntry epdEntry) {
        return run(searchMove, epdEntry);
    }

    public EpdSearchResult run(SearchMove searchMove, EPDEntry epdEntry) {

        Instant start = Instant.now();

        SearchMoveResult searchResult = searchMove.search(epdEntry.game, depth);

        long duration = Duration.between(start, Instant.now()).toMillis();

        searchResult.setEpdID(epdEntry.id);

        Move bestMove = searchResult.getBestMove();

        String bestMoveFoundStr = sanEncoder.encode(bestMove, epdEntry.game.getPossibleMoves());

        boolean bestMoveFound = epdEntry.bestMoves.contains(bestMove);

        return new EpdSearchResult(epdEntry, bestMoveFoundStr, bestMoveFound, duration, searchResult);
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
                            System.out.printf("Success %s\n", epdEntry.fen);
                        } else {
                            String failedTest = String.format("Fail [%s] - best move found %s",
                                    epdEntry.text,
                                    epdSearchResult.bestMoveFoundStr()
                            );
                            System.out.println(failedTest);
                        }
                        return epdSearchResult;
                    } catch (RuntimeException e) {
                        e.printStackTrace(System.err);
                        throw e;
                    } finally {
                        assert searchMove != null;
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
            System.out.println("Stopping executorService....");
            executorService.shutdownNow();
        }

        List<EpdSearchResult> epdSearchResults = new LinkedList<>();
        futures.forEach(future -> {
            try {
                EpdSearchResult epdSearchResult = future.get();
                epdSearchResults.add(epdSearchResult);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });


        if (epdSearchResults.isEmpty()) {
            throw new RuntimeException("No edp entry was processed");
        }

        epdSearchResults.sort(Comparator.comparing(o -> o.epdEntry().id));


        return epdSearchResults;
    }


    private static SearchMove buildSearchMove() {
        return new AlphaBetaBuilder()
                .withGameEvaluator(new GameEvaluatorCache(new DefaultEvaluator()))

                .withQuiescence()

                .withTranspositionTable()
                .withQTranspositionTable()
                //.withTranspositionTableReuse()

                .withTranspositionMoveSorter()
                .withQTranspositionMoveSorter()

                //.withStopProcessingCatch()

                .withIterativeDeepening()
                //.withMoveEvaluation()

                .withStatistics()
                .withTrackEvaluations() // Consume demasiada memoria

                .build();
    }
}
