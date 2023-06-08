package net.chesstango;

import net.chesstango.board.*;
import net.chesstango.board.moves.containers.MoveContainerTest;
import net.chesstango.board.movesgenerators.legal.squarecapturers.FullScanSquareCapturedTest;
import net.chesstango.board.movesgenerators.legal.strategies.CheckLegalMoveGeneratorTest;
import net.chesstango.board.movesgenerators.legal.strategies.NoCheckLegalMoveGeneratorTest;
import net.chesstango.board.position.ChessPositionTest;
import net.chesstango.board.position.imp.ArrayBoardTest;
import net.chesstango.board.position.imp.ChessPositionImpTest;
import net.chesstango.board.position.imp.ColorBoardTest;
import net.chesstango.board.position.imp.MoveCacheBoardTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;


/**
 * @author Mauricio Coria
 *
 */
@Suite
@SelectClasses({ PieceTest.class, SquareTest.class, PiecePositionedTest.class, GameTest.class, FullScanSquareCapturedTest.class,
		ChessPositionTest.class, MoveCacheBoardTest.class, ColorBoardTest.class, ArrayBoardTest.class,
		ChessPositionImpTest.class, CheckLegalMoveGeneratorTest.class, NoCheckLegalMoveGeneratorTest.class, MoveContainerTest.class,
		ZobristNoCollisionTest.class})
public class BasicTestSuite {

}
