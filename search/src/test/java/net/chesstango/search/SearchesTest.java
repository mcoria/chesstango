package net.chesstango.search;

import net.chesstango.board.Game;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.DefaultEvaluator;
import net.chesstango.search.builders.AlphaBetaBuilder;
import net.chesstango.search.reports.NodesReport;
import net.chesstango.search.reports.PrincipalVariationReport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public class SearchesTest {
    private static final boolean PRINT_REPORT = false;
    private SearchMove searchMove;
    private SearchMoveResult searchResult;

    @BeforeEach
    public void setup() {
        searchResult = null;

        searchMove = new AlphaBetaBuilder()
                .withGameEvaluator(new DefaultEvaluator())
                //.withGameEvaluatorCache()

                .withQuiescence()

                //.withTranspositionTable()
                //.withQTranspositionTable()

                //.withTranspositionMoveSorter()
                //.withQTranspositionMoveSorter()

                .withIterativeDeepening()
                .withAspirationWindows()
                //.withTriangularPV()

                .withStatistics()
                //.withZobristTracker()
                //.withTrackEvaluations() // Consume demasiada memoria
                //.withMoveEvaluation()

                .withPrintChain()
                .withDebugSearchTree()

                .build();
    }

    @AfterEach
    public void printReport() {
        if (PRINT_REPORT) {
            new NodesReport()
                    .withMoveResults(List.of(searchResult))
                    .withCutoffStatistics()
                    .withNodesVisitedStatistics()
                    .printReport(System.out);

                        /*
            new EvaluationReport()
                    .withMoveResults(List.of(searchResult))
                    //.withExportEvaluations()
                    .withEvaluationsStatistics()
                    .printReport(System.out);
            */
            new PrincipalVariationReport()
                    .withMoveResults(List.of(searchResult))
                    .printReport(System.out);
        }
    }

    @Test
    @Disabled
    public void testSearch_00() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        searchMove.setSearchParameter(SearchParameter.MAX_DEPTH, 6);
        searchResult = searchMove.search(game);
    }


    @Test
    @Disabled
    public void testSearch_01() {
        Game game = FENDecoder.loadGame("r4rk1/p1qbp1b1/2p3pp/2Pn1p2/1pQ5/5B2/PPP1NPPP/R1B2RK1 w - - 1 22");

        searchMove.setSearchParameter(SearchParameter.MAX_DEPTH, 6);
        searchResult = searchMove.search(game);
    }

    @Test
    @Disabled
    public void testSearch_02() {
        Game game = FENDecoder.loadGame("1k2r3/1pp5/4B3/1P3Q2/3q1Pp1/3n2Pp/3p3P/5R1K b - - 0 1");

        searchMove.setSearchParameter(SearchParameter.MAX_DEPTH, 3);
        searchResult = searchMove.search(game);
    }


    @Test
    @Disabled
    public void testSearch_03() {
        Game game = FENDecoder.loadGame("8/p7/2R5/4k3/8/Pp1b3P/1r3PP1/6K1 w - - 2 43");

        searchMove.setSearchParameter(SearchParameter.MAX_DEPTH, 7);
        searchResult = searchMove.search(game);
    }


    @Test
    @Disabled
    public void testSearch_04() {
        Game game = FENDecoder.loadGame("4R3/6pk/1p4Bp/5p2/p5P1/2BP3P/5P2/6K1 b - - 0 39");

        searchMove.setSearchParameter(SearchParameter.MAX_DEPTH, 7);
        searchResult = searchMove.search(game);
    }


    @Test
    @Disabled
    public void testSearch_06() {
        Game game = FENDecoder.loadGame("R7/P4k2/8/8/8/8/r7/6K1 w - - 0 1");

        searchMove.setSearchParameter(SearchParameter.MAX_DEPTH, 7);
        searchResult = searchMove.search(game);
    }

    @Test
    @Disabled
    public void testSearch_07() {
        Game game = FENDecoder.loadGame("2rr2k1/2p2ppp/1p3bn1/p2P1q2/2P5/1Q4B1/PP3PPP/R2R2K1 w - - 6 22");

        searchMove.setSearchParameter(SearchParameter.MAX_DEPTH, 3);
        searchResult = searchMove.search(game);
    }

    @Test
    @Disabled
    public void testSearch_08() {
        Game game = FENDecoder.loadGame("7k/6p1/8/8/8/N7/8/K7 w - - 0 1");

        searchMove.setSearchParameter(SearchParameter.MAX_DEPTH, 9);
        searchResult = searchMove.search(game);
    }
}
