/**
 * 
 */
package chess;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import chess.board.GameTest;
import chess.board.PieceTest;
import chess.board.SquareTest;
import chess.board.analyzer.CapturerTest;

/**
 * @author Mauricio Coria
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ PieceTest.class, SquareTest.class, GameTest.class, CapturerTest.class })
public class BasicTestSuite {

}
