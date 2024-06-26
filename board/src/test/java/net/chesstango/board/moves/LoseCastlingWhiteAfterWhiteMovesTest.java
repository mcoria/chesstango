package net.chesstango.board.moves;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.factory.SingletonMoveFactories;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.factories.MoveFactory;
import net.chesstango.board.moves.imp.MoveImp;
import net.chesstango.board.position.PositionState;
import net.chesstango.board.position.imp.PositionStateImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mauricio Coria
 */
public class LoseCastlingWhiteAfterWhiteMovesTest {

    private MoveFactory moveFactoryImp;

    private MoveImp moveExecutor;

    private PositionState positionState;

    @BeforeEach
    public void setUp() throws Exception {
        moveFactoryImp = SingletonMoveFactories.getDefaultMoveFactoryWhite();
        positionState = new PositionStateImp();
        moveExecutor = null;
    }

    @Test
    public void testSimpleKingMovePierdeEnroque() {
        positionState.setCurrentTurn(Color.WHITE);
        positionState.setCastlingWhiteKingAllowed(true);
        positionState.setCastlingWhiteQueenAllowed(true);
        positionState.setCastlingBlackKingAllowed(true);
        positionState.setCastlingBlackQueenAllowed(true);
        positionState.setHalfMoveClock(2);
        positionState.setFullMoveClock(5);

        PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.e1, Piece.KING_WHITE);
        PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.e2, null);

        moveExecutor = moveFactoryImp.createSimpleKingMove(origen, destino);

        moveExecutor.doMove(positionState);

        assertEquals(Color.BLACK, positionState.getCurrentTurn());
        assertFalse(positionState.isCastlingWhiteKingAllowed());
        assertFalse(positionState.isCastlingWhiteQueenAllowed());
        assertTrue(positionState.isCastlingBlackKingAllowed());
        assertTrue(positionState.isCastlingBlackQueenAllowed());
        assertEquals(3, positionState.getHalfMoveClock());
        assertEquals(5, positionState.getFullMoveClock());
    }

    @Test
    public void testCapturaKingMovePierdeEnroque() {
        positionState.setCurrentTurn(Color.WHITE);
        positionState.setCastlingWhiteKingAllowed(true);
        positionState.setCastlingWhiteQueenAllowed(true);
        positionState.setCastlingBlackKingAllowed(true);
        positionState.setCastlingBlackQueenAllowed(true);
        positionState.setHalfMoveClock(2);
        positionState.setFullMoveClock(5);

        PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.e1, Piece.KING_WHITE);
        PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.e2, Piece.KNIGHT_BLACK);

        moveExecutor = moveFactoryImp.createCaptureKingMove(origen, destino);

        moveExecutor.doMove(positionState);

        assertEquals(Color.BLACK, positionState.getCurrentTurn());
        assertFalse(positionState.isCastlingWhiteKingAllowed());
        assertFalse(positionState.isCastlingWhiteQueenAllowed());
        assertTrue(positionState.isCastlingBlackKingAllowed());
        assertTrue(positionState.isCastlingBlackQueenAllowed());
        assertEquals(0, positionState.getHalfMoveClock());
        assertEquals(5, positionState.getFullMoveClock());
    }

    @Test
    public void testSimpleRookMovePierdeEnroque() {
        positionState.setCurrentTurn(Color.WHITE);
        positionState.setCastlingWhiteKingAllowed(true);
        positionState.setCastlingWhiteQueenAllowed(true);
        positionState.setCastlingBlackKingAllowed(true);
        positionState.setCastlingBlackQueenAllowed(true);
        positionState.setHalfMoveClock(2);
        positionState.setFullMoveClock(5);


        PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.a1, Piece.ROOK_WHITE);
        PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.a2, null);

        moveExecutor = moveFactoryImp.createSimpleRookMove(origen, destino, Cardinal.Norte);

        moveExecutor.doMove(positionState);

        assertEquals(Color.BLACK, positionState.getCurrentTurn());
        assertTrue(positionState.isCastlingWhiteKingAllowed());
        assertFalse(positionState.isCastlingWhiteQueenAllowed());
        assertTrue(positionState.isCastlingBlackKingAllowed());
        assertTrue(positionState.isCastlingBlackQueenAllowed());
        assertEquals(3, positionState.getHalfMoveClock());
        assertEquals(5, positionState.getFullMoveClock());
    }

    @Test
    public void testCaptureRookMovePierdeEnroque() {
        positionState.setCurrentTurn(Color.WHITE);
        positionState.setCastlingWhiteKingAllowed(true);
        positionState.setCastlingWhiteQueenAllowed(true);
        positionState.setCastlingBlackKingAllowed(true);
        positionState.setCastlingBlackQueenAllowed(true);
        positionState.setHalfMoveClock(2);
        positionState.setFullMoveClock(5);


        PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.a1, Piece.ROOK_WHITE);
        PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.a2, Piece.PAWN_BLACK);

        moveExecutor = moveFactoryImp.createCaptureRookMove(origen, destino, Cardinal.Norte);

        moveExecutor.doMove(positionState);

        assertEquals(Color.BLACK, positionState.getCurrentTurn());
        assertTrue(positionState.isCastlingWhiteKingAllowed());
        assertFalse(positionState.isCastlingWhiteQueenAllowed());
        assertTrue(positionState.isCastlingBlackKingAllowed());
        assertTrue(positionState.isCastlingBlackQueenAllowed());
        assertEquals(0, positionState.getHalfMoveClock());
        assertEquals(5, positionState.getFullMoveClock());
    }

}
