package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.evaluation.evaluators.EvaluatorByMaterial;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.builders.AlphaBetaBuilder;
import net.chesstango.search.reports.EvaluationReport;
import net.chesstango.search.reports.NodesReport;
import net.chesstango.search.reports.NodesReportModel;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.alphabeta.filters.*;
import net.chesstango.search.smart.alphabeta.listeners.SetBestMoves;
import net.chesstango.search.smart.alphabeta.listeners.SetTranspositionTables;
import net.chesstango.search.smart.sorters.DefaultMoveSorter;
import net.chesstango.search.smart.sorters.MoveSorter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


/**
 * @author Mauricio Coria
 */
public class AlphaBetaStatisticsTest {

    private SearchMoveResult searchResult;

    private static final boolean PRINT_REPORT = true;

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
    @Disabled
    public void testDistinctMoves() {
        SearchMove moveFinder = new AlphaBetaBuilder()
                .withGameEvaluator(new EvaluatorByMaterial())
                .withTranspositionTable()
                .withStatistics()
                .build();

        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        SearchMoveResult searchResult = moveFinder.search(game, 2);

        Set<Move>[] distinctMoves = null; //searchResult.getDistinctMovesPerLevel();

        assertEquals(20, distinctMoves[0].size());
        assertEquals(20, distinctMoves[1].size());
    }

    @Test
    public void testVisitedNodesCounters() {
        SearchMove moveFinder = new AlphaBetaBuilder()
                .withGameEvaluator(new EvaluatorByMaterial())
                .withTranspositionTable()
                .withStatistics()
                .build();

        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        searchResult = moveFinder.search(game, 2);

        int[] visitedNodesCounters = searchResult.getRegularNodeStatistics().visitedNodesCounters();

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
    }

    @Test
    public void testExpectedNodesCounters() {
        SearchMove moveFinder = new AlphaBetaBuilder()
                .withGameEvaluator(new EvaluatorByMaterial())
                .withTranspositionTable()
                .withStatistics()
                .build();

        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        searchResult = moveFinder.search(game, 2);

        int[] visitedNodesCounters = searchResult.getRegularNodeStatistics().expectedNodesCounters();

        assertEquals(20, visitedNodesCounters[0]);
        assertEquals(400, visitedNodesCounters[1]);
    }

    @Test
    public void testEvaluationCollisions() {
        SearchMove moveFinder = new AlphaBetaBuilder()
                .withGameEvaluator(new EvaluatorByMaterial())
                .withTranspositionTable()
                .withStatistics()
                .build();

        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        searchResult = moveFinder.search(game, 2);

        assertEquals(20, searchResult.getBestMovesCounter());
    }

    @Test
    public void testSearch_01() {
        SearchMove moveFinder = new AlphaBetaBuilder()
                .withGameEvaluator(new EvaluatorByMaterial())
                .withStatistics()
                .build();

        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        searchResult = moveFinder.search(game, 1);

        int[] visitedNodesCounters = searchResult.getRegularNodeStatistics().visitedNodesCounters();
        int visitedNodesCountersTotal = Arrays.stream(searchResult.getRegularNodeStatistics().visitedNodesCounters()).sum();
        int executedMoves = searchResult.getExecutedMoves();

        assertEquals(20, visitedNodesCounters[0]);
        assertEquals(20, visitedNodesCountersTotal);
        assertNull(searchResult.getQuiescenceNodeStatistics());
        assertEquals(20, executedMoves);
    }

    @Test
    public void testSearch_02() {
        SearchMove moveFinder = new AlphaBetaBuilder()
                .withGameEvaluator(new EvaluatorByMaterial())
                .withTranspositionTable()
                .withStatistics()
                .build();

        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        searchResult = moveFinder.search(game, 1);

        int[] visitedNodesCounters = searchResult.getRegularNodeStatistics().visitedNodesCounters();
        int visitedNodesCountersTotal = Arrays.stream(searchResult.getRegularNodeStatistics().visitedNodesCounters()).sum();
        int executedMoves = searchResult.getExecutedMoves();

        assertEquals(20, visitedNodesCounters[0]);
        assertEquals(20, visitedNodesCountersTotal);
        assertNull(searchResult.getQuiescenceNodeStatistics());
        /**
         * Los 61 movimientos se descomponen en:
         * 20 movimientos de la busqueda regular AlphaBeta
         *  1 movimiento  de SetPrincipalVariation
         * 20 movimientos de SetMoveEvaluations
         * 20 movimientos de SetBestMoves
         */
        assertEquals(61, executedMoves);

    }

    @Test
    public void testSearch_03() {
        SearchMove moveFinder = new AlphaBetaBuilder()
                .withGameEvaluator(new EvaluatorByMaterial())
                .withQuiescence()
                .withStatistics()
                .build();

        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        searchResult = moveFinder.search(game, 1);

        int[] visitedNodesCounters = searchResult.getRegularNodeStatistics().visitedNodesCounters();
        int visitedNodesCountersTotal = Arrays.stream(visitedNodesCounters).sum();
        int[] visitedQNodesCounters = searchResult.getQuiescenceNodeStatistics().visitedNodesCounters();
        int visitedQNodesCountersTotal = Arrays.stream(visitedQNodesCounters).sum();
        int executedMoves = searchResult.getExecutedMoves();

        assertEquals(20, visitedNodesCounters[0]);
        assertEquals(20, visitedNodesCountersTotal);
        assertEquals(0, visitedQNodesCounters[0]);
        assertEquals(0, visitedQNodesCountersTotal);
        assertEquals(20, executedMoves);
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

        searchResult = moveFinder.search(game, 1);

        int[] visitedNodesCounters = searchResult.getRegularNodeStatistics().visitedNodesCounters();
        int visitedNodesCountersTotal = Arrays.stream(visitedNodesCounters).sum();
        int[] visitedQNodesCounters = searchResult.getQuiescenceNodeStatistics().visitedNodesCounters();
        int visitedQNodesCountersTotal = Arrays.stream(visitedQNodesCounters).sum();
        int executedMoves = searchResult.getExecutedMoves();

        assertEquals(20, visitedNodesCounters[0]);
        assertEquals(20, visitedNodesCountersTotal);
        assertEquals(0, visitedQNodesCounters[0]);
        assertEquals(0, visitedQNodesCountersTotal);
        assertEquals(61, executedMoves); // Ver mas arriba explicacion

    }
}
