package net.chesstango.board.moves.impl.inheritance;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveFactory;
import net.chesstango.board.position.Board;
import net.chesstango.board.position.imp.ArrayBoard;
import net.chesstango.board.position.imp.PositionState;
import net.chesstango.board.position.imp.ZobristHash;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author Mauricio Coria
 */
public class MoveFactoryBlackTest {
    private MoveFactory moveFactoryImp;

    private Move moveExecutor;

    private PositionState positionState;

    private Board board;

    private ZobristHash zobristHash;

    @BeforeEach
    public void setUp() throws Exception {
        moveFactoryImp = new MoveFactoryBlack();
        positionState = new PositionState();
        board = new ArrayBoard();
        zobristHash = new ZobristHash();
        moveExecutor = null;
    }

    @Test
    public void testSimpleKingMovePierdeEnroque() {
        positionState.setCurrentTurn(Color.BLACK);
        positionState.setCastlingWhiteKingAllowed(true);
        positionState.setCastlingWhiteQueenAllowed(true);
        positionState.setCastlingBlackKingAllowed(true);
        positionState.setCastlingBlackQueenAllowed(true);
        positionState.setHalfMoveClock(2);
        positionState.setFullMoveClock(5);

        PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.e8, Piece.KING_BLACK);
        PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.e7, null);

        moveExecutor = moveFactoryImp.createSimpleKingMove(origen, destino);

        moveExecutor.executeMove(positionState);

        assertEquals(Color.WHITE, positionState.getCurrentTurn());
        assertTrue(positionState.isCastlingWhiteKingAllowed());
        assertTrue(positionState.isCastlingWhiteQueenAllowed());
        assertFalse(positionState.isCastlingBlackKingAllowed());
        assertFalse(positionState.isCastlingBlackQueenAllowed());
        assertEquals(3, positionState.getHalfMoveClock());
        assertEquals(6, positionState.getFullMoveClock());
    }

    @Test
    public void testCapturaKingMovePierdeEnroque() {
        positionState.setCurrentTurn(Color.BLACK);
        positionState.setCastlingWhiteKingAllowed(true);
        positionState.setCastlingWhiteQueenAllowed(true);
        positionState.setCastlingBlackKingAllowed(true);
        positionState.setCastlingBlackQueenAllowed(true);
        positionState.setHalfMoveClock(2);
        positionState.setFullMoveClock(5);

        PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.e8, Piece.KING_WHITE);
        PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.e7, Piece.KNIGHT_BLACK);

        moveExecutor = moveFactoryImp.createCaptureKingMove(origen, destino);

        moveExecutor.executeMove(positionState);

        assertEquals(Color.WHITE, positionState.getCurrentTurn());
        assertTrue(positionState.isCastlingWhiteKingAllowed());
        assertTrue(positionState.isCastlingWhiteQueenAllowed());
        assertFalse(positionState.isCastlingBlackKingAllowed());
        assertFalse(positionState.isCastlingBlackQueenAllowed());
        assertEquals(0, positionState.getHalfMoveClock());
        assertEquals(6, positionState.getFullMoveClock());
    }

    @Test
    public void testCapturaTorreByKingMovePierdeEnroque1() {
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
    public void testCapturaTorreByKingMovePierdeEnroque2() {
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
    public void testCapturaTorreByKingMovePierdeEnroque3() {
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
    public void testCapturaTorreByKingMovePierdeEnroque4() {
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
    public void testCapturaTorreByKingMovePierdeEnroque5() {
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
    public void testCapturaTorreByKingMovePierdeEnroque6() {
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

}
