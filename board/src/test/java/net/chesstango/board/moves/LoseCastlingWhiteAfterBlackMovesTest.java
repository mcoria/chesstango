package net.chesstango.board.moves;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.factory.SingletonMoveFactories;
import net.chesstango.board.position.PiecePlacement;
import net.chesstango.board.position.PositionStateReader;
import net.chesstango.board.position.imp.ArrayPiecePlacement;
import net.chesstango.board.position.imp.PositionState;
import net.chesstango.board.position.imp.ZobristHash;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;


/**
 * @author Mauricio Coria
 */
public class LoseCastlingWhiteAfterBlackMovesTest {
    private MoveFactory moveFactoryImp;

    private Move moveExecutor;

    private PositionState positionState;

    private PiecePlacement piecePlacement;

    private ZobristHash zobristHash;

    @Before
    public void setUp() throws Exception {
        moveFactoryImp = SingletonMoveFactories.getDefaultMoveFactoryBlack();
        positionState = new PositionState();
        piecePlacement = new ArrayPiecePlacement();
        zobristHash = new ZobristHash();
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

        moveExecutor = moveFactoryImp.createCapturePawnPromotion(origen, destino, Piece.ROOK_BLACK);

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

        moveExecutor = moveFactoryImp.createCapturePawnPromotion(origen, destino, Piece.ROOK_BLACK);

        moveExecutor.executeMove(positionState);

        assertEquals(Color.WHITE, positionState.getCurrentTurn());
        assertTrue(positionState.isCastlingWhiteKingAllowed());
        assertFalse(positionState.isCastlingWhiteQueenAllowed());
        assertFalse(positionState.isCastlingBlackKingAllowed());
        assertFalse(positionState.isCastlingBlackQueenAllowed());
        assertEquals(0, positionState.getHalfMoveClock());
        assertEquals(6, positionState.getFullMoveClock());
    }

}
