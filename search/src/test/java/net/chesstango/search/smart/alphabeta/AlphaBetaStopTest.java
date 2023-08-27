package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Game;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.evaluators.EvaluatorByMaterial;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.StopSearchingException;
import net.chesstango.search.builders.AlphaBetaBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaStopTest {
    @Test
    public void testStop() throws InterruptedException {
        SearchMove search = new AlphaBetaBuilder()
                .withGameEvaluator(new EvaluatorByMaterial())

                .withQuiescence()

                .withTranspositionTable()
                .withQTranspositionTable()

                .withQuiescence()

                .withIterativeDeepening()

                .withTranspositionMoveSorter()
                .withQTranspositionMoveSorter()

                .withStopProcessingCatch()

                .withtStatistics()

                .build();


        Game game = FENDecoder.loadGame("r1bqkb1r/pppppppp/2n5/3nP3/2BP4/8/PPP2PPP/RNBQK1NR b KQkq - 2 4 ");

        Future<SearchMoveResult> searchTask = Executors.newSingleThreadExecutor().submit(() -> {
            try {
                search.search(game, 20);

                throw new RuntimeException("This point should not be reachable");
            } catch (StopSearchingException exception) {
                throw exception;
            }
        });


        Thread.sleep(1000);

        search.stopSearching();

        ExecutionException exception = null;
        try {
            searchTask.get();
        } catch (ExecutionException executionException) {
            exception = executionException;
        }

        Assertions.assertNotNull(exception);

        Assertions.assertInstanceOf(StopSearchingException.class, exception.getCause());
    }
}
