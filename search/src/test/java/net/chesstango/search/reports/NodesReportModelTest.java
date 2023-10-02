package net.chesstango.search.reports;

import net.chesstango.board.Game;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.evaluators.EvaluatorByMaterial;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.builders.AlphaBetaBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class NodesReportModelTest {

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
    public void testSearch_01() {
        SearchMove moveFinder = new AlphaBetaBuilder()
                .withGameEvaluator(new EvaluatorByMaterial())
                .withStatistics()
                .build();

        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        searchResult = moveFinder.search(game, 1);

        NodesReportModel model = NodesReportModel.collectStatistics("", List.of(searchResult));

        assertEquals(20, model.visitedRNodesTotal);
        assertEquals(0, model.visitedQNodesTotal);
        assertEquals(20, model.visitedNodesTotal);
        assertEquals(20, model.executedMovesTotal);
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

        NodesReportModel model = NodesReportModel.collectStatistics("", List.of(searchResult));

        assertEquals(20, model.visitedRNodesTotal);
        assertEquals(0, model.visitedQNodesTotal);
        assertEquals(20, model.visitedNodesTotal);

        /**
         * Los 61 movimientos se descomponen en:
         * 20 movimientos de la busqueda regular AlphaBeta
         *  1 movimiento  de SetPrincipalVariation
         * 20 movimientos de SetMoveEvaluations
         * 20 movimientos de SetBestMoves
         */
        assertEquals(61, model.executedMovesTotal);

    }
}
