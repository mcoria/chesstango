/**
 * 
 */
package net.chesstango;

import net.chesstango.board.GameTest;
import net.chesstango.board.PieceTest;
import net.chesstango.board.SquareTest;
import net.chesstango.board.moves.containers.MoveContainerTest;
import net.chesstango.board.movesgenerators.legal.squarecapturers.FullScanSquareCapturerTest;
import net.chesstango.board.movesgenerators.legal.strategies.CheckLegalMoveGeneratorTest;
import net.chesstango.board.movesgenerators.legal.strategies.NoCheckLegalMoveGeneratorTest;
import net.chesstango.board.position.ChessPositionTest;
import net.chesstango.board.position.imp.ArrayPiecePlacementTest;
import net.chesstango.board.position.imp.ChessPositionImpTest;
import net.chesstango.board.position.imp.ColorBoardTest;
import net.chesstango.board.position.imp.MoveCacheBoardTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

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