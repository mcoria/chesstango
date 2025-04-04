package net.chesstango.board.internal.moves;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.internal.moves.factories.MoveFactoryWhite;
import net.chesstango.board.position.PositionState;
import net.chesstango.board.internal.position.PositionStateImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author Mauricio Coria
 */
public class LoseCastlingBlackAfterWhiteMovesTest {
    private MoveFactoryWhite moveFactoryImp;

    private MoveImp moveExecutor;

    private PositionState positionState;

    @BeforeEach
    public void setUp() throws Exception {
        moveFactoryImp = new MoveFactoryWhite();
        positionState = new PositionStateImp();
        moveExecutor = null;
    }

    @Test
    public void testCapturaTorreByKing1() {
        positionState.setCurrentTurn(Color.WHITE);
        positionState.setCastlingWhiteKingAllowed(false);
        positionState.setCastlingWhiteQueenAllowed(false);
        positionState.setCastlingBlackKingAllowed(true);
        positionState.setCastlingBlackQueenAllowed(true);
        positionState.setHalfMoveClock(2);
        positionState.setFullMoveClock(5);

        PiecePositioned origen = PiecePositioned.of(Square.g7, Piece.KING_WHITE);
        PiecePositioned destino = PiecePositioned.of(Square.h8, Piece.ROOK_BLACK);

        moveExecutor = moveFactoryImp.createCaptureKingMove(origen, destino);

        moveExecutor.doMove(positionState);

        assertEquals(Color.BLACK, positionState.getCurrentTurn());
        assertFalse(positionState.isCastlingWhiteKingAllowed());
        assertFalse(positionState.isCastlingWhiteQueenAllowed());
        assertFalse(positionState.isCastlingBlackKingAllowed());
        assertTrue(positionState.isCastlingBlackQueenAllowed());
        assertEquals(0, positionState.getHalfMoveClock());
        assertEquals(5, positionState.getFullMoveClock());
    }

    @Test
    public void testCapturaTorreByKing() {
        positionState.setCurrentTurn(Color.WHITE);
        positionState.setCastlingWhiteKingAllowed(false);
        positionState.setCastlingWhiteQueenAllowed(false);
        positionState.setCastlingBlackKingAllowed(true);
        positionState.setCastlingBlackQueenAllowed(true);
        positionState.setHalfMoveClock(2);
        positionState.setFullMoveClock(5);

        PiecePositioned origen = PiecePositioned.of(Square.b7, Piece.KING_WHITE);
        PiecePositioned destino = PiecePositioned.of(Square.a8, Piece.ROOK_BLACK);

        moveExecutor = moveFactoryImp.createCaptureKingMove(origen, destino);

        moveExecutor.doMove(positionState);

        assertEquals(Color.BLACK, positionState.getCurrentTurn());
        assertFalse(positionState.isCastlingWhiteKingAllowed());
        assertFalse(positionState.isCastlingWhiteQueenAllowed());
        assertTrue(positionState.isCastlingBlackKingAllowed());
        assertFalse(positionState.isCastlingBlackQueenAllowed());
        assertEquals(0, positionState.getHalfMoveClock());
        assertEquals(5, positionState.getFullMoveClock());
    }

    @Test
    public void testCapturaTorreByQueen1() {
        positionState.setCurrentTurn(Color.WHITE);
        positionState.setCastlingWhiteKingAllowed(false);
        positionState.setCastlingWhiteQueenAllowed(false);
        positionState.setCastlingBlackKingAllowed(true);
        positionState.setCastlingBlackQueenAllowed(true);
        positionState.setHalfMoveClock(2);
        positionState.setFullMoveClock(5);

        PiecePositioned origen = PiecePositioned.of(Square.g7, Piece.QUEEN_WHITE);
        PiecePositioned destino = PiecePositioned.of(Square.h8, Piece.ROOK_BLACK);

        moveExecutor = moveFactoryImp.createCaptureKnightMove(origen, destino);

        moveExecutor.doMove(positionState);

        assertEquals(Color.BLACK, positionState.getCurrentTurn());
        assertFalse(positionState.isCastlingWhiteKingAllowed());
        assertFalse(positionState.isCastlingWhiteQueenAllowed());
        assertFalse(positionState.isCastlingBlackKingAllowed());
        assertTrue(positionState.isCastlingBlackQueenAllowed());
        assertEquals(0, positionState.getHalfMoveClock());
        assertEquals(5, positionState.getFullMoveClock());
    }

    @Test
    public void testCapturaTorreByQueen2() {
        positionState.setCurrentTurn(Color.WHITE);
        positionState.setCastlingWhiteKingAllowed(false);
        positionState.setCastlingWhiteQueenAllowed(false);
        positionState.setCastlingBlackKingAllowed(true);
        positionState.setCastlingBlackQueenAllowed(true);
        positionState.setHalfMoveClock(2);
        positionState.setFullMoveClock(5);

        PiecePositioned origen = PiecePositioned.of(Square.b7, Piece.QUEEN_WHITE);
        PiecePositioned destino = PiecePositioned.of(Square.a8, Piece.ROOK_BLACK);

        moveExecutor = moveFactoryImp.createCaptureKnightMove(origen, destino);

        moveExecutor.doMove(positionState);

        assertEquals(Color.BLACK, positionState.getCurrentTurn());
        assertFalse(positionState.isCastlingWhiteKingAllowed());
        assertFalse(positionState.isCastlingWhiteQueenAllowed());
        assertTrue(positionState.isCastlingBlackKingAllowed());
        assertFalse(positionState.isCastlingBlackQueenAllowed());
        assertEquals(0, positionState.getHalfMoveClock());
        assertEquals(5, positionState.getFullMoveClock());
    }

    @Test
    public void testCapturaTorreByPawnPromotion1() {
        positionState.setCurrentTurn(Color.WHITE);
        positionState.setCastlingWhiteKingAllowed(false);
        positionState.setCastlingWhiteQueenAllowed(false);
        positionState.setCastlingBlackKingAllowed(true);
        positionState.setCastlingBlackQueenAllowed(true);
        positionState.setHalfMoveClock(2);
        positionState.setFullMoveClock(5);

        PiecePositioned origen = PiecePositioned.of(Square.g7, Piece.PAWN_WHITE);
        PiecePositioned destino = PiecePositioned.of(Square.h8, Piece.ROOK_BLACK);

        moveExecutor = moveFactoryImp.createCapturePromotionPawnMove(origen, destino, Piece.ROOK_WHITE, Cardinal.NorteEste);

        moveExecutor.doMove(positionState);

        assertEquals(Color.BLACK, positionState.getCurrentTurn());
        assertFalse(positionState.isCastlingWhiteKingAllowed());
        assertFalse(positionState.isCastlingWhiteQueenAllowed());
        assertFalse(positionState.isCastlingBlackKingAllowed());
        assertTrue(positionState.isCastlingBlackQueenAllowed());
        assertEquals(0, positionState.getHalfMoveClock());
        assertEquals(5, positionState.getFullMoveClock());
    }

    @Test
    public void testCapturaTorreByPawnPromotion2() {
        positionState.setCurrentTurn(Color.WHITE);
        positionState.setCastlingWhiteKingAllowed(false);
        positionState.setCastlingWhiteQueenAllowed(false);
        positionState.setCastlingBlackKingAllowed(true);
        positionState.setCastlingBlackQueenAllowed(true);
        positionState.setHalfMoveClock(2);
        positionState.setFullMoveClock(5);

        PiecePositioned origen = PiecePositioned.of(Square.b7, Piece.PAWN_WHITE);
        PiecePositioned destino = PiecePositioned.of(Square.a8, Piece.ROOK_BLACK);

        moveExecutor = moveFactoryImp.createCapturePromotionPawnMove(origen, destino, Piece.ROOK_WHITE, Cardinal.NorteOeste);

        moveExecutor.doMove(positionState);

        assertEquals(Color.BLACK, positionState.getCurrentTurn());
        assertFalse(positionState.isCastlingWhiteKingAllowed());
        assertFalse(positionState.isCastlingWhiteQueenAllowed());
        assertTrue(positionState.isCastlingBlackKingAllowed());
        assertFalse(positionState.isCastlingBlackQueenAllowed());
        assertEquals(0, positionState.getHalfMoveClock());
        assertEquals(5, positionState.getFullMoveClock());
    }

    @Test
    public void testCapturaTorreByTorre01() {
        positionState.setCurrentTurn(Color.WHITE);
        positionState.setCastlingWhiteKingAllowed(true);
        positionState.setCastlingWhiteQueenAllowed(true);
        positionState.setCastlingBlackKingAllowed(true);
        positionState.setCastlingBlackQueenAllowed(true);
        positionState.setHalfMoveClock(2);
        positionState.setFullMoveClock(5);


        PiecePositioned origen = PiecePositioned.of(Square.a1, Piece.ROOK_WHITE);
        PiecePositioned destino = PiecePositioned.of(Square.a8, Piece.ROOK_BLACK);

        moveExecutor = moveFactoryImp.createCaptureRookMove(origen, destino, Cardinal.Norte);

        moveExecutor.doMove(positionState);

        assertEquals(Color.BLACK, positionState.getCurrentTurn());
        assertTrue(positionState.isCastlingWhiteKingAllowed());
        assertFalse(positionState.isCastlingWhiteQueenAllowed());
        assertTrue(positionState.isCastlingBlackKingAllowed());
        assertFalse(positionState.isCastlingBlackQueenAllowed());
        assertEquals(0, positionState.getHalfMoveClock());
        assertEquals(5, positionState.getFullMoveClock());
    }


    @Test
    public void testCapturaTorreByTorre02() {
        positionState.setCurrentTurn(Color.WHITE);
        positionState.setCastlingWhiteKingAllowed(true);
        positionState.setCastlingWhiteQueenAllowed(true);
        positionState.setCastlingBlackKingAllowed(true);
        positionState.setCastlingBlackQueenAllowed(true);
        positionState.setHalfMoveClock(2);
        positionState.setFullMoveClock(5);


        PiecePositioned origen = PiecePositioned.of(Square.h1, Piece.ROOK_WHITE);
        PiecePositioned destino = PiecePositioned.of(Square.h8, Piece.ROOK_BLACK);

        moveExecutor = moveFactoryImp.createCaptureRookMove(origen, destino, Cardinal.Norte);

        moveExecutor.doMove(positionState);

        assertEquals(Color.BLACK, positionState.getCurrentTurn());
        assertFalse(positionState.isCastlingWhiteKingAllowed());
        assertTrue(positionState.isCastlingWhiteQueenAllowed());
        assertFalse(positionState.isCastlingBlackKingAllowed());
        assertTrue(positionState.isCastlingBlackQueenAllowed());
        assertEquals(0, positionState.getHalfMoveClock());
        assertEquals(5, positionState.getFullMoveClock());
    }
}
