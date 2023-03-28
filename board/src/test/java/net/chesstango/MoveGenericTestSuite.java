package net.chesstango;

import net.chesstango.board.moves.*;
import net.chesstango.board.moves.AbstractMoveTest;
import net.chesstango.board.moves.CaptureKingMoveTest;
import net.chesstango.board.moves.CaptureMoveTest;
import net.chesstango.board.moves.CapturePawnEnPassantTest;
import net.chesstango.board.moves.CapturePawnPromotionTest;
import net.chesstango.board.moves.CastlingBlackKingTest;
import net.chesstango.board.moves.CastlingBlackQueenTest;
import net.chesstango.board.moves.CastlingWhiteKingTest;
import net.chesstango.board.moves.CastlingWhiteQueenTest;
import net.chesstango.board.moves.SimpleKingMoveTest;
import net.chesstango.board.moves.SimpleMoveTest;
import net.chesstango.board.moves.SimplePawnMoveTest;
import net.chesstango.board.moves.SimplePawnPromotionTest;
import net.chesstango.board.moves.SimpleTwoSquaresPawnMoveTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Mauricio Coria
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
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
