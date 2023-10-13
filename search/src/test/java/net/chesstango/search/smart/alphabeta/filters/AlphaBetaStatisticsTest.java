package net.chesstango.search.smart.alphabeta.filters;

import net.chesstango.board.Game;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.evaluators.EvaluatorByMaterial;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.SearchParameter;
import net.chesstango.search.builders.AlphaBetaBuilder;
import net.chesstango.search.reports.EvaluationReport;
import net.chesstango.search.reports.NodesReport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * @author Mauricio Coria
 */
public class AlphaBetaStatisticsTest {

    private SearchMoveResult searchResult;

    private static final boolean PRINT_REPORT = false;

    @AfterEach
    public void printReport() {
        if (PRINT_REPORT) {
            new NodesReport()
                    .withMoveResults(List.of(searchResult))
                    .withCutoffStatistics()
                    .withNodesVisitedStatistics()
                    .printReport(System.out);

            new EvaluationReport()
                    .withMoveResults(List.of(searchResult))
                    //.withExportEvaluations()
                    .withEvaluationsStatistics()
                    .printReport(System.out);

            /*
            new PrincipalVariationReport()
                    .withMoveResults(List.of(searchResult))
                    .printReport(System.out);
            */
        }
    }

    @Test
    public void testVisitedNodesCounters_NoIterative() {
        SearchMove moveFinder = new AlphaBetaBuilder()
                .withGameEvaluator(new EvaluatorByMaterial())
                .withStatistics()
                .build();

        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);
        moveFinder.setParameter(SearchParameter.MAX_DEPTH, 2);

        searchResult = moveFinder.search(game);

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
        SearchMove moveFinder = new AlphaBetaBuilder()
                .withGameEvaluator(new EvaluatorByMaterial())
                .withStatistics()
                .build();

        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);
        moveFinder.setParameter(SearchParameter.MAX_DEPTH, 2);

        searchResult = moveFinder.search(game);

        int[] expectedNodesCounters = searchResult.getRegularNodeStatistics().expectedNodesCounters();
        int expectedNodesCountersTotal = Arrays.stream(expectedNodesCounters).sum();

        assertEquals(20, expectedNodesCounters[0]);
        assertEquals(400, expectedNodesCounters[1]);
        assertEquals(420, expectedNodesCountersTotal);
    }


    @Test
    public void testBestMovesCounter_NoIterative() {
        SearchMove moveFinder = new AlphaBetaBuilder()
                .withGameEvaluator(new EvaluatorByMaterial())
                .withTranspositionTable()       // Para detectar la cantidad de mejores movimientos necesita TT
                .withStatistics()
                .build();

        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        moveFinder.setParameter(SearchParameter.MAX_DEPTH, 2);
        searchResult = moveFinder.search(game);

        assertEquals(20, searchResult.getBestMovesCounter());
    }

    @Test
    public void testVisitedNodesCounters_Iterative() {
        SearchMove moveFinder = new AlphaBetaBuilder()
                .withGameEvaluator(new EvaluatorByMaterial())
                .withStatistics()
                .withIterativeDeepening()
                .build();

        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        moveFinder.setParameter(SearchParameter.MAX_DEPTH, 2);
        searchResult = moveFinder.search(game);

        int[] visitedNodesCounters = searchResult.getRegularNodeStatistics().visitedNodesCounters();
        int visitedNodesCountersTotal = Arrays.stream(visitedNodesCounters).sum();

        assertEquals(40, visitedNodesCounters[0]);
        assertEquals(39, visitedNodesCounters[1]);
        assertEquals(79, visitedNodesCountersTotal);
    }

    @Test
    public void testExpectedNodesCounters_Iterative() {
        SearchMove moveFinder = new AlphaBetaBuilder()
                .withGameEvaluator(new EvaluatorByMaterial())
                .withStatistics()
                .withIterativeDeepening()
                .build();

        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        moveFinder.setParameter(SearchParameter.MAX_DEPTH, 2);
        searchResult = moveFinder.search(game);

        int[] expectedNodesCounters = searchResult.getRegularNodeStatistics().expectedNodesCounters();
        int expectedNodesCountersTotal = Arrays.stream(expectedNodesCounters).sum();

        assertEquals(40, expectedNodesCounters[0]);
        assertEquals(400, expectedNodesCounters[1]);
        assertEquals(0, expectedNodesCounters[2]);
        assertEquals(440, expectedNodesCountersTotal);
    }


    @Test
    public void testBestMovesCounter_Iterative() {
        SearchMove moveFinder = new AlphaBetaBuilder()
                .withGameEvaluator(new EvaluatorByMaterial())
                .withTranspositionTable()       // Para detectar la cantidad de mejores movimientos necesita TT
                .withStatistics()
                .withIterativeDeepening()
                .build();

        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        moveFinder.setParameter(SearchParameter.MAX_DEPTH, 2);
        searchResult = moveFinder.search(game);

        assertEquals(20, searchResult.getBestMovesCounter());
    }

    @Test
    public void testSearch_01() {
        SearchMove moveFinder = new AlphaBetaBuilder()
                .withGameEvaluator(new EvaluatorByMaterial())
                .withStatistics()
                .build();

        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        moveFinder.setParameter(SearchParameter.MAX_DEPTH, 1);
        searchResult = moveFinder.search(game);

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
        SearchMove moveFinder = new AlphaBetaBuilder()
                .withGameEvaluator(new EvaluatorByMaterial())
                .withTranspositionTable()
                .withStatistics()
                .build();

        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        moveFinder.setParameter(SearchParameter.MAX_DEPTH, 1);
        searchResult = moveFinder.search(game);

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
         *  1 movimiento  de SetPrincipalVariation
         */
        assertEquals(21, executedMoves);

    }

    @Test
    public void testSearch_03() {
        SearchMove moveFinder = new AlphaBetaBuilder()
                .withGameEvaluator(new EvaluatorByMaterial())
                .withQuiescence()
                .withStatistics()
                .build();

        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        moveFinder.setParameter(SearchParameter.MAX_DEPTH, 1);
        searchResult = moveFinder.search(game);

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
        SearchMove moveFinder = new AlphaBetaBuilder()
                .withGameEvaluator(new EvaluatorByMaterial())
                .withQuiescence()
                .withTranspositionTable()
                .withQTranspositionTable()
                .withStatistics()
                .build();

        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        moveFinder.setParameter(SearchParameter.MAX_DEPTH, 1);
        searchResult = moveFinder.search(game);

        int[] visitedNodesCounters = searchResult.getRegularNodeStatistics().visitedNodesCounters();
        int visitedNodesCountersTotal = Arrays.stream(visitedNodesCounters).sum();
        int[] visitedQNodesCounters = searchResult.getQuiescenceNodeStatistics().visitedNodesCounters();
        int visitedQNodesCountersTotal = Arrays.stream(visitedQNodesCounters).sum();
        int executedMoves = searchResult.getExecutedMoves();

        assertEquals(20, visitedNodesCounters[0]);
        assertEquals(20, visitedNodesCountersTotal);
        assertEquals(0, visitedQNodesCounters[0]);
        assertEquals(0, visitedQNodesCountersTotal);
        assertEquals(21, executedMoves); // Ver mas arriba explicacion

    }
}
