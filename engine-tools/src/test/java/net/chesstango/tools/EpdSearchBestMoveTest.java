package net.chesstango.tools;

import net.chesstango.board.representations.epd.EPD;
import net.chesstango.board.representations.epd.EPDDecoder;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.evaluation.evaluators.EvaluatorImp04;
import net.chesstango.search.Search;
import net.chesstango.search.builders.AlphaBetaBuilder;
import net.chesstango.search.smart.features.debug.DebugNodeTrap;
import net.chesstango.search.smart.features.debug.model.DebugNode;
import net.chesstango.search.smart.features.debug.traps.ComposedTrap;
import net.chesstango.search.smart.features.debug.traps.actions.PrintForUnitTest;
import net.chesstango.search.smart.features.debug.traps.predicates.NodeByZobrist;
import net.chesstango.tools.search.EpdSearch;
import net.chesstango.tools.search.EpdSearchResult;
import net.chesstango.tools.search.EpdSearchResultBuildWithBestMove;
import net.chesstango.tools.search.reports.evaluation.EvaluationReport;
import net.chesstango.tools.search.reports.nodes.NodesReport;
import net.chesstango.tools.search.reports.pv.PrincipalVariationReport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * @author Mauricio Coria
 */
public class EpdSearchBestMoveTest {
    private static final boolean PRINT_REPORT = false;
    private static EPDDecoder epdDecoder;
    private static EpdSearch epdSearch;
    private static DebugNodeTrap debugNodeTrap;
    private EpdSearchResult epdSearchResult;

    @BeforeAll
    public static void setup() {
        epdDecoder = new EPDDecoder();
        epdSearch = new EpdSearch()
                .setEpdSearchResultBuilder(new EpdSearchResultBuildWithBestMove());
    }

    @AfterEach
    public void tearDown() {
        if (PRINT_REPORT) {
            new NodesReport()
                    .withNodesVisitedStatistics()
                    //.withCutoffStatics()
                    //.withPrincipalVariation()
                    .withMoveResults(List.of(epdSearchResult.getSearchResult()))
                    .printReport(System.out);

            new EvaluationReport()
                    .withEvaluationsStatistics()
                    .withMoveResults(List.of(epdSearchResult.getSearchResult()))
                    .printReport(System.out);

            new PrincipalVariationReport()
                    .withMoveResults(List.of(epdSearchResult.getSearchResult()))
                    .printReport(System.out);
        }
    }

    @Test
    public void test_WAC003() {
        epdSearch.setDepth(5);
        EPD epd = epdDecoder.readEdpLine("5rk1/1ppb3p/p1pb4/6q1/3P1p1r/2P1R2P/PP1BQ1P1/5RKN w - - bm Rg3; id \"WAC.003\";");
        epdSearchResult = epdSearch.run(buildSearchMove(new EvaluatorImp04()), epd);
        assertTrue(epdSearchResult.isSearchSuccess());
    }

    // Evita entrar en loop
    @Test
    public void test_WAC008() {
        epdSearch.setDepth(5);
        EPD epd = epdDecoder.readEdpLine("r4q1k/p2bR1rp/2p2Q1N/5p2/5p2/2P5/PP3PPP/R5K1 w - - bm Rf7; id \"WAC.008\";");
        epdSearchResult = epdSearch.run(buildSearchMove(new EvaluatorImp04()), epd);
        assertTrue(epdSearchResult.isSearchSuccess());
    }

    @Test
    public void test_WAC012() {
        epdSearch.setDepth(5);
        EPD epd = epdDecoder.readEdpLine("4k1r1/2p3r1/1pR1p3/3pP2p/3P2qP/P4N2/1PQ4P/5R1K b - - bm Qxf3+; id \"WAC.012\";");
        epdSearchResult = epdSearch.run(buildSearchMove(new EvaluatorImp04()), epd);
        assertTrue(epdSearchResult.isSearchSuccess());
    }

    @Test
    public void test_WAC137() {
        epdSearch.setDepth(6);
        EPD epd = epdDecoder.readEdpLine("3b1rk1/1bq3pp/5pn1/1p2rN2/2p1p3/2P1B2Q/1PB2PPP/R2R2K1 w - - bm Rd7; id \"WAC.137\";");
        epdSearchResult = epdSearch.run(buildSearchMove(new EvaluatorImp04()), epd);
        assertTrue(epdSearchResult.isSearchSuccess());
    }

    @Test
    public void test_WAC072() {
        epdSearch.setDepth(1);
        EPD epd = epdDecoder.readEdpLine("r3r1k1/pp1n1ppp/2p5/4Pb2/2B2P2/B1P5/P5PP/R2R2K1 w - - bm e6; id \"WAC.072\";");
        epdSearchResult = epdSearch.run(buildSearchMove(new EvaluatorImp04()), epd);
        assertTrue(epdSearchResult.isSearchSuccess());
    }


    @Test
    public void test_WAC133() {
        epdSearch.setDepth(5);
        EPD epd = epdDecoder.readEdpLine("r1b1k2r/1pp1q2p/p1n3p1/3QPp2/8/1BP3B1/P5PP/3R1RK1 w kq - bm Bh4; id \"WAC.133\";");
        epdSearchResult = epdSearch.run(buildSearchMove(new EvaluatorImp04()), epd);
        assertTrue(epdSearchResult.isSearchSuccess());
    }


    @Test
    public void test_WAC111() {
        epdSearch.setDepth(5);
        EPD epd = epdDecoder.readEdpLine("6k1/p5p1/5p2/2P2Q2/3pN2p/3PbK1P/7P/6q1 b - - bm Qf1+; id \"WAC.111\";");
        epdSearchResult = epdSearch.run(buildSearchMove(new EvaluatorImp04()), epd);
        assertTrue(epdSearchResult.isSearchSuccess());
    }

    @Test
    public void test_WAC283() {
        epdSearch.setDepth(5);
        EPD epd = epdDecoder.readEdpLine("3q1rk1/4bp1p/1n2P2Q/3p1p2/6r1/Pp2R2N/1B4PP/7K w - - bm Ng5; id \"WAC.283\";");
        epdSearchResult = epdSearch.run(buildSearchMove(new EvaluatorImp04()), epd);
        assertTrue(epdSearchResult.isSearchSuccess());
    }


    @Test
    public void test_BK01() {
        epdSearch.setDepth(5);
        EPD epd = epdDecoder.readEdpLine("1k1r4/pp1b1R2/3q2pp/4p3/2B5/4Q3/PPP2B2/2K5 b - - bm Qd1+; id \"BK.01\";");
        epdSearchResult = epdSearch.run(buildSearchMove(new EvaluatorImp04()), epd);
        assertTrue(epdSearchResult.isSearchSuccess());
    }

    @Test
    public void test_BK04() {
        epdSearch.setDepth(5);
        EPD epd = epdDecoder.readEdpLine("rnbqkb1r/p3pppp/1p6/2ppP3/3N4/2P5/PPP1QPPP/R1B1KB1R w KQkq - bm e6; id \"BK.04\";");
        epdSearchResult = epdSearch.run(buildSearchMove(new EvaluatorImp04()), epd);
        assertTrue(epdSearchResult.isSearchSuccess());
    }

    @Test
    public void test_BK05() {
        epdSearch.setDepth(2);
        EPD epd = epdDecoder.readEdpLine("r1b2rk1/2q1b1pp/p2ppn2/1p6/3QP3/1BN1B3/PPP3PP/R4RK1 w - - bm Nd5 a4; id \"BK.05\";");
        epdSearchResult = epdSearch.run(buildSearchMove(new EvaluatorImp04()), epd);
        assertTrue(epdSearchResult.isSearchSuccess());
    }

    @Test
    @Disabled
    public void test_BK06() {
        epdSearch.setDepth(3);
        EPD epd = epdDecoder.readEdpLine("2r3k1/pppR1pp1/4p3/4P1P1/5P2/1P4K1/P1P5/8 w - - bm g6; id \"BK.06\";");
        epdSearchResult = epdSearch.run(buildSearchMove(new EvaluatorImp04()), epd);
        assertTrue(epdSearchResult.isSearchSuccess());
    }

    @Test
    @Disabled
    public void test_BK23() {
        epdSearch.setDepth(3);
        EPD epd = epdDecoder.readEdpLine("r1bqk2r/pp2bppp/2p5/3pP3/P2Q1P2/2N1B3/1PP3PP/R4RK1 b kq - bm f6; id \"BK.23\";");
        epdSearchResult = epdSearch.run(buildSearchMove(new EvaluatorImp04()), epd);
        assertTrue(epdSearchResult.isSearchSuccess());
    }

    @Test
    public void test_BK24() {
        epdSearch.setDepth(6);
        EPD epd = epdDecoder.readEdpLine("r2qnrnk/p2b2b1/1p1p2pp/2pPpp2/1PP1P3/PRNBB3/3QNPPP/5RK1 w - - bm f4; id \"BK.24\";");
        epdSearchResult = epdSearch.run(buildSearchMove(new EvaluatorImp04()), epd);
        assertTrue(epdSearchResult.isSearchSuccess());
    }

    @Test
    public void test_40H_10390() {
        epdSearch.setDepth(1);
        EPD epd = epdDecoder.readEdpLine("1Q6/6kp/6p1/8/1P2q3/5R1P/5PK1/4r3 w - - bm Qb8-f8+; ce +M1; pv Qb8-f8+; id \"10390\";");
        epdSearchResult = epdSearch.run(buildSearchMove(new EvaluatorImp04()), epd);
        assertTrue(epdSearchResult.isSearchSuccess());
    }

    @Test
    public void test_40H_10390_mirror() {
        epdSearch.setDepth(1);
        EPD epd = epdDecoder.readEdpLine("4R3/5pk1/5r1p/1p2Q3/8/6P1/6KP/1q6 b - - bm Qb1-f1+; id \"10390\";");
        epdSearchResult = epdSearch.run(buildSearchMove(new EvaluatorImp04()), epd);
        assertTrue(epdSearchResult.isSearchSuccess());
    }

    @Test
    public void test_40H_9482() {
        epdSearch.setDepth(30);//El JM se encuentra a un ply
        EPD epd = epdDecoder.readEdpLine("1Q4rr/1p1bkp2/pP6/2p1pP2/3nP1Bp/2P1q2P/7K/3R1R2 w - - bm f5-f6+; ce +M1; pv f5-f6+; id \"9482\";");
        epdSearchResult = epdSearch.run(buildSearchMove(new EvaluatorImp04()), epd);
        assertTrue(epdSearchResult.isSearchSuccess());
    }

    @Test
    public void test_40H_079() {
        epdSearch.setDepth(3);
        EPD epd = epdDecoder.readEdpLine("1R1nk2r/4q1p1/5pP1/3QpP1p/P6P/5P2/5BK1/2r5 w k - bm Rb8xd8+; ce +M2; pv Rb8xd8+ Qe7xd8 Qd5-f7+; id \"079\";");
        epdSearchResult = epdSearch.run(buildSearchMove(new EvaluatorImp04()), epd);
        assertTrue(epdSearchResult.isSearchSuccess());
    }

    @Test
    public void test_40H_2857() {
        epdSearch.setDepth(5);
        EPD epd = epdDecoder.readEdpLine("1R3b1k/2p3pp/4qr2/Q7/3p2P1/3P3K/6NP/8 b - - bm Rf6-f3+; ce -M3; pv Rf6-f3+ Kh3-h4 Qe6-h6+ Qa5-h5 g7-g5+; id \"2857\";");
        epdSearchResult = epdSearch.run(buildSearchMove(new EvaluatorImp04()), epd);
        assertTrue(epdSearchResult.isSearchSuccess());
    }

    @Test
    public void test_40H_5712() {
        epdSearch.setDepth(9);
        EPD epd = epdDecoder.readEdpLine("1B1bk1nr/5pp1/rNR5/p3Pp1p/1p2pP2/1P5P/1PP3P1/2KR4 w k - bm Rd1xd8+; ce +M5; pv Rd1xd8+ Ke8xd8 Rc6-c8+ Kd8-e7 Bb8-d6+ Ke7-e6 Rc8-e8+ Ng8-e7 Re8xe7+; id \"5712\";");
        epdSearchResult = epdSearch.run(buildSearchMove(new EvaluatorImp04()), epd);
        assertTrue(epdSearchResult.isSearchSuccess());
    }

    @Test
    public void test_40H_2462() {
        epdSearch.setDepth(5);
        EPD epd = epdDecoder.readEdpLine("1k2r3/1pp5/4B3/1P3Q2/3q1Pp1/3n2Pp/3p3P/5R1K b - - bm Qd4-f2; ce -M3; pv Qd4-f2 Rf1-g1 Qf2-f3+ Rg1-g2 d2-d1Q+; id \"2462\";");
        epdSearchResult = epdSearch.run(buildSearchMove(new EvaluatorImp04()), epd);
        assertTrue(epdSearchResult.isSearchSuccess());
    }

    @Test
    public void test_40H_2462_mirror() {
        epdSearch.setDepth(5);
        EPD epd = epdDecoder.readEdpLine("5r1k/3P3p/3N2pP/3Q1pP1/1p3q2/4b3/1PP5/1K2R3 w - - bm Qd5-f7; id \"2462\";");
        epdSearchResult = epdSearch.run(buildSearchMove(new EvaluatorImp04()), epd);
        assertTrue(epdSearchResult.isSearchSuccess());
    }

    @Test
    public void test_40H_1445() {
        epdSearch.setDepth(5);
        EPD epd = epdDecoder.readEdpLine("2r3k1/p4p2/3Rp2p/1p2P1pK/8/1P4P1/P3Q2P/1q6 b - - bm Qb1-g6+; ce -M3; pv Qb1-g6+ Kh5-g4 Qg6-f5+ Kg4-h5 Qf5-h3+; id \"1445\";");
        epdSearchResult = epdSearch.run(buildSearchMove(new EvaluatorImp04()), epd);
        assertTrue(epdSearchResult.isSearchSuccess());
    }

    @Test
    public void test_40H_10021() {
        epdSearch.setDepth(5);
        EPD epd = epdDecoder.readEdpLine("3k4/p2r4/1pR4p/4Q3/8/5P2/q5P1/6K1 w - - bm Qe5-f6+; ce +M3; pv Qe5-f6+ Rd7-e7 Qf6-f8+ Re7-e8 Qf8-d6+; id \"10021\";");
        epdSearchResult = epdSearch.run(buildSearchMove(new EvaluatorImp04()), epd);
        assertTrue(epdSearchResult.isSearchSuccess());
    }

    @Test
    public void test_40H_022() {
        epdSearch.setDepth(1);
        EPD epd = epdDecoder.readEdpLine("6kr/1p3ppp/p1q2b2/2Bp1N2/3P4/6Q1/PP3PPP/4R1K1 w - - bm Nf5-h6+; ce +M1; pv Nf5-h6+; id \"022\";");
        epdSearchResult = epdSearch.run(buildSearchMove(new EvaluatorImp04()), epd);
        assertTrue(epdSearchResult.isSearchSuccess());
    }

    @Test
    public void test_40H_10255() {
        epdSearch.setDepth(6);
        EPD epd = epdDecoder.readEdpLine("5r2/ppbqn2k/7B/2p1p2p/P1NpP1P1/3P4/1PP2r2/R1Q1K1R1 b Q - bm Qd7xg4; ce -M3; pv Qd7xg4 Rg1xg4 Rf2-f1+ Ke1-e2 Rf8-f2+; id \"10255\";");
        epdSearchResult = epdSearch.run(buildSearchMove(new EvaluatorImp04()), epd);
        assertTrue(epdSearchResult.isSearchSuccess());
    }

    @Test
    public void test_40H_1027() {
        epdSearch.setDepth(5);
        EPD epd = epdDecoder.readEdpLine("kbK5/pp6/1P6/8/8/8/R7/8 w - - bm Ra2-a6; ce +M2; pv Ra2-a6 b7xa6 b6-b7+; id \"1027\";");
        epdSearchResult = epdSearch.run(buildSearchMove(new EvaluatorImp04()), epd);
        assertTrue(epdSearchResult.isSearchSuccess());
    }

    @Test
    public void test_40H_3243() {
        epdSearch.setDepth(5);
        EPD epd = epdDecoder.readEdpLine("6k1/p1pp2pp/2p5/8/3P3q/3bP3/PP3rPP/R1K1Q2R b - - bm Rf2-c2+; ce -M3; pv Rf2-c2+ Kc1-d1 Qh4-g4+ Qe1-e2 Qg4xe2+; id \"3243\";");
        epdSearchResult = epdSearch.run(buildSearchMove(new EvaluatorImp04()), epd);
        assertTrue(epdSearchResult.isSearchSuccess());
    }


    @Test
    public void test_sbd_058() {
        epdSearch.setDepth(5);
        EPD epd = epdDecoder.readEdpLine("r1bqk2r/2ppbppp/p1n2n2/1p2p3/4P3/1B3N2/PPPPQPPP/RNB2RK1 b kq - bm O-O; id \"sbd.058\";");
        epdSearchResult = epdSearch.run(buildSearchMove(new EvaluatorImp04()), epd);
        assertTrue(epdSearchResult.isSearchSuccess());
    }

    private void trapNodeByZobristAndPrintForUT() {
        NodeByZobrist nodeByZobrist = new NodeByZobrist()
                .setZobristHash(0x0CE7DD3862149D3EL)
                .setTopology(DebugNode.NodeTopology.INTERIOR)
                .setAlpha(-61290)
                .setBeta(-59722);
        debugNodeTrap = new ComposedTrap(nodeByZobrist, new PrintForUnitTest());
    }


    private static Search buildSearchMove(Evaluator evaluator) {
        AlphaBetaBuilder builder = new AlphaBetaBuilder()
                .withGameEvaluator(evaluator)
                .withGameEvaluatorCache()

                //.withExtensionCheckResolver()
                .withQuiescence()

                //.withTriangularPV()
                .withTranspositionTable()

                .withTranspositionMoveSorter()
                .withKillerMoveSorter()
                .withRecaptureSorter()
                .withMvvLvaSorter()

                .withAspirationWindows()
                .withIterativeDeepening()

                //.withStopProcessingCatch()
                //.withPrintChain()
                //.withZobristTracker()
                //.withTrackEvaluations() // Consume demasiada memoria
                //.withDebugSearchTree(debugNodeTrap, false, true, true)
                ;

        if (PRINT_REPORT) {
            builder.withStatistics();
            //.withTrackEvaluations();
        }

        return builder.build();
    }
}
