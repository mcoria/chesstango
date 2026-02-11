package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Game;
import net.chesstango.evaluation.evaluators.EvaluatorByMaterial;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.search.Search;
import net.chesstango.search.SearchResult;
import net.chesstango.search.builders.AlphaBetaBuilder;
import net.chesstango.search.smart.features.statistics.node.NodeStatistics;
import net.chesstango.search.visitors.SetMaxDepthVisitor;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * @author Mauricio Coria
 */
public class AlphaBetaStatisticsTest {

    private SearchResult searchResult;

    @Test
    public void testVisitedNodesCounters_NoIterative() {
        Search moveFinder = new AlphaBetaBuilder()
                .withGameEvaluator(new EvaluatorByMaterial())
                .withStatistics()
                .build();

        Game game = Game.from(FEN.START_POSITION);
        moveFinder.accept(new SetMaxDepthVisitor(2));

        searchResult = moveFinder.startSearch(game);

        int[] visitedNodesCounters = searchResult.getRegularNodeStatistics().visitedNodesCounters();
        int visitedNodesCountersTotal = Arrays.stream(visitedNodesCounters).sum();

        assertEquals(20, visitedNodesCounters[0]);

        /**
         * En el 1er ciclo de exploracion se ejecuta el movimiento de una pieza blanca y el de 20 negras.
         * Dado que la evaluacion es por material y no hay ningun tipo de captura tenemos que alpha = 0
         * Desde el 2do al 20mo ciclo (los otros 19 restantes) se ejecuta un movimiento de pieza blanca y
         * SOLO un movimiento de pieza negra. Cada movimiento de pieza negra produce un beta cutoff debido a que
         * la evaluacion vuelve a ser 0 y no es necesario explorar otros movimientos de pieza negra.
         * Esto se continua repitiendo hasta finalizar los 19 ciclos restantes.
         */
        assertEquals(39, visitedNodesCounters[1]); // ESTA PERFECTO ES 39!!!!

        assertEquals(59, visitedNodesCountersTotal);
    }

    @Test
    public void testExpectedNodesCounters_NoIterative() {
        Search moveFinder = new AlphaBetaBuilder()
                .withGameEvaluator(new EvaluatorByMaterial())
                .withStatistics()
                .build();

        Game game = Game.from(FEN.START_POSITION);
        moveFinder.accept(new SetMaxDepthVisitor(2));

        searchResult = moveFinder.startSearch(game);

        int[] expectedNodesCounters = searchResult.getRegularNodeStatistics().expectedNodesCounters();
        int expectedNodesCountersTotal = Arrays.stream(expectedNodesCounters).sum();

        assertEquals(20, expectedNodesCounters[0]);
        assertEquals(400, expectedNodesCounters[1]);
        assertEquals(420, expectedNodesCountersTotal);
    }


    @Test
    public void testVisitedNodesCounters_Iterative() {
        Search moveFinder = new AlphaBetaBuilder()
                .withGameEvaluator(new EvaluatorByMaterial())
                .withStatistics()
                .withIterativeDeepening()
                .build();

        Game game = Game.from(FEN.START_POSITION);

        moveFinder.accept(new SetMaxDepthVisitor(2));
        searchResult = moveFinder.startSearch(game);

        int[] visitedNodesCounters = searchResult.getRegularNodeStatistics().visitedNodesCounters();
        int visitedNodesCountersTotal = Arrays.stream(visitedNodesCounters).sum();

        assertEquals(40, visitedNodesCounters[0]);
        assertEquals(39, visitedNodesCounters[1]);
        assertEquals(79, visitedNodesCountersTotal);
    }

    @Test
    public void testExpectedNodesCounters_Iterative() {
        Search moveFinder = new AlphaBetaBuilder()
                .withGameEvaluator(new EvaluatorByMaterial())
                .withStatistics()
                .withIterativeDeepening()
                .build();

        Game game = Game.from(FEN.START_POSITION);
        
        moveFinder.accept(new SetMaxDepthVisitor(2));
        searchResult = moveFinder.startSearch(game);

        int[] expectedNodesCounters = searchResult.getRegularNodeStatistics().expectedNodesCounters();
        int expectedNodesCountersTotal = Arrays.stream(expectedNodesCounters).sum();

        assertEquals(40, expectedNodesCounters[0]);
        assertEquals(400, expectedNodesCounters[1]);
        assertEquals(0, expectedNodesCounters[2]);
        assertEquals(440, expectedNodesCountersTotal);
    }


    @Test
    public void testSearch_01() {
        Search moveFinder = new AlphaBetaBuilder()
                .withGameEvaluator(new EvaluatorByMaterial())
                .withStatistics()
                .build();

        Game game = Game.from(FEN.START_POSITION);

        moveFinder.accept(new SetMaxDepthVisitor(1));
        searchResult = moveFinder.startSearch(game);

        int[] visitedNodesCounters = searchResult.getRegularNodeStatistics().visitedNodesCounters();
        int visitedNodesCountersTotal = Arrays.stream(searchResult.getRegularNodeStatistics().visitedNodesCounters()).sum();
        int[] visitedQNodesCounters = searchResult.getQuiescenceNodeStatistics().visitedNodesCounters();
        int visitedQNodesCountersTotal = Arrays.stream(visitedQNodesCounters).sum();
        int executedMoves = searchResult.getExecutedMoves();

        assertEquals(20, visitedNodesCounters[0]);
        assertEquals(20, visitedNodesCountersTotal);
        assertEquals(0, visitedQNodesCounters[0]);
        assertEquals(0, visitedQNodesCountersTotal);
        assertEquals(21, executedMoves);
    }

    @Test
    public void testSearch_02() {
        Search moveFinder = new AlphaBetaBuilder()
                .withGameEvaluator(new EvaluatorByMaterial())
                .withTranspositionTable()
                .withStatistics()
                .build();

        Game game = Game.from(FEN.START_POSITION);

        moveFinder.accept(new SetMaxDepthVisitor(1));
        searchResult = moveFinder.startSearch(game);

        int[] visitedNodesCounters = searchResult.getRegularNodeStatistics().visitedNodesCounters();
        int visitedNodesCountersTotal = Arrays.stream(searchResult.getRegularNodeStatistics().visitedNodesCounters()).sum();
        int[] visitedQNodesCounters = searchResult.getQuiescenceNodeStatistics().visitedNodesCounters();
        int visitedQNodesCountersTotal = Arrays.stream(visitedQNodesCounters).sum();
        int executedMoves = searchResult.getExecutedMoves();

        assertEquals(20, visitedNodesCounters[0]);
        assertEquals(20, visitedNodesCountersTotal);
        assertEquals(0, visitedQNodesCounters[0]);
        assertEquals(0, visitedQNodesCountersTotal);
        /**
         * Los 61 movimientos se descomponen en:
         * 20 movimientos de la busqueda regular AlphaBeta
         *  0 movimientos de TranspositionPV
         */
        assertEquals(20, executedMoves);

    }

    @Test
    public void testSearch_03() {
        Search moveFinder = new AlphaBetaBuilder()
                .withGameEvaluator(new EvaluatorByMaterial())
                .withQuiescence()
                .withStatistics()
                .build();

        Game game = Game.from(FEN.START_POSITION);

        moveFinder.accept(new SetMaxDepthVisitor(1));
        searchResult = moveFinder.startSearch(game);

        int[] visitedNodesCounters = searchResult.getRegularNodeStatistics().visitedNodesCounters();
        int visitedNodesCountersTotal = Arrays.stream(visitedNodesCounters).sum();
        int[] visitedQNodesCounters = searchResult.getQuiescenceNodeStatistics().visitedNodesCounters();
        int visitedQNodesCountersTotal = Arrays.stream(visitedQNodesCounters).sum();
        int executedMoves = searchResult.getExecutedMoves();

        assertEquals(20, visitedNodesCounters[0]);
        assertEquals(20, visitedNodesCountersTotal);
        assertEquals(0, visitedQNodesCounters[0]);
        assertEquals(0, visitedQNodesCountersTotal);
        assertEquals(21, executedMoves);
    }

    @Test
    public void testSearch_04() {
        Search moveFinder = new AlphaBetaBuilder()
                .withGameEvaluator(new EvaluatorByMaterial())
                .withQuiescence()
                .withTranspositionTable()
                .withStatistics()
                .build();

        Game game = Game.from(FEN.START_POSITION);

        moveFinder.accept(new SetMaxDepthVisitor(1));
        searchResult = moveFinder.startSearch(game);

        int[] visitedNodesCounters = searchResult.getRegularNodeStatistics().visitedNodesCounters();
        int visitedNodesCountersTotal = Arrays.stream(visitedNodesCounters).sum();
        int[] visitedQNodesCounters = searchResult.getQuiescenceNodeStatistics().visitedNodesCounters();
        int visitedQNodesCountersTotal = Arrays.stream(visitedQNodesCounters).sum();
        int executedMoves = searchResult.getExecutedMoves();

        assertEquals(20, visitedNodesCounters[0]);
        assertEquals(20, visitedNodesCountersTotal);
        assertEquals(0, visitedQNodesCounters[0]);
        assertEquals(0, visitedQNodesCountersTotal);
        assertEquals(20, executedMoves); // Ver mas arriba explicacion
    }

    @Test
    public void testSearch_05() {
        Search moveFinder = new AlphaBetaBuilder()
                .withGameEvaluator(new EvaluatorByMaterial())
                .withTranspositionTable()
                .withIterativeDeepening()
                .withStatistics()
                .build();

        Game game = Game.from(FEN.of("k1n5/p1p1p3/PpPpP3/1P1P4/4p1p1/3pPpPp/3P1P1P/5N1K w - - 0 1"));

        moveFinder.accept(new SetMaxDepthVisitor(7));
        searchResult = moveFinder.startSearch(game);

        int[] visitedNodesCounters = searchResult.getRegularNodeStatistics().visitedNodesCounters();
        int visitedNodesCountersTotal = Arrays.stream(visitedNodesCounters).sum();
        int executedMoves = searchResult.getExecutedMoves();

        assertEquals(7, visitedNodesCounters[0]);
        assertEquals(6, visitedNodesCounters[1]);
        assertEquals(5, visitedNodesCounters[2]);
        assertEquals(4, visitedNodesCounters[3]);
        assertEquals(0, visitedNodesCounters[4]);
        assertEquals(0, visitedNodesCounters[5]);

        assertEquals(22, visitedNodesCountersTotal);
        assertEquals(43, executedMoves); // Ver mas arriba explicacion
    }


    @Test
    public void testSearch_06() {
        Search moveFinder = new AlphaBetaBuilder()
                .withGameEvaluator(new EvaluatorByMaterial())
                .withIterativeDeepening()
                .withStatistics()
                .build();

        Game game = Game.from(FEN.of("k1n5/p1p1p3/PpPpP3/1P1P4/4p1p1/3pPpPp/3P1P1P/5N1K w - - 0 1"));

        moveFinder.accept(new SetMaxDepthVisitor(7));
        searchResult = moveFinder.startSearch(game);

        int[] visitedNodesCounters = searchResult.getRegularNodeStatistics().visitedNodesCounters();
        int visitedNodesCountersTotal = Arrays.stream(visitedNodesCounters).sum();
        int executedMoves = searchResult.getExecutedMoves();

        assertEquals(7, visitedNodesCounters[0]);
        assertEquals(6, visitedNodesCounters[1]);
        assertEquals(5, visitedNodesCounters[2]);
        assertEquals(4, visitedNodesCounters[3]);
        assertEquals(0, visitedNodesCounters[4]); // Esta perfecto, el 4to movimiento conduce a una es una repeticion
        assertEquals(0, visitedNodesCounters[5]);
        assertEquals(0, visitedNodesCounters[6]);
        assertEquals(0, visitedNodesCounters[7]);

        assertEquals(22, visitedNodesCountersTotal);
        assertEquals(44, executedMoves);
    }


    @Test
    public void test_40H_2820_mirror() {
        Search moveFinder = new AlphaBetaBuilder()
                .withGameEvaluator(new EvaluatorByMaterial())
                .withIterativeDeepening()
                .withAspirationWindows()
                .withStatistics()
                //.withDebugSearchTree(null, false, true, true)
                .build();


        Game game = Game.from(FEN.of("8/2p5/2P5/p7/k1B5/2K5/2N1p3/8 w - - 0 1"));
        moveFinder.accept(new SetMaxDepthVisitor(5));
        searchResult = moveFinder.startSearch(game);

        /**
         * Ahora se prueba el inverso
         */
        Game game1 = Game.from(FEN.of("8/2n1P3/2k5/K1b5/P7/2p5/2P5/8 b - - 0 1"));
        moveFinder.accept(new SetMaxDepthVisitor(5));
        SearchResult searchResult1 = moveFinder.startSearch(game1);


        NodeStatistics quiescenceNodeStatistics = searchResult.getQuiescenceNodeStatistics();
        int[] visitedNodesQuiescenceCounter = quiescenceNodeStatistics.visitedNodesCounters();

        NodeStatistics quiescenceNodeStatistics1 = searchResult1.getQuiescenceNodeStatistics();
        int[] visitedNodesQuiescenceCounter1 = quiescenceNodeStatistics1.visitedNodesCounters();

        NodeStatistics regularNodeStatistics = searchResult.getRegularNodeStatistics();
        NodeStatistics regularNodeStatistics1 = searchResult1.getRegularNodeStatistics();

        int[] expectedNodesCounters = regularNodeStatistics.expectedNodesCounters();
        int[] visitedNodesCounters = regularNodeStatistics.visitedNodesCounters();

        int[] expectedNodesCounters1 = regularNodeStatistics1.expectedNodesCounters();
        int[] visitedNodesCounters1 = regularNodeStatistics1.visitedNodesCounters();

        for (int i = 0; i < 30; i++) {
            assertEquals(expectedNodesCounters[i], expectedNodesCounters1[i]);
            assertEquals(visitedNodesCounters[i], visitedNodesCounters1[i]);
            assertEquals(visitedNodesQuiescenceCounter[i], visitedNodesQuiescenceCounter1[i]);
        }

        assertEquals(searchResult.getExecutedMoves(), searchResult1.getExecutedMoves());
    }
}
