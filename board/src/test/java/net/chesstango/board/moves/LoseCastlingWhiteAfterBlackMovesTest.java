package net.chesstango.board.moves;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.factory.SingletonMoveFactories;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.position.imp.PositionStateImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author Mauricio Coria
 */
public class LoseCastlingWhiteAfterBlackMovesTest {
    private MoveFactory moveFactoryImp;

    private Move moveExecutor;

    private PositionStateImp positionState;

    @BeforeEach
    public void setUp() throws Exception {
        moveFactoryImp = SingletonMoveFactories.getDefaultMoveFactoryBlack();
        positionState = new PositionStateImp();
        moveExecutor = null;
    }

    @Test
    public void testCapturaTorreByKing1() {
        positionState.setCurrentTurn(Color.BLACK);
        positionState.setCastlingWhiteKingAllowed(true);
        positionState.setCastlingWhiteQueenAllowed(true);
        positionState.setCastlingBlackKingAllowed(false);
        positionState.setCastlingBlackQueenAllowed(false);
        positionState.setHalfMoveClock(2);
        positionState.setFullMoveClock(5);

        PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.g2, Piece.KING_BLACK);
        PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.h1, Piece.ROOK_WHITE);

        moveExecutor = moveFactoryImp.createCaptureKingMove(origen, destino);

        moveExecutor.executeMove(positionState);

        assertEquals(Color.WHITE, positionState.getCurrentTurn());
        assertFalse(positionState.isCastlingWhiteKingAllowed());
        assertTrue(positionState.isCastlingWhiteQueenAllowed());
        assertFalse(positionState.isCastlingBlackKingAllowed());
        assertFalse(positionState.isCastlingBlackQueenAllowed());
        assertEquals(0, positionState.getHalfMoveClock());
        assertEquals(6, positionState.getFullMoveClock());
    }

    @Test
    public void testCapturaTorreByKing2() {
        positionState.setCurrentTurn(Color.BLACK);
        positionState.setCastlingWhiteKingAllowed(true);
        positionState.setCastlingWhiteQueenAllowed(true);
        positionState.setCastlingBlackKingAllowed(false);
        positionState.setCastlingBlackQueenAllowed(false);
        positionState.setHalfMoveClock(2);
        positionState.setFullMoveClock(5);

        PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.b2, Piece.KING_BLACK);
        PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.a1, Piece.ROOK_WHITE);

        moveExecutor = moveFactoryImp.createCaptureKingMove(origen, destino);

        moveExecutor.executeMove(positionState);

        assertEquals(Color.WHITE, positionState.getCurrentTurn());
        assertTrue(positionState.isCastlingWhiteKingAllowed());
        assertFalse(positionState.isCastlingWhiteQueenAllowed());
        assertFalse(positionState.isCastlingBlackKingAllowed());
        assertFalse(positionState.isCastlingBlackQueenAllowed());
        assertEquals(0, positionState.getHalfMoveClock());
        assertEquals(6, positionState.getFullMoveClock());
    }

    @Test
    public void testCapturaTorreByQueen1() {
        positionState.setCurrentTurn(Color.BLACK);
        positionState.setCastlingWhiteKingAllowed(true);
        positionState.setCastlingWhiteQueenAllowed(true);
        positionState.setCastlingBlackKingAllowed(false);
        positionState.setCastlingBlackQueenAllowed(false);
        positionState.setHalfMoveClock(2);
        positionState.setFullMoveClock(5);

        PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.g2, Piece.QUEEN_BLACK);
        PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.h1, Piece.ROOK_WHITE);

        moveExecutor = moveFactoryImp.createCaptureMove(origen, destino);

        moveExecutor.executeMove(positionState);

        assertEquals(Color.WHITE, positionState.getCurrentTurn());
        assertFalse(positionState.isCastlingWhiteKingAllowed());
        assertTrue(positionState.isCastlingWhiteQueenAllowed());
        assertFalse(positionState.isCastlingBlackKingAllowed());
        assertFalse(positionState.isCastlingBlackQueenAllowed());
        assertEquals(0, positionState.getHalfMoveClock());
        assertEquals(6, positionState.getFullMoveClock());
    }

    @Test
    public void testCapturaTorreByQueen2() {
        positionState.setCurrentTurn(Color.BLACK);
        positionState.setCastlingWhiteKingAllowed(true);
        positionState.setCastlingWhiteQueenAllowed(true);
        positionState.setCastlingBlackKingAllowed(false);
        positionState.setCastlingBlackQueenAllowed(false);
        positionState.setHalfMoveClock(2);
        positionState.setFullMoveClock(5);

        PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.b2, Piece.QUEEN_BLACK);
        PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.a1, Piece.ROOK_WHITE);

        moveExecutor = moveFactoryImp.createCaptureMove(origen, destino);

        moveExecutor.executeMove(positionState);

        assertEquals(Color.WHITE, positionState.getCurrentTurn());
        assertTrue(positionState.isCastlingWhiteKingAllowed());
        assertFalse(positionState.isCastlingWhiteQueenAllowed());
        assertFalse(positionState.isCastlingBlackKingAllowed());
        assertFalse(positionState.isCastlingBlackQueenAllowed());
        assertEquals(0, positionState.getHalfMoveClock());
        assertEquals(6, positionState.getFullMoveClock());
    }

    @Test
    public void testCapturaTorreByPawnPromotion1() {
        positionState.setCurrentTurn(Color.BLACK);
        positionState.setCastlingWhiteKingAllowed(true);
        positionState.setCastlingWhiteQueenAllowed(true);
        positionState.setCastlingBlackKingAllowed(false);
        positionState.setCastlingBlackQueenAllowed(false);
        positionState.setHalfMoveClock(2);
        positionState.setFullMoveClock(5);

        PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.g2, Piece.PAWN_BLACK);
        PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.h1, Piece.ROOK_WHITE);

        moveExecutor = moveFactoryImp.createCapturePromotionPawnMove(origen, destino, Piece.ROOK_BLACK, Cardinal.SurEste);

        moveExecutor.executeMove(positionState);

        assertEquals(Color.WHITE, positionState.getCurrentTurn());
        assertFalse(positionState.isCastlingWhiteKingAllowed());
        assertTrue(positionState.isCastlingWhiteQueenAllowed());
        assertFalse(positionState.isCastlingBlackKingAllowed());
        assertFalse(positionState.isCastlingBlackQueenAllowed());
        assertEquals(0, positionState.getHalfMoveClock());
        assertEquals(6, positionState.getFullMoveClock());
    }

    @Test
    public void testCapturaTorreByPawnPromotion2() {
        positionState.setCurrentTurn(Color.BLACK);
        positionState.setCastlingWhiteKingAllowed(true);
        positionState.setCastlingWhiteQueenAllowed(true);
        positionState.setCastlingBlackKingAllowed(false);
        positionState.setCastlingBlackQueenAllowed(false);
        positionState.setHalfMoveClock(2);
        positionState.setFullMoveClock(5);

        PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.b2, Piece.PAWN_BLACK);
        PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.a1, Piece.ROOK_WHITE);

        moveExecutor = moveFactoryImp.createCapturePromotionPawnMove(origen, destino, Piece.ROOK_BLACK, Cardinal.SurOeste);

        moveExecutor.executeMove(positionState);

        assertEquals(Color.WHITE, positionState.getCurrentTurn());
        assertTrue(positionState.isCastlingWhiteKingAllowed());
        assertFalse(positionState.isCastlingWhiteQueenAllowed());
        assertFalse(positionState.isCastlingBlackKingAllowed());
        assertFalse(positionState.isCastlingBlackQueenAllowed());
        assertEquals(0, positionState.getHalfMoveClock());
        assertEquals(6, positionState.getFullMoveClock());
    }


    @Test
    public void testCapturaTorreByTorre01() {
        positionState.setCurrentTurn(Color.BLACK);
        positionState.setCastlingWhiteKingAllowed(true);
        positionState.setCastlingWhiteQueenAllowed(true);
        positionState.setCastlingBlackKingAllowed(true);
        positionState.setCastlingBlackQueenAllowed(true);
        positionState.setHalfMoveClock(2);
        positionState.setFullMoveClock(5);


        PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.a8, Piece.ROOK_BLACK);
        PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.a1, Piece.ROOK_WHITE);

        moveExecutor = moveFactoryImp.createCaptureRookMove(origen, destino, Cardinal.Sur);

        moveExecutor.executeMove(positionState);

        assertEquals(Color.WHITE, positionState.getCurrentTurn());
        assertTrue(positionState.isCastlingWhiteKingAllowed());
        assertFalse(positionState.isCastlingWhiteQueenAllowed());
        assertTrue(positionState.isCastlingBlackKingAllowed());
        assertFalse(positionState.isCastlingBlackQueenAllowed());
        assertEquals(0, positionState.getHalfMoveClock());
        assertEquals(6, positionState.getFullMoveClock());
    }


    @Test
    public void testCapturaTorreByTorre02() {
        positionState.setCurrentTurn(Color.BLACK);
        positionState.setCastlingWhiteKingAllowed(true);
        positionState.setCastlingWhiteQueenAllowed(true);
        positionState.setCastlingBlackKingAllowed(true);
        positionState.setCastlingBlackQueenAllowed(true);
        positionState.setHalfMoveClock(2);
        positionState.setFullMoveClock(5);


        PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.h8, Piece.ROOK_BLACK);
        PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.h1, Piece.ROOK_WHITE);

        moveExecutor = moveFactoryImp.createCaptureRookMove(origen, destino, Cardinal.Sur);

        moveExecutor.executeMove(positionState);

        assertEquals(Color.WHITE, positionState.getCurrentTurn());
        assertFalse(positionState.isCastlingWhiteKingAllowed());
        assertTrue(positionState.isCastlingWhiteQueenAllowed());
        assertFalse(positionState.isCastlingBlackKingAllowed());
        assertTrue(positionState.isCastlingBlackQueenAllowed());
        assertEquals(0, positionState.getHalfMoveClock());
        assertEquals(6, positionState.getFullMoveClock());
    }
}
