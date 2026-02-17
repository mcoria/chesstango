package net.chesstango.reports;

import net.chesstango.board.Game;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.reports.tree.nodes.NodesReport;
import net.chesstango.reports.tree.summary.SummaryReport;
import net.chesstango.reports.tree.transposition.TranspositionReport;
import net.chesstango.search.Search;
import net.chesstango.search.SearchResult;
import net.chesstango.search.builders.AlphaBetaBuilder;
import net.chesstango.search.visitors.SetMaxDepthVisitor;
import org.junit.jupiter.api.*;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public class SearchesTest {
    private static final boolean PRINT_REPORT = true;
    private Search search;
    private SearchResult searchResult;

    @BeforeEach
    public void setup() {
        searchResult = null;

        search = new AlphaBetaBuilder()
                .withGameEvaluator(Evaluator.createInstance())
                .withGameEvaluatorCache()

                .withQuiescence()

                .withTranspositionTable()

                .withTranspositionMoveSorter()
                .withKillerMoveSorter()
                .withRecaptureSorter()
                .withMvvLvaSorter()

                .withAspirationWindows()
                .withIterativeDeepening()

                .withStatistics()
                //.withZobristTracker()
                //.withTrackEvaluations() // Consume demasiada memoria
                //.withMoveEvaluation()

                //.withPrintChain()
                //.withDebugSearchTree(null)

                //.withStopProcessingCatch()

                .build();
    }

    @AfterEach
    public void printReport(TestInfo testInfo) {
        if (PRINT_REPORT) {
            /*
            new SummaryReport()
                    .addSearchesByTreeSummaryModel(testInfo.getDisplayName(), List.of(searchResult))
                    .withNodesVisitedStatistics()
                    .withTranspositionStatistics()
                    //.withCutoffStatistics()
                    .printReport(System.out);
            */


            new NodesReport()
                    .setReportTitle(testInfo.getDisplayName())
                    .withMoveResults(List.of(searchResult))
                    .withNodesVisitedStatistics()
                    //.withCutoffStatistics()
                    .printReport(System.out);

            /*
            new EvaluationReport()
            .setReportTitle(testInfo.getDisplayName())
                    .withMoveResults(List.of(searchResult))
                    //.withExportEvaluations()
                    .withEvaluationsStatistics()
                    .printReport(System.out);

            new PrincipalVariationReport()
            .setReportTitle(testInfo.getDisplayName())
                    .withMoveResults(List.of(searchResult))
                    .printReport(System.out);
             */

            new TranspositionReport()
                    .setReportTitle(testInfo.getDisplayName())
                    .withMoveResults(List.of(searchResult))
                    .printReport(System.out);

        }
    }

    @Test
    @Disabled
    public void testSearch_00() {
        Game game = Game.from(FEN.START_POSITION);

        search.accept(new SetMaxDepthVisitor(6));
        searchResult = search.startSearch(game);
    }


    @Test
    @Disabled
    public void testSearch_01() {
        Game game = Game.from(FEN.of("r4rk1/p1qbp1b1/2p3pp/2Pn1p2/1pQ5/5B2/PPP1NPPP/R1B2RK1 w - - 1 22"));

        search.accept(new SetMaxDepthVisitor(6));
        searchResult = search.startSearch(game);
    }

    @Test
    @Disabled
    public void testSearch_02() {
        Game game = Game.from(FEN.of("1k2r3/1pp5/4B3/1P3Q2/3q1Pp1/3n2Pp/3p3P/5R1K b - - 0 1"));

        search.accept(new SetMaxDepthVisitor(5));
        searchResult = search.startSearch(game);
    }


    @Test
    @Disabled
    public void testSearch_03() {
        Game game = Game.from(FEN.of("8/p7/2R5/4k3/8/Pp1b3P/1r3PP1/6K1 w - - 2 43"));

        search.accept(new SetMaxDepthVisitor(7));
        searchResult = search.startSearch(game);
    }


    @Test
    @Disabled
    public void testSearch_04() {
        Game game = Game.from(FEN.of("4R3/6pk/1p4Bp/5p2/p5P1/2BP3P/5P2/6K1 b - - 0 39"));

        search.accept(new SetMaxDepthVisitor(7));
        searchResult = search.startSearch(game);
    }


    @Test
    @Disabled
    public void testSearch_06() {
        Game game = Game.from(FEN.of("R7/P4k2/8/8/8/8/r7/6K1 w - - 0 1"));

        search.accept(new SetMaxDepthVisitor(7));
        searchResult = search.startSearch(game);
    }

    @Test
    @Disabled
    public void testSearch_07() {
        Game game = Game.from(FEN.of("2rr2k1/2p2ppp/1p3bn1/p2P1q2/2P5/1Q4B1/PP3PPP/R2R2K1 w - - 6 22"));

        search.accept(new SetMaxDepthVisitor(3));
        searchResult = search.startSearch(game);
    }

    @Test
    @Disabled
    public void testSearch_08() {
        Game game = Game.from(FEN.of("7k/6p1/8/8/8/N7/8/K7 w - - 0 1"));

        search.accept(new SetMaxDepthVisitor(9));
        searchResult = search.startSearch(game);
    }


    @Test
    @Disabled
    public void testSearch_09() {
        Game game = Game.from(FEN.of("rnb2rk1/pp3ppp/4pn2/2q5/1Q2P3/P1P2P2/3B2PP/R3KBNR b KQ - 4 12"));

        search.accept(new SetMaxDepthVisitor(7));
        searchResult = search.startSearch(game);
    }

    @Test
    @Disabled
    public void testSearch_11() {
        Game game = Game.from(FEN.of("1r2r1k1/pp3p1p/3pb1pB/4b3/P2pQ3/1PqP2P1/2P2RBP/3R2K1 b - - 2 23"));

        search.accept(new SetMaxDepthVisitor(4));
        searchResult = search.startSearch(game);
    }


    @Test
    @Disabled
    public void testSearch_12() {
        Game game = Game.from(FEN.of("1RRbr3/3pkp2/2b1p1p1/2P1P3/5PP1/P6P/1KP5/5B2 w - - 17 49"));

        search.accept(new SetMaxDepthVisitor(7));
        searchResult = search.startSearch(game);
    }

}
