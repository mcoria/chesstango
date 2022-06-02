/**
 * 
 */
package chess;

import chess.board.moves.containers.MoveContainerTest;
import chess.board.position.imp.ChessPositionImpTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import chess.board.GameTest;
import chess.board.PieceTest;
import chess.board.SquareTest;
import chess.board.movesgenerators.legal.squarecapturers.FullScanSquareCapturerTest;
import chess.board.movesgenerators.legal.strategies.CheckLegalMoveGeneratorTest;
import chess.board.movesgenerators.legal.strategies.NoCheckLegalMoveGeneratorTest;
import chess.board.position.ChessPositionTest;
import chess.board.position.imp.ArrayPiecePlacementTest;
import chess.board.position.imp.ColorBoardTest;
import chess.board.position.imp.MoveCacheBoardTest;

/**
 * @author Mauricio Coria
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ PieceTest.class, SquareTest.class, GameTest.class, FullScanSquareCapturerTest.class, ChessPositionTest.class,
		MoveCacheBoardTest.class, ColorBoardTest.class, ArrayPiecePlacementTest.class, ChessPositionImpTest.class,
		CheckLegalMoveGeneratorTest.class, NoCheckLegalMoveGeneratorTest.class, MoveContainerTest.class })
public class BasicTestSuite {

}
