package net.chesstango.search;

import net.chesstango.board.representations.EDPReader;
import net.chesstango.search.reports.SearchesReport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * @author Mauricio Coria
 */
public class BestMoveSearchSuiteTest {
    private EDPReader edpReader;

    private BestMoveSearchSuite finderSuite;

    private Instant start;

    @BeforeEach
    public void setup() {
        edpReader = new EDPReader();
        start = Instant.now();
    }

    @AfterEach
    public void tearDown(){
        Duration timeElapsed = Duration.between(start, Instant.now());
        System.out.println("Time taken: "+ timeElapsed.toMillis() +" ms");
        new SearchesReport()
                .withNodesVisitedStatics()
                .withCutoffStatics()
                .withPrincipalVariation()
                .printSearchesStatics(finderSuite.searchMoveResults);
    }

    @Test
    public void test_WAC003() {
        finderSuite = new BestMoveSearchSuite(4);
        EDPReader.EDPEntry edpEntry = edpReader.readEdpLine("5rk1/1ppb3p/p1pb4/6q1/3P1p1r/2P1R2P/PP1BQ1P1/5RKN w - - bm Rg3; id \"WAC.003\";");
        assertTrue(finderSuite.run(edpEntry));
    }

    @Test
    public void test_WAC072() {
        finderSuite = new BestMoveSearchSuite(1);
        EDPReader.EDPEntry edpEntry = edpReader.readEdpLine("r3r1k1/pp1n1ppp/2p5/4Pb2/2B2P2/B1P5/P5PP/R2R2K1 w - - bm e6; id \"WAC.072\";");
        assertTrue(finderSuite.run(edpEntry));
    }

    @Test
    public void test_BK01() {
        finderSuite = new BestMoveSearchSuite(6);
        EDPReader.EDPEntry edpEntry = edpReader.readEdpLine("1k1r4/pp1b1R2/3q2pp/4p3/2B5/4Q3/PPP2B2/2K5 b - - bm Qd1+; id \"BK.01\";");
        assertTrue(finderSuite.run(edpEntry));
    }

    @Test
    public void test_BK05() {
        finderSuite = new BestMoveSearchSuite(2);
        EDPReader.EDPEntry edpEntry = edpReader.readEdpLine("r1b2rk1/2q1b1pp/p2ppn2/1p6/3QP3/1BN1B3/PPP3PP/R4RK1 w - - bm Nd5 a4; id \"BK.05\";");
        assertTrue(finderSuite.run(edpEntry));
    }

    @Test
    @Disabled
    public void test_BK06() {
        finderSuite = new BestMoveSearchSuite(3);
        EDPReader.EDPEntry edpEntry = edpReader.readEdpLine("2r3k1/pppR1pp1/4p3/4P1P1/5P2/1P4K1/P1P5/8 w - - bm g6; id \"BK.06\";");
        assertTrue(finderSuite.run(edpEntry));
    }

    @Test
    public void test_40H_10390() {
        finderSuite = new BestMoveSearchSuite(1);
        EDPReader.EDPEntry edpEntry = edpReader.readEdpLine("1Q6/6kp/6p1/8/1P2q3/5R1P/5PK1/4r3 w - - bm Qb8-f8+; ce +M1; pv Qb8-f8+; id \"10390\";");
        assertTrue(finderSuite.run(edpEntry));
    }

    @Test
    public void test_40H_10390_mirror() {
        finderSuite = new BestMoveSearchSuite(1);
        EDPReader.EDPEntry edpEntry = edpReader.readEdpLine("4R3/5pk1/5r1p/1p2Q3/8/6P1/6KP/1q6 b - - bm Qb1-f1+; id \"10390\";");
        assertTrue(finderSuite.run(edpEntry));
    }

    @Test
    public void test_40H_9482() {
        finderSuite = new BestMoveSearchSuite(30); //El JM se encuentra a un ply
        EDPReader.EDPEntry edpEntry = edpReader.readEdpLine("1Q4rr/1p1bkp2/pP6/2p1pP2/3nP1Bp/2P1q2P/7K/3R1R2 w - - bm f5-f6+; ce +M1; pv f5-f6+; id \"9482\";");
        assertTrue(finderSuite.run(edpEntry));
    }

    @Test
    public void test_40H_079() {
        finderSuite = new BestMoveSearchSuite(3);
        EDPReader.EDPEntry edpEntry = edpReader.readEdpLine("1R1nk2r/4q1p1/5pP1/3QpP1p/P6P/5P2/5BK1/2r5 w k - bm Rb8xd8+; ce +M2; pv Rb8xd8+ Qe7xd8 Qd5-f7+; id \"079\";");
        assertTrue(finderSuite.run(edpEntry));
    }

    @Test
    public void test_40H_2857() {
        finderSuite = new BestMoveSearchSuite(5);
        EDPReader.EDPEntry edpEntry = edpReader.readEdpLine("1R3b1k/2p3pp/4qr2/Q7/3p2P1/3P3K/6NP/8 b - - bm Rf6-f3+; ce -M3; pv Rf6-f3+ Kh3-h4 Qe6-h6+ Qa5-h5 g7-g5+; id \"2857\";");
        assertTrue(finderSuite.run(edpEntry));
    }

    @Test
    public void test_40H_5712() {
        finderSuite = new BestMoveSearchSuite(9);
        EDPReader.EDPEntry edpEntry = edpReader.readEdpLine("1B1bk1nr/5pp1/rNR5/p3Pp1p/1p2pP2/1P5P/1PP3P1/2KR4 w k - bm Rd1xd8+; ce +M5; pv Rd1xd8+ Ke8xd8 Rc6-c8+ Kd8-e7 Bb8-d6+ Ke7-e6 Rc8-e8+ Ng8-e7 Re8xe7+; id \"5712\";");
        assertTrue(finderSuite.run(edpEntry));
    }

    @Test
    public void test_40H_2462() {
        finderSuite = new BestMoveSearchSuite(5);
        EDPReader.EDPEntry edpEntry = edpReader.readEdpLine("1k2r3/1pp5/4B3/1P3Q2/3q1Pp1/3n2Pp/3p3P/5R1K b - - bm Qd4-f2; ce -M3; pv Qd4-f2 Rf1-g1 Qf2-f3+ Rg1-g2 d2-d1Q+; id \"2462\";");
        assertTrue(finderSuite.run(edpEntry));
    }

    @Test
    public void test_40H_2462_mirror() {
        finderSuite = new BestMoveSearchSuite(5);
        EDPReader.EDPEntry edpEntry = edpReader.readEdpLine("5r1k/3P3p/3N2pP/3Q1pP1/1p3q2/4b3/1PP5/1K2R3 w - - bm Qd5-f7; id \"2462\";");
        assertTrue(finderSuite.run(edpEntry));
    }

    @Test
    public void test_40H_1445() {
        finderSuite = new BestMoveSearchSuite(5);
        EDPReader.EDPEntry edpEntry = edpReader.readEdpLine("2r3k1/p4p2/3Rp2p/1p2P1pK/8/1P4P1/P3Q2P/1q6 b - - bm Qb1-g6+; ce -M3; pv Qb1-g6+ Kh5-g4 Qg6-f5+ Kg4-h5 Qf5-h3+; id \"1445\";");
        assertTrue(finderSuite.run(edpEntry));
    }
}
