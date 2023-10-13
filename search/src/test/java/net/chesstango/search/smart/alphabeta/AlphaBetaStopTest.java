package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Game;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.DefaultEvaluator;
import net.chesstango.evaluation.GameEvaluatorCache;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.builders.AlphaBetaBuilder;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaStopTest {
    @Test
    public void testStop() throws InterruptedException {
        SearchMove search = new AlphaBetaBuilder()
                .withGameEvaluator(new GameEvaluatorCache(new DefaultEvaluator()))

                .withQuiescence()

                .withTranspositionTable()
                .withQTranspositionTable()

                .withTranspositionMoveSorter()
                .withQTranspositionMoveSorter()

                .withStopProcessingCatch()

                .withIterativeDeepening()

                .withAspirationWindows()

                .withStatistics()

                .build();


        //Game game = FENDecoder.loadGame("r1bqkb1r/pppppppp/2n5/3nP3/2BP4/8/PPP2PPP/RNBQK1NR b KQkq - 2 4");
        Game game = FENDecoder.loadGame("2rr2k1/2p2ppp/1p3bn1/p2P1q2/2P5/1Q4B1/PP3PPP/R2R2K1 w - - 6 22");

        Future<SearchMoveResult> searchTask = Executors.newSingleThreadExecutor().submit(() -> {
            try {
                return search.search(game);
            } catch (RuntimeException e) {
                e.printStackTrace(System.err);
                throw e;
            }
        });

        Thread.sleep(500);

        search.stopSearching();

        SearchMoveResult searchMoveResult = null;
        try {
            searchMoveResult = searchTask.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        assertNotNull(searchMoveResult);
    }
}
