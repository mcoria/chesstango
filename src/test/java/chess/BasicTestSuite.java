/**
 * 
 */
package chess;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import chess.board.GameTest;
import chess.board.PieceTest;
import chess.board.SquareTest;
import chess.board.analyzer.capturer.CapturerTest;
import chess.board.legalmovesgenerators.strategies.DefaultLegalMoveGeneratorTest;
import chess.board.legalmovesgenerators.strategies.NoCheckLegalMoveGeneratorTest;
import chess.board.position.ChessPositionTest;
import chess.board.position.imp.ArrayPiecePlacementTest;
import chess.board.position.imp.ColorBoardTest;
import chess.board.position.imp.MoveCacheBoardTest;

/**
 * @author Mauricio Coria
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ PieceTest.class, SquareTest.class, GameTest.class, CapturerTest.class, ChessPositionTest.class,
		MoveCacheBoardTest.class, ColorBoardTest.class, ArrayPiecePlacementTest.class,
		DefaultLegalMoveGeneratorTest.class, NoCheckLegalMoveGeneratorTest.class })
public class BasicTestSuite {

}
