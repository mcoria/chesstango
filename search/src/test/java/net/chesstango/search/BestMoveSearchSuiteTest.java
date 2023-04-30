package net.chesstango.search;

import net.chesstango.board.representations.EDPReader;
import net.chesstango.search.reports.SearchesReport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * @author Mauricio Coria
 */
public class BestMoveSearchSuiteTest {
    private EDPReader edpReader;

    private BestMoveSearchSuite finderSuite;

    @BeforeEach
    public void setup() {
        edpReader = new EDPReader();
    }

    @AfterEach
    public void tearDown(){
        new SearchesReport()
                .withNodesVisitedStatics()
                .withCutoffStatics()
                .withPrincipalVariation()
                .printSearchesStatics(finderSuite.searchMoveResults);
    }

    @Test
    public void test01() {
        finderSuite = new BestMoveSearchSuite(8);
        EDPReader.EDPEntry edpEntry = edpReader.readEdpLine("1B1bk1nr/5pp1/rNR5/p3Pp1p/1p2pP2/1P5P/1PP3P1/2KR4 w k - bm Rd1xd8+; ce +M5; pv Rd1xd8+ Ke8xd8 Rc6-c8+ Kd8-e7 Bb8-d6+ Ke7-e6 Rc8-e8+ Ng8-e7 Re8xe7+; id \"5712\";");
        assertTrue(finderSuite.run(edpEntry));
    }

    @Test
    public void test02() {
        finderSuite = new BestMoveSearchSuite(4);
        EDPReader.EDPEntry edpEntry = edpReader.readEdpLine("5rk1/1ppb3p/p1pb4/6q1/3P1p1r/2P1R2P/PP1BQ1P1/5RKN w - - bm Rg3; id \"WAC.003\";");
        assertTrue(finderSuite.run(edpEntry));
    }

    @Test
    public void test03() {
        finderSuite = new BestMoveSearchSuite(1);
        EDPReader.EDPEntry edpEntry = edpReader.readEdpLine("r3r1k1/pp1n1ppp/2p5/4Pb2/2B2P2/B1P5/P5PP/R2R2K1 w - - bm e6; id \"WAC.072\";");
        assertTrue(finderSuite.run(edpEntry));
    }

    @Test
    public void test04() {
        finderSuite = new BestMoveSearchSuite(1);
        EDPReader.EDPEntry edpEntry = edpReader.readEdpLine("r3r1k1/pp1n1ppp/2p5/4Pb2/2B2P2/B1P5/P5PP/R2R2K1 w - - bm e6; id \"WAC.072\";");
        assertTrue(finderSuite.run(edpEntry));
    }

    @Test
    public void test05() {
        finderSuite = new BestMoveSearchSuite(2);
        EDPReader.EDPEntry edpEntry = edpReader.readEdpLine("r1b2rk1/2q1b1pp/p2ppn2/1p6/3QP3/1BN1B3/PPP3PP/R4RK1 w - - bm Nd5 a4; id \"BK.05\";");
        assertTrue(finderSuite.run(edpEntry));
    }


    @Test
    public void test06() {
        finderSuite = new BestMoveSearchSuite(3);
        EDPReader.EDPEntry edpEntry = edpReader.readEdpLine("1R1nk2r/4q1p1/5pP1/3QpP1p/P6P/5P2/5BK1/2r5 w k - bm Rb8xd8+; ce +M2; pv Rb8xd8+ Qe7xd8 Qd5-f7+; id \"079\";");
        assertTrue(finderSuite.run(edpEntry));
    }

    @Test
    public void test07() {
        finderSuite = new BestMoveSearchSuite(5);
        EDPReader.EDPEntry edpEntry = edpReader.readEdpLine("1R3b1k/2p3pp/4qr2/Q7/3p2P1/3P3K/6NP/8 b - - bm Rf6-f3+; ce -M3; pv Rf6-f3+ Kh3-h4 Qe6-h6+ Qa5-h5 g7-g5+; id \"2857\";");
        assertTrue(finderSuite.run(edpEntry));
    }

}
