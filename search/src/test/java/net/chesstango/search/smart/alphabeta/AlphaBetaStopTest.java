package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Game;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.search.Search;
import net.chesstango.search.SearchResult;
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
        Search search = new AlphaBetaBuilder()
                .withGameEvaluator(Evaluator.getInstance())

                .withQuiescence()

                .withTranspositionTable()

                .withTranspositionMoveSorter()

                .withStopProcessingCatch()

                .withAspirationWindows()

                .withIterativeDeepening()

                .withStatistics()

                .build();


        //Game game = FENDecoder.loadGame("r1bqkb1r/pppppppp/2n5/3nP3/2BP4/8/PPP2PPP/RNBQK1NR b KQkq - 2 4");
        Game game = Game.from(FEN.of("2rr2k1/2p2ppp/1p3bn1/p2P1q2/2P5/1Q4B1/PP3PPP/R2R2K1 w - - 6 22"));

        Future<SearchResult> searchTask = Executors.newSingleThreadExecutor().submit(() -> {
            try {
                return search.search(game);
            } catch (RuntimeException e) {
                e.printStackTrace(System.err);
                throw e;
            }
        });

        Thread.sleep(500);

        search.stopSearching();

        SearchResult searchResult = null;
        try {
            searchResult = searchTask.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        assertNotNull(searchResult);
    }
}
