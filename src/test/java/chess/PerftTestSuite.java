/**
 * 
 */
package chess;

import chess.board.perft.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Mauricio Coria
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ InitialPositionTest.class, KiwipeteTest.class, PerftMainTestSuiteTest.class, PerftTest.class,
		Pos_4k2r_6K1_8_8_8_8_8_8_w_k_Test.class })
public class PerftTestSuite {
}
