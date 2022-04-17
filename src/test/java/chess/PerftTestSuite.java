/**
 * 
 */
package chess;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import chess.board.perft.InitialPositionTests;
import chess.board.perft.KiwipeteTest;
import chess.board.perft.PerftMainTestSuiteTest;
import chess.board.perft.PerftTest;
import chess.board.perft.Pos_4k2r_6K1_8_8_8_8_8_8_w_k_Test;

/**
 * @author Mauricio Coria
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ InitialPositionTests.class, KiwipeteTest.class, PerftMainTestSuiteTest.class, PerftTest.class,
		Pos_4k2r_6K1_8_8_8_8_8_8_w_k_Test.class })
public class PerftTestSuite {

}
