package net.chesstango.board.moves.imp;

import org.junit.Before;
import org.junit.Test;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.position.imp.PositionState;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;


/**
 * @author Mauricio Coria
 */
public class MoveFactoryWhiteTest {
    private MoveFactoryWhite moveFactoryImp;

    private Move moveExecutor;

    private PositionState positionState;

    @Before
    public void setUp() throws Exception {
        moveFactoryImp = new MoveFactoryWhite();
        positionState = new PositionState();
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

        moveExecutor.executeMove(positionState);

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

        moveExecutor.executeMove(positionState);

        assertEquals(Color.BLACK, positionState.getCurrentTurn());
        assertFalse(positionState.isCastlingWhiteKingAllowed());
        assertFalse(positionState.isCastlingWhiteQueenAllowed());
        assertTrue(positionState.isCastlingBlackKingAllowed());
        assertTrue(positionState.isCastlingBlackQueenAllowed());
        assertEquals(0, positionState.getHalfMoveClock());
        assertEquals(5, positionState.getFullMoveClock());
    }

    @Test
    public void testCapturaTorreByKingMovePierdeEnroque1() {
        positionState.setCurrentTurn(Color.WHITE);
        positionState.setCastlingWhiteKingAllowed(false);
        positionState.setCastlingWhiteQueenAllowed(false);
        positionState.setCastlingBlackKingAllowed(true);
        positionState.setCastlingBlackQueenAllowed(true);
        positionState.setHalfMoveClock(2);
        positionState.setFullMoveClock(5);

        PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.g7, Piece.KING_WHITE);
        PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.h8, Piece.ROOK_BLACK);

        moveExecutor = moveFactoryImp.createCaptureKingMove(origen, destino);

        moveExecutor.executeMove(positionState);

        assertEquals(Color.BLACK, positionState.getCurrentTurn());
        assertFalse(positionState.isCastlingWhiteKingAllowed());
        assertFalse(positionState.isCastlingWhiteQueenAllowed());
        assertFalse(positionState.isCastlingBlackKingAllowed());
        assertTrue(positionState.isCastlingBlackQueenAllowed());
        assertEquals(0, positionState.getHalfMoveClock());
        assertEquals(5, positionState.getFullMoveClock());
    }

    @Test
    public void testCapturaTorreByKingMovePierdeEnroque2() {
        positionState.setCurrentTurn(Color.WHITE);
        positionState.setCastlingWhiteKingAllowed(false);
        positionState.setCastlingWhiteQueenAllowed(false);
        positionState.setCastlingBlackKingAllowed(true);
        positionState.setCastlingBlackQueenAllowed(true);
        positionState.setHalfMoveClock(2);
        positionState.setFullMoveClock(5);

        PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.b7, Piece.KING_WHITE);
        PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.a8, Piece.ROOK_BLACK);

        moveExecutor = moveFactoryImp.createCaptureKingMove(origen, destino);

        moveExecutor.executeMove(positionState);

        assertEquals(Color.BLACK, positionState.getCurrentTurn());
        assertFalse(positionState.isCastlingWhiteKingAllowed());
        assertFalse(positionState.isCastlingWhiteQueenAllowed());
        assertTrue(positionState.isCastlingBlackKingAllowed());
        assertFalse(positionState.isCastlingBlackQueenAllowed());
        assertEquals(0, positionState.getHalfMoveClock());
        assertEquals(5, positionState.getFullMoveClock());
    }

    @Test
    public void testCapturaTorreByKingMovePierdeEnroque3() {
        positionState.setCurrentTurn(Color.WHITE);
        positionState.setCastlingWhiteKingAllowed(false);
        positionState.setCastlingWhiteQueenAllowed(false);
        positionState.setCastlingBlackKingAllowed(true);
        positionState.setCastlingBlackQueenAllowed(true);
        positionState.setHalfMoveClock(2);
        positionState.setFullMoveClock(5);

        PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.g7, Piece.QUEEN_WHITE);
        PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.h8, Piece.ROOK_BLACK);

        moveExecutor = moveFactoryImp.createCaptureMove(origen, destino);

        moveExecutor.executeMove(positionState);

        assertEquals(Color.BLACK, positionState.getCurrentTurn());
        assertFalse(positionState.isCastlingWhiteKingAllowed());
        assertFalse(positionState.isCastlingWhiteQueenAllowed());
        assertFalse(positionState.isCastlingBlackKingAllowed());
        assertTrue(positionState.isCastlingBlackQueenAllowed());
        assertEquals(0, positionState.getHalfMoveClock());
        assertEquals(5, positionState.getFullMoveClock());
    }

    @Test
    public void testCapturaTorreByKingMovePierdeEnroque4() {
        positionState.setCurrentTurn(Color.WHITE);
        positionState.setCastlingWhiteKingAllowed(false);
        positionState.setCastlingWhiteQueenAllowed(false);
        positionState.setCastlingBlackKingAllowed(true);
        positionState.setCastlingBlackQueenAllowed(true);
        positionState.setHalfMoveClock(2);
        positionState.setFullMoveClock(5);

        PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.b7, Piece.QUEEN_WHITE);
        PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.a8, Piece.ROOK_BLACK);

        moveExecutor = moveFactoryImp.createCaptureMove(origen, destino);

        moveExecutor.executeMove(positionState);

        assertEquals(Color.BLACK, positionState.getCurrentTurn());
        assertFalse(positionState.isCastlingWhiteKingAllowed());
        assertFalse(positionState.isCastlingWhiteQueenAllowed());
        assertTrue(positionState.isCastlingBlackKingAllowed());
        assertFalse(positionState.isCastlingBlackQueenAllowed());
        assertEquals(0, positionState.getHalfMoveClock());
        assertEquals(5, positionState.getFullMoveClock());
    }

    @Test
    public void testCapturaTorreByKingMovePierdeEnroque5() {
        positionState.setCurrentTurn(Color.WHITE);
        positionState.setCastlingWhiteKingAllowed(false);
        positionState.setCastlingWhiteQueenAllowed(false);
        positionState.setCastlingBlackKingAllowed(true);
        positionState.setCastlingBlackQueenAllowed(true);
        positionState.setHalfMoveClock(2);
        positionState.setFullMoveClock(5);

        PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.g7, Piece.PAWN_WHITE);
        PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.h8, Piece.ROOK_BLACK);

        moveExecutor = moveFactoryImp.createCapturePawnPromotion(origen, destino, Piece.ROOK_WHITE);

        moveExecutor.executeMove(positionState);

        assertEquals(Color.BLACK, positionState.getCurrentTurn());
        assertFalse(positionState.isCastlingWhiteKingAllowed());
        assertFalse(positionState.isCastlingWhiteQueenAllowed());
        assertFalse(positionState.isCastlingBlackKingAllowed());
        assertTrue(positionState.isCastlingBlackQueenAllowed());
        assertEquals(0, positionState.getHalfMoveClock());
        assertEquals(5, positionState.getFullMoveClock());
    }

    @Test
    public void testCapturaTorreByKingMovePierdeEnroque6() {
        positionState.setCurrentTurn(Color.WHITE);
        positionState.setCastlingWhiteKingAllowed(false);
        positionState.setCastlingWhiteQueenAllowed(false);
        positionState.setCastlingBlackKingAllowed(true);
        positionState.setCastlingBlackQueenAllowed(true);
        positionState.setHalfMoveClock(2);
        positionState.setFullMoveClock(5);

        PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.b7, Piece.PAWN_WHITE);
        PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.a8, Piece.ROOK_BLACK);

        moveExecutor = moveFactoryImp.createCapturePawnPromotion(origen, destino, Piece.ROOK_WHITE);

        moveExecutor.executeMove(positionState);

        assertEquals(Color.BLACK, positionState.getCurrentTurn());
        assertFalse(positionState.isCastlingWhiteKingAllowed());
        assertFalse(positionState.isCastlingWhiteQueenAllowed());
        assertTrue(positionState.isCastlingBlackKingAllowed());
        assertFalse(positionState.isCastlingBlackQueenAllowed());
        assertEquals(0, positionState.getHalfMoveClock());
        assertEquals(5, positionState.getFullMoveClock());
    }
}
