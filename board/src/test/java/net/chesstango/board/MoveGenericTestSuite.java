package net.chesstango.board;

import net.chesstango.board.internal.moves.*;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author Mauricio Coria
 *
 */
@Suite
@SelectClasses({
        AbstractMoveTest.class,
        CaptureKingMoveTest.class,
        CaptureMoveTest.class,
        CapturePawnEnPassantTest.class,
        CapturePawnPromotionTest.class,
        CastlingBlackKingTest.class,
        CastlingBlackQueenTest.class,
        CastlingWhiteKingTest.class,
        CastlingWhiteQueenTest.class,
        LoseCastlingBlackAfterBlackMovesTest.class,
        LoseCastlingBlackAfterWhiteMovesTest.class,
        LoseCastlingWhiteAfterBlackMovesTest.class,
        LoseCastlingWhiteAfterWhiteMovesTest.class,
        SimpleKingMoveTest.class,
        SimpleMoveTest.class,
        SimplePawnMoveTest.class,
        SimplePawnPromotionTest.class,
        SimpleTwoSquaresPawnMoveTest.class
})
public class MoveGenericTestSuite {
}
