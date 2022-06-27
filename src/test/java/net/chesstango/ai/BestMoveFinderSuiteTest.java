/**
 * 
 */
package net.chesstango.ai;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Mauricio Coria
 *
 */
public class BestMoveFinderSuiteTest {

	@Test
	@Ignore
	public void test01() {
		BestMoveFinderSuite suite = new BestMoveFinderSuite();
		
		Assert.assertTrue( suite.run("n1QBq1k1/5p1p/5KP1/p7/8/8/8/8 w - - 0 1 bm d8c7") );
	}
	
	
	@Test
	public void test02() {
		BestMoveFinderSuite suite = new BestMoveFinderSuite();
		
		Assert.assertTrue( suite.run("1R6/1brk2p1/4p2p/p1P1Pp2/P7/6P1/1P4P1/2R3K1 w - - 0 1 bm b8b7") );
	}	

}
