package net.chesstango.board;

import net.chesstango.board.analyzer.KingSafePositionsAnalyzerTest;
import net.chesstango.board.analyzer.PositionAnalyzerTest;
import net.chesstango.board.internal.GameTest;
import net.chesstango.board.internal.moves.generators.legal.squarecapturers.FullScanSquareCapturedTest;
import net.chesstango.board.internal.position.*;
import net.chesstango.board.moves.containers.MoveContainerTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;



/**
 * @author Mauricio Coria
 *
 */
@Suite
@SelectClasses({ PieceTest.class, SquareTest.class, PiecePositionedTest.class, GameTest.class, FullScanSquareCapturedTest.class,
		PositionTest.class, MoveCacheBoardImpTest.class, BitBoardTest.class, SquareBoardImpTest.class,
		PositionIteratorTest.class, MoveContainerTest.class,
		ZobristNoCollisionTest.class, PositionAnalyzerTest.class, KingSafePositionsAnalyzerTest.class})
public class BasicTestSuite {

}
