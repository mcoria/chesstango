package net.chesstango.board.internal.moves;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.internal.moves.factories.MoveFactoryWhite;
import net.chesstango.board.position.State;
import net.chesstango.board.internal.position.StateImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author Mauricio Coria
 */
public class LoseCastlingBlackAfterWhiteMovesTest {
    private MoveFactoryWhite moveFactoryImp;

    private MoveImp moveExecutor;

    private State state;

    @BeforeEach
    public void setUp() throws Exception {
        moveFactoryImp = new MoveFactoryWhite();
        state = new StateImp();
        moveExecutor = null;
    }

    @Test
    public void testCapturaTorreByKing1() {
        state.setCurrentTurn(Color.WHITE);
        state.setCastlingWhiteKingAllowed(false);
        state.setCastlingWhiteQueenAllowed(false);
        state.setCastlingBlackKingAllowed(true);
        state.setCastlingBlackQueenAllowed(true);
        state.setHalfMoveClock(2);
        state.setFullMoveClock(5);

        PiecePositioned origen = PiecePositioned.of(Square.g7, Piece.KING_WHITE);
        PiecePositioned destino = PiecePositioned.of(Square.h8, Piece.ROOK_BLACK);

        moveExecutor = moveFactoryImp.createCaptureKingMove(origen, destino);

        moveExecutor.doMove(state);

        assertEquals(Color.BLACK, state.getCurrentTurn());
        assertFalse(state.isCastlingWhiteKingAllowed());
        assertFalse(state.isCastlingWhiteQueenAllowed());
        assertFalse(state.isCastlingBlackKingAllowed());
        assertTrue(state.isCastlingBlackQueenAllowed());
        assertEquals(0, state.getHalfMoveClock());
        assertEquals(5, state.getFullMoveClock());
    }

    @Test
    public void testCapturaTorreByKing() {
        state.setCurrentTurn(Color.WHITE);
        state.setCastlingWhiteKingAllowed(false);
        state.setCastlingWhiteQueenAllowed(false);
        state.setCastlingBlackKingAllowed(true);
        state.setCastlingBlackQueenAllowed(true);
        state.setHalfMoveClock(2);
        state.setFullMoveClock(5);

        PiecePositioned origen = PiecePositioned.of(Square.b7, Piece.KING_WHITE);
        PiecePositioned destino = PiecePositioned.of(Square.a8, Piece.ROOK_BLACK);

        moveExecutor = moveFactoryImp.createCaptureKingMove(origen, destino);

        moveExecutor.doMove(state);

        assertEquals(Color.BLACK, state.getCurrentTurn());
        assertFalse(state.isCastlingWhiteKingAllowed());
        assertFalse(state.isCastlingWhiteQueenAllowed());
        assertTrue(state.isCastlingBlackKingAllowed());
        assertFalse(state.isCastlingBlackQueenAllowed());
        assertEquals(0, state.getHalfMoveClock());
        assertEquals(5, state.getFullMoveClock());
    }

    @Test
    public void testCapturaTorreByQueen1() {
        state.setCurrentTurn(Color.WHITE);
        state.setCastlingWhiteKingAllowed(false);
        state.setCastlingWhiteQueenAllowed(false);
        state.setCastlingBlackKingAllowed(true);
        state.setCastlingBlackQueenAllowed(true);
        state.setHalfMoveClock(2);
        state.setFullMoveClock(5);

        PiecePositioned origen = PiecePositioned.of(Square.g7, Piece.QUEEN_WHITE);
        PiecePositioned destino = PiecePositioned.of(Square.h8, Piece.ROOK_BLACK);

        moveExecutor = moveFactoryImp.createCaptureKnightMove(origen, destino);

        moveExecutor.doMove(state);

        assertEquals(Color.BLACK, state.getCurrentTurn());
        assertFalse(state.isCastlingWhiteKingAllowed());
        assertFalse(state.isCastlingWhiteQueenAllowed());
        assertFalse(state.isCastlingBlackKingAllowed());
        assertTrue(state.isCastlingBlackQueenAllowed());
        assertEquals(0, state.getHalfMoveClock());
        assertEquals(5, state.getFullMoveClock());
    }

    @Test
    public void testCapturaTorreByQueen2() {
        state.setCurrentTurn(Color.WHITE);
        state.setCastlingWhiteKingAllowed(false);
        state.setCastlingWhiteQueenAllowed(false);
        state.setCastlingBlackKingAllowed(true);
        state.setCastlingBlackQueenAllowed(true);
        state.setHalfMoveClock(2);
        state.setFullMoveClock(5);

        PiecePositioned origen = PiecePositioned.of(Square.b7, Piece.QUEEN_WHITE);
        PiecePositioned destino = PiecePositioned.of(Square.a8, Piece.ROOK_BLACK);

        moveExecutor = moveFactoryImp.createCaptureKnightMove(origen, destino);

        moveExecutor.doMove(state);

        assertEquals(Color.BLACK, state.getCurrentTurn());
        assertFalse(state.isCastlingWhiteKingAllowed());
        assertFalse(state.isCastlingWhiteQueenAllowed());
        assertTrue(state.isCastlingBlackKingAllowed());
        assertFalse(state.isCastlingBlackQueenAllowed());
        assertEquals(0, state.getHalfMoveClock());
        assertEquals(5, state.getFullMoveClock());
    }

    @Test
    public void testCapturaTorreByPawnPromotion1() {
        state.setCurrentTurn(Color.WHITE);
        state.setCastlingWhiteKingAllowed(false);
        state.setCastlingWhiteQueenAllowed(false);
        state.setCastlingBlackKingAllowed(true);
        state.setCastlingBlackQueenAllowed(true);
        state.setHalfMoveClock(2);
        state.setFullMoveClock(5);

        PiecePositioned origen = PiecePositioned.of(Square.g7, Piece.PAWN_WHITE);
        PiecePositioned destino = PiecePositioned.of(Square.h8, Piece.ROOK_BLACK);

        moveExecutor = moveFactoryImp.createCapturePromotionPawnMove(origen, destino, Piece.ROOK_WHITE, Cardinal.NorteEste);

        moveExecutor.doMove(state);

        assertEquals(Color.BLACK, state.getCurrentTurn());
        assertFalse(state.isCastlingWhiteKingAllowed());
        assertFalse(state.isCastlingWhiteQueenAllowed());
        assertFalse(state.isCastlingBlackKingAllowed());
        assertTrue(state.isCastlingBlackQueenAllowed());
        assertEquals(0, state.getHalfMoveClock());
        assertEquals(5, state.getFullMoveClock());
    }

    @Test
    public void testCapturaTorreByPawnPromotion2() {
        state.setCurrentTurn(Color.WHITE);
        state.setCastlingWhiteKingAllowed(false);
        state.setCastlingWhiteQueenAllowed(false);
        state.setCastlingBlackKingAllowed(true);
        state.setCastlingBlackQueenAllowed(true);
        state.setHalfMoveClock(2);
        state.setFullMoveClock(5);

        PiecePositioned origen = PiecePositioned.of(Square.b7, Piece.PAWN_WHITE);
        PiecePositioned destino = PiecePositioned.of(Square.a8, Piece.ROOK_BLACK);

        moveExecutor = moveFactoryImp.createCapturePromotionPawnMove(origen, destino, Piece.ROOK_WHITE, Cardinal.NorteOeste);

        moveExecutor.doMove(state);

        assertEquals(Color.BLACK, state.getCurrentTurn());
        assertFalse(state.isCastlingWhiteKingAllowed());
        assertFalse(state.isCastlingWhiteQueenAllowed());
        assertTrue(state.isCastlingBlackKingAllowed());
        assertFalse(state.isCastlingBlackQueenAllowed());
        assertEquals(0, state.getHalfMoveClock());
        assertEquals(5, state.getFullMoveClock());
    }

    @Test
    public void testCapturaTorreByTorre01() {
        state.setCurrentTurn(Color.WHITE);
        state.setCastlingWhiteKingAllowed(true);
        state.setCastlingWhiteQueenAllowed(true);
        state.setCastlingBlackKingAllowed(true);
        state.setCastlingBlackQueenAllowed(true);
        state.setHalfMoveClock(2);
        state.setFullMoveClock(5);


        PiecePositioned origen = PiecePositioned.of(Square.a1, Piece.ROOK_WHITE);
        PiecePositioned destino = PiecePositioned.of(Square.a8, Piece.ROOK_BLACK);

        moveExecutor = moveFactoryImp.createCaptureRookMove(origen, destino, Cardinal.Norte);

        moveExecutor.doMove(state);

        assertEquals(Color.BLACK, state.getCurrentTurn());
        assertTrue(state.isCastlingWhiteKingAllowed());
        assertFalse(state.isCastlingWhiteQueenAllowed());
        assertTrue(state.isCastlingBlackKingAllowed());
        assertFalse(state.isCastlingBlackQueenAllowed());
        assertEquals(0, state.getHalfMoveClock());
        assertEquals(5, state.getFullMoveClock());
    }


    @Test
    public void testCapturaTorreByTorre02() {
        state.setCurrentTurn(Color.WHITE);
        state.setCastlingWhiteKingAllowed(true);
        state.setCastlingWhiteQueenAllowed(true);
        state.setCastlingBlackKingAllowed(true);
        state.setCastlingBlackQueenAllowed(true);
        state.setHalfMoveClock(2);
        state.setFullMoveClock(5);


        PiecePositioned origen = PiecePositioned.of(Square.h1, Piece.ROOK_WHITE);
        PiecePositioned destino = PiecePositioned.of(Square.h8, Piece.ROOK_BLACK);

        moveExecutor = moveFactoryImp.createCaptureRookMove(origen, destino, Cardinal.Norte);

        moveExecutor.doMove(state);

        assertEquals(Color.BLACK, state.getCurrentTurn());
        assertFalse(state.isCastlingWhiteKingAllowed());
        assertTrue(state.isCastlingWhiteQueenAllowed());
        assertFalse(state.isCastlingBlackKingAllowed());
        assertTrue(state.isCastlingBlackQueenAllowed());
        assertEquals(0, state.getHalfMoveClock());
        assertEquals(5, state.getFullMoveClock());
    }
}
