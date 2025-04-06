package net.chesstango.board.internal.moves;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.internal.moves.factories.MoveFactoryBlack;
import net.chesstango.board.position.State;
import net.chesstango.board.internal.position.StateImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author Mauricio Coria
 */
public class LoseCastlingWhiteAfterBlackMovesTest {
    private MoveFactoryBlack moveFactoryImp;

    private MoveImp moveExecutor;

    private State state;

    @BeforeEach
    public void setUp() throws Exception {
        moveFactoryImp = new MoveFactoryBlack();
        state = new StateImp();
        moveExecutor = null;
    }

    @Test
    public void testCapturaTorreByKing1() {
        state.setCurrentTurn(Color.BLACK);
        state.setCastlingWhiteKingAllowed(true);
        state.setCastlingWhiteQueenAllowed(true);
        state.setCastlingBlackKingAllowed(false);
        state.setCastlingBlackQueenAllowed(false);
        state.setHalfMoveClock(2);
        state.setFullMoveClock(5);

        PiecePositioned origen = PiecePositioned.of(Square.g2, Piece.KING_BLACK);
        PiecePositioned destino = PiecePositioned.of(Square.h1, Piece.ROOK_WHITE);

        moveExecutor = moveFactoryImp.createCaptureKingMove(origen, destino);

        moveExecutor.doMove(state);

        assertEquals(Color.WHITE, state.getCurrentTurn());
        assertFalse(state.isCastlingWhiteKingAllowed());
        assertTrue(state.isCastlingWhiteQueenAllowed());
        assertFalse(state.isCastlingBlackKingAllowed());
        assertFalse(state.isCastlingBlackQueenAllowed());
        assertEquals(0, state.getHalfMoveClock());
        assertEquals(6, state.getFullMoveClock());
    }

    @Test
    public void testCapturaTorreByKing2() {
        state.setCurrentTurn(Color.BLACK);
        state.setCastlingWhiteKingAllowed(true);
        state.setCastlingWhiteQueenAllowed(true);
        state.setCastlingBlackKingAllowed(false);
        state.setCastlingBlackQueenAllowed(false);
        state.setHalfMoveClock(2);
        state.setFullMoveClock(5);

        PiecePositioned origen = PiecePositioned.of(Square.b2, Piece.KING_BLACK);
        PiecePositioned destino = PiecePositioned.of(Square.a1, Piece.ROOK_WHITE);

        moveExecutor = moveFactoryImp.createCaptureKingMove(origen, destino);

        moveExecutor.doMove(state);

        assertEquals(Color.WHITE, state.getCurrentTurn());
        assertTrue(state.isCastlingWhiteKingAllowed());
        assertFalse(state.isCastlingWhiteQueenAllowed());
        assertFalse(state.isCastlingBlackKingAllowed());
        assertFalse(state.isCastlingBlackQueenAllowed());
        assertEquals(0, state.getHalfMoveClock());
        assertEquals(6, state.getFullMoveClock());
    }

    @Test
    public void testCapturaTorreByQueen1() {
        state.setCurrentTurn(Color.BLACK);
        state.setCastlingWhiteKingAllowed(true);
        state.setCastlingWhiteQueenAllowed(true);
        state.setCastlingBlackKingAllowed(false);
        state.setCastlingBlackQueenAllowed(false);
        state.setHalfMoveClock(2);
        state.setFullMoveClock(5);

        PiecePositioned origen = PiecePositioned.of(Square.g2, Piece.QUEEN_BLACK);
        PiecePositioned destino = PiecePositioned.of(Square.h1, Piece.ROOK_WHITE);

        moveExecutor = moveFactoryImp.createCaptureKnightMove(origen, destino);

        moveExecutor.doMove(state);

        assertEquals(Color.WHITE, state.getCurrentTurn());
        assertFalse(state.isCastlingWhiteKingAllowed());
        assertTrue(state.isCastlingWhiteQueenAllowed());
        assertFalse(state.isCastlingBlackKingAllowed());
        assertFalse(state.isCastlingBlackQueenAllowed());
        assertEquals(0, state.getHalfMoveClock());
        assertEquals(6, state.getFullMoveClock());
    }

    @Test
    public void testCapturaTorreByQueen2() {
        state.setCurrentTurn(Color.BLACK);
        state.setCastlingWhiteKingAllowed(true);
        state.setCastlingWhiteQueenAllowed(true);
        state.setCastlingBlackKingAllowed(false);
        state.setCastlingBlackQueenAllowed(false);
        state.setHalfMoveClock(2);
        state.setFullMoveClock(5);

        PiecePositioned origen = PiecePositioned.of(Square.b2, Piece.QUEEN_BLACK);
        PiecePositioned destino = PiecePositioned.of(Square.a1, Piece.ROOK_WHITE);

        moveExecutor = moveFactoryImp.createCaptureKnightMove(origen, destino);

        moveExecutor.doMove(state);

        assertEquals(Color.WHITE, state.getCurrentTurn());
        assertTrue(state.isCastlingWhiteKingAllowed());
        assertFalse(state.isCastlingWhiteQueenAllowed());
        assertFalse(state.isCastlingBlackKingAllowed());
        assertFalse(state.isCastlingBlackQueenAllowed());
        assertEquals(0, state.getHalfMoveClock());
        assertEquals(6, state.getFullMoveClock());
    }

    @Test
    public void testCapturaTorreByPawnPromotion1() {
        state.setCurrentTurn(Color.BLACK);
        state.setCastlingWhiteKingAllowed(true);
        state.setCastlingWhiteQueenAllowed(true);
        state.setCastlingBlackKingAllowed(false);
        state.setCastlingBlackQueenAllowed(false);
        state.setHalfMoveClock(2);
        state.setFullMoveClock(5);

        PiecePositioned origen = PiecePositioned.of(Square.g2, Piece.PAWN_BLACK);
        PiecePositioned destino = PiecePositioned.of(Square.h1, Piece.ROOK_WHITE);

        moveExecutor = moveFactoryImp.createCapturePromotionPawnMove(origen, destino, Piece.ROOK_BLACK, Cardinal.SurEste);

        moveExecutor.doMove(state);

        assertEquals(Color.WHITE, state.getCurrentTurn());
        assertFalse(state.isCastlingWhiteKingAllowed());
        assertTrue(state.isCastlingWhiteQueenAllowed());
        assertFalse(state.isCastlingBlackKingAllowed());
        assertFalse(state.isCastlingBlackQueenAllowed());
        assertEquals(0, state.getHalfMoveClock());
        assertEquals(6, state.getFullMoveClock());
    }

    @Test
    public void testCapturaTorreByPawnPromotion2() {
        state.setCurrentTurn(Color.BLACK);
        state.setCastlingWhiteKingAllowed(true);
        state.setCastlingWhiteQueenAllowed(true);
        state.setCastlingBlackKingAllowed(false);
        state.setCastlingBlackQueenAllowed(false);
        state.setHalfMoveClock(2);
        state.setFullMoveClock(5);

        PiecePositioned origen = PiecePositioned.of(Square.b2, Piece.PAWN_BLACK);
        PiecePositioned destino = PiecePositioned.of(Square.a1, Piece.ROOK_WHITE);

        moveExecutor = moveFactoryImp.createCapturePromotionPawnMove(origen, destino, Piece.ROOK_BLACK, Cardinal.SurOeste);

        moveExecutor.doMove(state);

        assertEquals(Color.WHITE, state.getCurrentTurn());
        assertTrue(state.isCastlingWhiteKingAllowed());
        assertFalse(state.isCastlingWhiteQueenAllowed());
        assertFalse(state.isCastlingBlackKingAllowed());
        assertFalse(state.isCastlingBlackQueenAllowed());
        assertEquals(0, state.getHalfMoveClock());
        assertEquals(6, state.getFullMoveClock());
    }


    @Test
    public void testCapturaTorreByTorre01() {
        state.setCurrentTurn(Color.BLACK);
        state.setCastlingWhiteKingAllowed(true);
        state.setCastlingWhiteQueenAllowed(true);
        state.setCastlingBlackKingAllowed(true);
        state.setCastlingBlackQueenAllowed(true);
        state.setHalfMoveClock(2);
        state.setFullMoveClock(5);


        PiecePositioned origen = PiecePositioned.of(Square.a8, Piece.ROOK_BLACK);
        PiecePositioned destino = PiecePositioned.of(Square.a1, Piece.ROOK_WHITE);

        moveExecutor = moveFactoryImp.createCaptureRookMove(origen, destino, Cardinal.Sur);

        moveExecutor.doMove(state);

        assertEquals(Color.WHITE, state.getCurrentTurn());
        assertTrue(state.isCastlingWhiteKingAllowed());
        assertFalse(state.isCastlingWhiteQueenAllowed());
        assertTrue(state.isCastlingBlackKingAllowed());
        assertFalse(state.isCastlingBlackQueenAllowed());
        assertEquals(0, state.getHalfMoveClock());
        assertEquals(6, state.getFullMoveClock());
    }


    @Test
    public void testCapturaTorreByTorre02() {
        state.setCurrentTurn(Color.BLACK);
        state.setCastlingWhiteKingAllowed(true);
        state.setCastlingWhiteQueenAllowed(true);
        state.setCastlingBlackKingAllowed(true);
        state.setCastlingBlackQueenAllowed(true);
        state.setHalfMoveClock(2);
        state.setFullMoveClock(5);


        PiecePositioned origen = PiecePositioned.of(Square.h8, Piece.ROOK_BLACK);
        PiecePositioned destino = PiecePositioned.of(Square.h1, Piece.ROOK_WHITE);

        moveExecutor = moveFactoryImp.createCaptureRookMove(origen, destino, Cardinal.Sur);

        moveExecutor.doMove(state);

        assertEquals(Color.WHITE, state.getCurrentTurn());
        assertFalse(state.isCastlingWhiteKingAllowed());
        assertTrue(state.isCastlingWhiteQueenAllowed());
        assertFalse(state.isCastlingBlackKingAllowed());
        assertTrue(state.isCastlingBlackQueenAllowed());
        assertEquals(0, state.getHalfMoveClock());
        assertEquals(6, state.getFullMoveClock());
    }
}
