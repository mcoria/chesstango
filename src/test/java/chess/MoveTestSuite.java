package chess;

import chess.board.moves.imp.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Mauricio Coria
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ AbstractMoveTest.class, CaptureEnPassantTest.class, CaptureKingMoveTest.class, CaptureMoveTest.class,
        CapturePawnPromotionTest.class, CastlingBlackKingMoveTest.class, CastlingBlackQueenMoveTest.class,
        CastlingWhiteKingMoveTest.class, CastlingWhiteQueenMoveTest.class, MoveFactoryBlackTest.class, MoveFactoryWhiteTest.class,
        SimpleTwoSquaresPawnMoveTest.class, SimpleKingMoveTest.class, SimpleMoveTest.class, SimplePawnPromotionTest.class,
})
public class MoveTestSuite {
}
