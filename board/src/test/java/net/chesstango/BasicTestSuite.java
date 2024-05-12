package net.chesstango;

import net.chesstango.board.*;
import net.chesstango.board.analyzer.KingSafePositionsAnalyzerTest;
import net.chesstango.board.analyzer.PositionAnalyzerTest;
import net.chesstango.board.moves.containers.MoveContainerTest;
import net.chesstango.board.moves.generators.legal.squarecapturers.FullScanSquareCapturedTest;
import net.chesstango.board.moves.generators.legal.strategies.CheckLegalMoveGeneratorTest;
import net.chesstango.board.moves.generators.legal.strategies.NoCheckLegalMoveGeneratorTest;
import net.chesstango.board.position.ChessPositionTest;
import net.chesstango.board.position.imp.SquareBoardImpTest;
import net.chesstango.board.position.imp.ChessPositionImpTest;
import net.chesstango.board.position.imp.BitBoardTest;
import net.chesstango.board.position.imp.MoveCacheBoardImpTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;


/**
 * @author Mauricio Coria
 *
 */
@Suite
@SelectClasses({ PieceTest.class, SquareTest.class, PiecePositionedTest.class, GameTest.class, FullScanSquareCapturedTest.class,
		ChessPositionTest.class, MoveCacheBoardImpTest.class, BitBoardTest.class, SquareBoardImpTest.class,
		ChessPositionImpTest.class, CheckLegalMoveGeneratorTest.class, NoCheckLegalMoveGeneratorTest.class, MoveContainerTest.class,
		ZobristNoCollisionTest.class, PositionAnalyzerTest.class, KingSafePositionsAnalyzerTest.class})
public class BasicTestSuite {

}
