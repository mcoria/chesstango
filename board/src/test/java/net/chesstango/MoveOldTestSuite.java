package net.chesstango;

import net.chesstango.board.moves.impl.inheritance.*;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author Mauricio Coria
 *
 */
@Suite
@SelectClasses({ AbstractMoveTest.class, CapturePawnEnPassantTest.class, CaptureKingMoveTest.class, CaptureMoveTest.class,
        CapturePawnPromotionTest.class, CastlingBlackKingMoveTest.class, CastlingBlackQueenMoveTest.class,
        CastlingWhiteKingMoveTest.class, CastlingWhiteQueenMoveTest.class, MoveFactoryBlackTest.class, MoveFactoryWhiteTest.class,
        SimpleTwoSquaresPawnMoveTest.class, SimpleKingMoveTest.class, SimpleMoveTest.class, SimplePawnPromotionTest.class
})
public class MoveOldTestSuite {
}
