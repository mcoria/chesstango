package net.chesstango.search;

import net.chesstango.board.representations.EPDEntry;
import net.chesstango.board.representations.EPDReader;
import net.chesstango.evaluation.DefaultEvaluator;
import net.chesstango.search.builders.AlphaBetaBuilder;
import net.chesstango.search.reports.EvaluationReport;
import net.chesstango.search.reports.NodesReport;
import net.chesstango.search.reports.PrincipalVariationReport;
import net.chesstango.search.smart.statistics.NodeStatistics;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * @author Mauricio Coria
 */
public class EpdSearchMainTest {
    private static final boolean PRINT_REPORT = false;
    private EPDReader epdReader;
    private EpdSearch epdSearch;
    private EpdSearchResult epdSearchResult;

    @BeforeEach
    public void setup() {
        epdReader = new EPDReader();
        epdSearch = new EpdSearch();
        epdSearch.setSearchMove(buildSearchMove());
    }

    @AfterEach
    public void tearDown() {
        if (PRINT_REPORT) {
            System.out.println("Time taken: " + epdSearchResult.searchDuration() + " ms");
            new NodesReport()
                    .withNodesVisitedStatistics()
                    //.withCutoffStatics()
                    //.withPrincipalVariation()
                    .withMoveResults(List.of(epdSearchResult.searchResult()))
                    .printReport(System.out);

            new EvaluationReport()
                    .withEvaluationsStatistics()
                    .withMoveResults(List.of(epdSearchResult.searchResult()))
                    .printReport(System.out);

            new PrincipalVariationReport()
                    .withMoveResults(List.of(epdSearchResult.searchResult()))
                    .printReport(System.out);
        }
    }

    @Test
    public void test_WAC003() {
        epdSearch.setDepth(5);
        EPDEntry epdEntry = epdReader.readEdpLine("5rk1/1ppb3p/p1pb4/6q1/3P1p1r/2P1R2P/PP1BQ1P1/5RKN w - - bm Rg3; id \"WAC.003\";");
        epdSearchResult = epdSearch.run(epdEntry);
        assertTrue(epdSearchResult.bestMoveFound());
    }

    @Test
    public void test_WAC012() {
        epdSearch.setDepth(5);
        EPDEntry epdEntry = epdReader.readEdpLine("4k1r1/2p3r1/1pR1p3/3pP2p/3P2qP/P4N2/1PQ4P/5R1K b - - bm Qxf3+; id \"WAC.012\";");
        epdSearchResult = epdSearch.run(epdEntry);
        assertTrue(epdSearchResult.bestMoveFound());
    }

    @Test
    public void test_WAC072() {
        epdSearch.setDepth(1);
        EPDEntry epdEntry = epdReader.readEdpLine("r3r1k1/pp1n1ppp/2p5/4Pb2/2B2P2/B1P5/P5PP/R2R2K1 w - - bm e6; id \"WAC.072\";");
        epdSearchResult = epdSearch.run(epdEntry);
        assertTrue(epdSearchResult.bestMoveFound());
    }


    @Test
    public void test_WAC133() {
        epdSearch.setDepth(5);
        EPDEntry epdEntry = epdReader.readEdpLine("r1b1k2r/1pp1q2p/p1n3p1/3QPp2/8/1BP3B1/P5PP/3R1RK1 w kq - bm Bh4; id \"WAC.133\";");
        epdSearchResult = epdSearch.run(epdEntry);
        assertTrue(epdSearchResult.bestMoveFound());
    }

    @Test
    public void test_BK01() {
        epdSearch.setDepth(5);
        EPDEntry epdEntry = epdReader.readEdpLine("1k1r4/pp1b1R2/3q2pp/4p3/2B5/4Q3/PPP2B2/2K5 b - - bm Qd1+; id \"BK.01\";");
        epdSearchResult = epdSearch.run(epdEntry);
        assertTrue(epdSearchResult.bestMoveFound());
    }

    @Test
    public void test_BK05() {
        epdSearch.setDepth(2);
        EPDEntry epdEntry = epdReader.readEdpLine("r1b2rk1/2q1b1pp/p2ppn2/1p6/3QP3/1BN1B3/PPP3PP/R4RK1 w - - bm Nd5 a4; id \"BK.05\";");
        epdSearchResult = epdSearch.run(epdEntry);
        assertTrue(epdSearchResult.bestMoveFound());
    }

    @Test
    @Disabled
    public void test_BK06() {
        epdSearch.setDepth(3);
        EPDEntry epdEntry = epdReader.readEdpLine("2r3k1/pppR1pp1/4p3/4P1P1/5P2/1P4K1/P1P5/8 w - - bm g6; id \"BK.06\";");
        epdSearchResult = epdSearch.run(epdEntry);
        assertTrue(epdSearchResult.bestMoveFound());
    }

    @Test
    @Disabled
    public void test_BK23() {
        epdSearch.setDepth(3);
        EPDEntry epdEntry = epdReader.readEdpLine("r1bqk2r/pp2bppp/2p5/3pP3/P2Q1P2/2N1B3/1PP3PP/R4RK1 b kq - bm f6; id \"BK.23\";");
        epdSearchResult = epdSearch.run(epdEntry);
        assertTrue(epdSearchResult.bestMoveFound());
    }

    @Test
    public void test_BK24() {
        epdSearch.setDepth(6);
        EPDEntry epdEntry = epdReader.readEdpLine("r2qnrnk/p2b2b1/1p1p2pp/2pPpp2/1PP1P3/PRNBB3/3QNPPP/5RK1 w - - bm f4; id \"BK.24\";");
        epdSearchResult = epdSearch.run(epdEntry);
        assertTrue(epdSearchResult.bestMoveFound());
    }

    @Test
    public void test_40H_10390() {
        epdSearch.setDepth(1);
        EPDEntry epdEntry = epdReader.readEdpLine("1Q6/6kp/6p1/8/1P2q3/5R1P/5PK1/4r3 w - - bm Qb8-f8+; ce +M1; pv Qb8-f8+; id \"10390\";");
        epdSearchResult = epdSearch.run(epdEntry);
        assertTrue(epdSearchResult.bestMoveFound());
    }

    @Test
    public void test_40H_10390_mirror() {
        epdSearch.setDepth(1);
        EPDEntry epdEntry = epdReader.readEdpLine("4R3/5pk1/5r1p/1p2Q3/8/6P1/6KP/1q6 b - - bm Qb1-f1+; id \"10390\";");
        epdSearchResult = epdSearch.run(epdEntry);
        assertTrue(epdSearchResult.bestMoveFound());
    }

    @Test
    public void test_40H_9482() {
        epdSearch.setDepth(30);//El JM se encuentra a un ply
        EPDEntry epdEntry = epdReader.readEdpLine("1Q4rr/1p1bkp2/pP6/2p1pP2/3nP1Bp/2P1q2P/7K/3R1R2 w - - bm f5-f6+; ce +M1; pv f5-f6+; id \"9482\";");
        epdSearchResult = epdSearch.run(epdEntry);
        assertTrue(epdSearchResult.bestMoveFound());
    }

    @Test
    public void test_40H_079() {
        epdSearch.setDepth(3);
        EPDEntry epdEntry = epdReader.readEdpLine("1R1nk2r/4q1p1/5pP1/3QpP1p/P6P/5P2/5BK1/2r5 w k - bm Rb8xd8+; ce +M2; pv Rb8xd8+ Qe7xd8 Qd5-f7+; id \"079\";");
        epdSearchResult = epdSearch.run(epdEntry);
        assertTrue(epdSearchResult.bestMoveFound());
    }

    @Test
    public void test_40H_2857() {
        epdSearch.setDepth(5);
        EPDEntry epdEntry = epdReader.readEdpLine("1R3b1k/2p3pp/4qr2/Q7/3p2P1/3P3K/6NP/8 b - - bm Rf6-f3+; ce -M3; pv Rf6-f3+ Kh3-h4 Qe6-h6+ Qa5-h5 g7-g5+; id \"2857\";");
        epdSearchResult = epdSearch.run(epdEntry);
        assertTrue(epdSearchResult.bestMoveFound());
    }

    @Test
    public void test_40H_5712() {
        epdSearch.setDepth(9);
        EPDEntry epdEntry = epdReader.readEdpLine("1B1bk1nr/5pp1/rNR5/p3Pp1p/1p2pP2/1P5P/1PP3P1/2KR4 w k - bm Rd1xd8+; ce +M5; pv Rd1xd8+ Ke8xd8 Rc6-c8+ Kd8-e7 Bb8-d6+ Ke7-e6 Rc8-e8+ Ng8-e7 Re8xe7+; id \"5712\";");
        epdSearchResult = epdSearch.run(epdEntry);
        assertTrue(epdSearchResult.bestMoveFound());
    }

    @Test
    public void test_40H_2462() {
        epdSearch.setDepth(5);
        EPDEntry epdEntry = epdReader.readEdpLine("1k2r3/1pp5/4B3/1P3Q2/3q1Pp1/3n2Pp/3p3P/5R1K b - - bm Qd4-f2; ce -M3; pv Qd4-f2 Rf1-g1 Qf2-f3+ Rg1-g2 d2-d1Q+; id \"2462\";");
        epdSearchResult = epdSearch.run(epdEntry);
        assertTrue(epdSearchResult.bestMoveFound());
    }

    @Test
    public void test_40H_2462_mirror() {
        epdSearch.setDepth(5);
        EPDEntry epdEntry = epdReader.readEdpLine("5r1k/3P3p/3N2pP/3Q1pP1/1p3q2/4b3/1PP5/1K2R3 w - - bm Qd5-f7; id \"2462\";");
        epdSearchResult = epdSearch.run(epdEntry);
        assertTrue(epdSearchResult.bestMoveFound());
    }

    @Test
    public void test_40H_1445() {
        epdSearch.setDepth(5);
        EPDEntry epdEntry = epdReader.readEdpLine("2r3k1/p4p2/3Rp2p/1p2P1pK/8/1P4P1/P3Q2P/1q6 b - - bm Qb1-g6+; ce -M3; pv Qb1-g6+ Kh5-g4 Qg6-f5+ Kg4-h5 Qf5-h3+; id \"1445\";");
        epdSearchResult = epdSearch.run(epdEntry);
        assertTrue(epdSearchResult.bestMoveFound());
    }

    @Test
    public void test_40H_10021() {
        epdSearch.setDepth(5);
        EPDEntry epdEntry = epdReader.readEdpLine("3k4/p2r4/1pR4p/4Q3/8/5P2/q5P1/6K1 w - - bm Qe5-f6+; ce +M3; pv Qe5-f6+ Rd7-e7 Qf6-f8+ Re7-e8 Qf8-d6+; id \"10021\";");
        epdSearchResult = epdSearch.run(epdEntry);
        assertTrue(epdSearchResult.bestMoveFound());
    }

    @Test
    public void test_40H_022() {
        epdSearch.setDepth(1);
        EPDEntry epdEntry = epdReader.readEdpLine("6kr/1p3ppp/p1q2b2/2Bp1N2/3P4/6Q1/PP3PPP/4R1K1 w - - bm Nf5-h6+; ce +M1; pv Nf5-h6+; id \"022\";");
        epdSearchResult = epdSearch.run(epdEntry);
        assertTrue(epdSearchResult.bestMoveFound());
    }

    @Test
    public void test_40H_10255() {
        epdSearch.setDepth(6);
        EPDEntry epdEntry = epdReader.readEdpLine("5r2/ppbqn2k/7B/2p1p2p/P1NpP1P1/3P4/1PP2r2/R1Q1K1R1 b Q - bm Qd7xg4; ce -M3; pv Qd7xg4 Rg1xg4 Rf2-f1+ Ke1-e2 Rf8-f2+; id \"10255\";");
        epdSearchResult = epdSearch.run(epdEntry);
        assertTrue(epdSearchResult.bestMoveFound());
    }


    @Test
    public void test_40H_2820() {
        epdSearch.setDepth(5);
        EPDEntry epdEntry = epdReader.readEdpLine("8/2p5/2P5/p7/k1B5/2K5/2N1p3/8 w - - bm Nc2-e1; ce +M3; pv Nc2-e1 Ka4-a3 Bc4-b3 a5-a4 Ne1-c2+; id \"2820\";");
        epdSearchResult = epdSearch.run(epdEntry);
        assertTrue(epdSearchResult.bestMoveFound());


        /**
         * Ahora se prueba el inverso
         */
        EPDEntry epdEntry1 = epdReader.readEdpLine("8/2n1P3/2k5/K1b5/P7/2p5/2P5/8 b - - bm Nc7-e8; ce -M3; pv Nc7-e8 Ka5-a6 Bc5-b6 a4-a5 Ne8-c7+; id \"2820\";");
        EpdSearchResult epdSearchResult1 = epdSearch.run(epdEntry1);
        assertTrue(epdSearchResult1.bestMoveFound());


        SearchMoveResult searchResult = epdSearchResult.searchResult();
        SearchMoveResult searchResult1 = epdSearchResult1.searchResult();

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

        /**
         * Esta fallando esta linea, podriamos hacer un dump de la ejecucion de los movimientos
         */
        assertEquals(searchResult.getExecutedMoves(), searchResult1.getExecutedMoves());
    }


    private SearchMove buildSearchMove() {
        return new AlphaBetaBuilder()
                .withGameEvaluator(new DefaultEvaluator())
                //.withGameEvaluatorCache()

                .withQuiescence()

                //.withTranspositionTable()
                //.withQTranspositionTable()

                //.withTranspositionMoveSorter()
                //.withQTranspositionMoveSorter()

                .withIterativeDeepening()
                //.withAspirationWindows()
                //.withTriangularPV()

                //.withStatistics()
                .withPrintChain()
                //.withZobristTracker()
                //.withTrackEvaluations() // Consume demasiada memoria
                .withDebugSearchTree()

                .build();
    }
}
