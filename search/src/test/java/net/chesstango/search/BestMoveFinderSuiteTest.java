/**
 *
 */
package net.chesstango.search;

import net.chesstango.board.representations.EDPReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * @author Mauricio Coria
 */
public class BestMoveFinderSuiteTest {
    private EDPReader edpReader;

    @BeforeEach
    public void setup() {
        edpReader = new EDPReader();
    }

    @Test
    public void test01() {
        BestMoveFinderSuite finderSuite = new BestMoveFinderSuite(8);
        EDPReader.EDPEntry edpEntry = edpReader.readEdpLine("1B1bk1nr/5pp1/rNR5/p3Pp1p/1p2pP2/1P5P/1PP3P1/2KR4 w k - bm Rd1xd8+; ce +M5; pv Rd1xd8+ Ke8xd8 Rc6-c8+ Kd8-e7 Bb8-d6+ Ke7-e6 Rc8-e8+ Ng8-e7 Re8xe7+; id \"5712\";");
        assertTrue(finderSuite.run(edpEntry));
    }

    @Test
    public void test02() {
        BestMoveFinderSuite finderSuite = new BestMoveFinderSuite(4);
        EDPReader.EDPEntry edpEntry = edpReader.readEdpLine("5rk1/1ppb3p/p1pb4/6q1/3P1p1r/2P1R2P/PP1BQ1P1/5RKN w - - bm Rg3; id \"WAC.003\";");
        assertTrue(finderSuite.run(edpEntry));
    }

    @Test
    public void test03() {
        BestMoveFinderSuite finderSuite = new BestMoveFinderSuite(1);
        EDPReader.EDPEntry edpEntry = edpReader.readEdpLine("r3r1k1/pp1n1ppp/2p5/4Pb2/2B2P2/B1P5/P5PP/R2R2K1 w - - bm e6; id \"WAC.072\";");
        assertTrue(finderSuite.run(edpEntry));
    }

    @Test
    @Disabled
    public void test04() {
        BestMoveFinderSuite finderSuite = new BestMoveFinderSuite(7);
        EDPReader.EDPEntry edpEntry = edpReader.readEdpLine("1k1r3r/pp6/2P1bp2/2R1p3/Q3Pnp1/P2q4/1BR3B1/6K1 b - - bm Nf4-h3+; ce -M4; pv Nf4-h3+ Bg2xh3 Qd3-g3+ Rc2-g2 Qg3-e1+ Kg1-h2 Rh8xh3+; id \"4778\";");
        assertTrue(finderSuite.run(edpEntry));
    }

}
