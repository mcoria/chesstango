package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Game;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.evaluation.evaluators.EvaluatorByMaterial;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.search.Search;
import net.chesstango.search.SearchResult;
import net.chesstango.search.builders.AlphaBetaBuilder;
import net.chesstango.search.visitors.SetSearchByDepthListenerVisitor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaStopTest {

    private ExecutorService singleThreadExecutor;

    @BeforeEach
    public void setup() {
        singleThreadExecutor = Executors.newSingleThreadExecutor();
    }

    @AfterEach
    public void tearDown() {
        singleThreadExecutor.shutdown();
    }

    @Test
    public void testStop() {
        Search search = AlphaBetaBuilder
                .createDefaultBuilderInstance()
                .withGameEvaluator(Evaluator.createInstance())
                .build();

        Game game = Game.from(FEN.from("rnbqkb1r/p4p2/2p1p2p/1p1nP1p1/2pP4/2N2NB1/PP3PPP/R2QKB1R w KQkq - 1 10"));

        CountDownLatch latch = new CountDownLatch(1);

        search.accept(new SetSearchByDepthListenerVisitor(_ -> latch.countDown()));

        Future<SearchResult> searchTask = singleThreadExecutor.submit(() -> {
            try {
                return search.startSearch(game);
            } catch (RuntimeException e) {
                e.printStackTrace(System.err);
                throw e;
            }
        });


        try {
            latch.await();

            search.stopSearch();

            SearchResult searchResult = searchTask.get();

            assertNotNull(searchResult);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace(System.err);
            throw new RuntimeException(e);
        }
    }
}
