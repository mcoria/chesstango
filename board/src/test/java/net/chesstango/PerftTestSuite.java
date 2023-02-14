/**
 * 
 */
package net.chesstango;

import net.chesstango.board.perft.*;
import net.chesstango.board.perft.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Mauricio Coria
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ InitialPositionTest.class, KiwipeteTest.class, PerftMainTestSuiteTest.class, PerftDifferentPositionsTest.class,
		Pos_4k2r_6K1_8_8_8_8_8_8_w_k_Test.class })
public class PerftTestSuite {
}