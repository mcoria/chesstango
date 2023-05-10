package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Game;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.imp.GameEvaluatorByMaterial;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.builders.MinMaxPruningBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Mauricio Coria
 */
public class MinMaxPrunningTest {


    @Test
    public void test01() throws ExecutionException, InterruptedException {
        SearchMove search = new MinMaxPruningBuilder()
                .withGameEvaluator(new GameEvaluatorByMaterial())
                .withStatics()
                .withTranspositionTable()
                .withQTranspositionTable()

                .withQuiescence()

                .withIterativeDeepening()

                .withTranspositionMoveSorter()
                .withQTranspositionMoveSorter()

                .withGameRevert()

                .build();


        Game game = FENDecoder.loadGame("r1bqkb1r/pppppppp/2n5/3nP3/2BP4/8/PPP2PPP/RNBQK1NR b KQkq - 2 4 ");


        Thread testingThread = Thread.currentThread();

        Future<SearchMoveResult> searchTask = Executors.newSingleThreadExecutor().submit(() -> {
            try {
                SearchMoveResult searchResult = search.searchBestMove(game, 20);

                testingThread.interrupt();

                return searchResult;
            } catch (RuntimeException exception) {
                exception.printStackTrace(System.err);
                throw exception;
            }
        });


        Thread.sleep(1000);

        if (!searchTask.isDone()) {
            search.stopSearching();
            try {
                Thread.sleep(Long.MAX_VALUE);
            } catch (InterruptedException ie) {

            }
        }

        SearchMoveResult result = searchTask.get();

        Assertions.assertTrue(result != null);
    }
}
