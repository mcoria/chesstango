/**
 *
 */
package net.chesstango.search;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Mauricio Coria
 *
 */
public class SearchMoveSuiteTest {

    private BestMoveFinderSuite finderSuite;
    @Before
    public void setup(){
        finderSuite = new BestMoveFinderSuite(7);
    }

    @Test
    public void test01() {
        Assert.assertTrue(finderSuite.run("n1QBq1k1/5p1p/5KP1/p7/8/8/8/8 w - - 0 1 bm d8c7"));
    }


    @Test
    public void test02() {
        Assert.assertTrue(finderSuite.run("1R6/1brk2p1/4p2p/p1P1Pp2/P7/6P1/1P4P1/2R3K1 w - - 0 1 bm b8b7"));
    }

    @Test
    public void test03() {
        Assert.assertTrue(finderSuite.run("1B1bk1nr/5pp1/rNR5/p3Pp1p/1p2pP2/1P5P/1PP3P1/2KR4 w k - 0 1 bm Rd1xd8+"));
    }

}
