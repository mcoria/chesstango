package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Game;
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
        Search search = new AlphaBetaBuilder()
                .withGameEvaluator(new EvaluatorByMaterial())

                .withQuiescence()

                .withTranspositionTable()
                .withTranspositionHashSize(1024)

                .withTranspositionMoveSorter()

                .withStopProcessingCatch()

                .withAspirationWindows()

                .withIterativeDeepening()

                .withStatistics()

                .build();

        Game game = Game.from(FEN.from("2rr2k1/2p2ppp/1p3bn1/p2P1q2/2P5/1Q4B1/PP3PPP/R2R2K1 w - - 6 22"));


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
            throw new RuntimeException(e);
        }
    }
}
