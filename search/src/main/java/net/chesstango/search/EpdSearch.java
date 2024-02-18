package net.chesstango.search;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.EPDEntry;
import net.chesstango.board.representations.move.SANEncoder;
import net.chesstango.evaluation.DefaultEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Supplier;

/**
 * @author Mauricio Coria
 */
public class EpdSearch {
    private static final Logger logger = LoggerFactory.getLogger(EpdSearch.class);

    private static final int SEARCH_THREADS = 4;

    private static final SANEncoder sanEncoder = new SANEncoder();

    @Setter
    @Accessors(chain = true)
    private Supplier<SearchMove> searchMoveSupplier;

    @Setter
    @Accessors(chain = true)
    private int depth;


    public EpdSearchResult run(EPDEntry epdEntry) {
        return run(searchMoveSupplier.get(), epdEntry);
    }

    public List<EpdSearchResult> run(List<EPDEntry> edpEntries) {
        ExecutorService executorService = Executors.newFixedThreadPool(SEARCH_THREADS);

        BlockingQueue<SearchMove> blockingQueue = new LinkedBlockingDeque<>(SEARCH_THREADS);

        for (int i = 0; i < SEARCH_THREADS; i++) {
            blockingQueue.add(searchMoveSupplier.get());
        }

        List<Future<EpdSearchResult>> futures = new LinkedList<>();
        for (EPDEntry epdEntry : edpEntries) {
            Future<EpdSearchResult> future = executorService.submit(() -> {
                SearchMove searchMove = null;
                try {
                    searchMove = blockingQueue.take();
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
                    assert searchMove != null;
                    blockingQueue.put(searchMove);
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

        searchMove.setSearchParameter(SearchParameter.MAX_DEPTH, depth);

        SearchMoveResult searchResult = searchMove.search(epdEntry.game);

        searchResult.setEpdID(epdEntry.id);

        searchMove.reset();

        Move bestMove = searchResult.getBestMove();

        String bestMoveFoundStr = sanEncoder.encode(bestMove, epdEntry.game.getPossibleMoves());

        boolean epdSearchResult;
        if (epdEntry.bestMoves != null && !epdEntry.bestMoves.isEmpty()) {
            epdSearchResult = epdEntry.bestMoves.contains(bestMove);
        } else if (epdEntry.avoidMoves != null && !epdEntry.avoidMoves.isEmpty()) {
            epdSearchResult = !epdEntry.avoidMoves.contains(bestMove);
        } else {
            throw new RuntimeException("Undefined expected EPD result");
        }


        return new EpdSearchResult(epdEntry, searchResult, bestMoveFoundStr, epdSearchResult);
    }
}
