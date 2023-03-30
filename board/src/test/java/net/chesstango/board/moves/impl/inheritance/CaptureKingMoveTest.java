package net.chesstango.board.moves.impl.inheritance;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.movesgenerators.legal.MoveFilter;
import net.chesstango.board.position.ChessPosition;
import net.chesstango.board.position.Board;
import net.chesstango.board.position.imp.ArrayBoard;
import net.chesstango.board.position.imp.ColorBoard;
import net.chesstango.board.position.imp.KingCacheBoard;
import net.chesstango.board.position.imp.PositionState;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;


/**
 * @author Mauricio Coria
 */
@RunWith(MockitoJUnitRunner.class)
public class CaptureKingMoveTest {

    private CaptureKingMove moveExecutor;

    private Board board;

    private PositionState positionState;

    private KingCacheBoard kingCacheBoard;

    private ColorBoard colorBoard;

    @Mock
    private ChessPosition chessPosition;

    @Mock
    private MoveFilter filter;

    @Before
    public void setUp() throws Exception {
        positionState = new PositionState();
        kingCacheBoard = new KingCacheBoard();
        moveExecutor = null;
        board = null;
    }


    @Test
    public void testPosicionPiezaBoard() {
        board = new ArrayBoard();
        board.setPieza(Square.e1, Piece.KING_WHITE);

        PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.e1, Piece.KING_WHITE);
        PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.e2, Piece.KNIGHT_BLACK);

        moveExecutor = new CaptureKingMove(origen, destino);

        // execute
        moveExecutor.executeMove(board);

        // asserts execute
        assertEquals(Piece.KING_WHITE, board.getPiece(Square.e2));
        assertNull(board.getPiece(Square.e1));

        // undos
        moveExecutor.undoMove(board);

        // asserts undos
        assertEquals(Piece.KING_WHITE, board.getPiece(Square.e1));
        assertEquals(Piece.KNIGHT_BLACK, board.getPiece(Square.e2));
    }

    @Test
    public void testBoardState() {
        positionState.setCurrentTurn(Color.WHITE);
        positionState.setHalfMoveClock(2);
        positionState.setFullMoveClock(5);

        PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.e1, Piece.KING_WHITE);
        PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.e2, Piece.KNIGHT_BLACK);

        moveExecutor = new CaptureKingMove(origen, destino);

        moveExecutor.executeMove(positionState);
        assertEquals(0, positionState.getHalfMoveClock());
        assertEquals(5, positionState.getFullMoveClock());

        assertEquals(Color.BLACK, positionState.getCurrentTurn());

        moveExecutor.undoMove(positionState);

        assertEquals(Color.WHITE, positionState.getCurrentTurn());
        assertEquals(2, positionState.getHalfMoveClock());
        assertEquals(5, positionState.getFullMoveClock());
    }

    @Test
    public void testKingCacheBoard() {
        kingCacheBoard.setKingSquare(Color.WHITE, Square.d2);

        PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.e1, Piece.KING_WHITE);
        PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.e2, Piece.KNIGHT_BLACK);

        moveExecutor = new CaptureKingMove(origen, destino);

        moveExecutor.executeMove(kingCacheBoard);

        assertEquals(Square.e2, kingCacheBoard.getSquareKingWhiteCache());

        moveExecutor.undoMove(kingCacheBoard);

        assertEquals(Square.e1, kingCacheBoard.getSquareKingWhiteCache());
    }

    @Test
    public void testColorBoard() {
        board = new ArrayBoard();
        board.setPieza(Square.e1, Piece.KING_WHITE);

        colorBoard = new ColorBoard();
        colorBoard.init(board);

        PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.e1, Piece.KING_WHITE);
        PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.e2, Piece.KNIGHT_BLACK);

        moveExecutor = new CaptureKingMove(origen, destino);

        // execute
        moveExecutor.executeMove(colorBoard);

        // asserts execute
        assertEquals(Color.WHITE, colorBoard.getColor(Square.e2));
        assertTrue(colorBoard.isEmpty(Square.e1));

        // undos
        moveExecutor.undoMove(colorBoard);

        // asserts undos
        assertEquals(Color.WHITE, colorBoard.getColor(Square.e1));
        assertEquals(Color.BLACK, colorBoard.getColor(Square.e2));
    }

    @Test
    public void testBoard() {
        PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.e1, Piece.KING_WHITE);
        PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.e2, Piece.KNIGHT_BLACK);

        moveExecutor = new CaptureKingMove(origen, destino);

        // execute
        moveExecutor.executeMove(chessPosition);

        // asserts execute
        verify(chessPosition).executeMoveKing(moveExecutor);

        // undos
        moveExecutor.undoMove(chessPosition);


        // asserts undos
        verify(chessPosition).undoMoveKing(moveExecutor);
    }


    @Test
    public void testFilter() {
        PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.e1, Piece.KING_WHITE);
        PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.e2, Piece.KNIGHT_BLACK);

        moveExecutor = new CaptureKingMove(origen, destino);

        // execute
        moveExecutor.filter(filter);

        // asserts execute
        verify(filter).filterMoveKing(moveExecutor);
    }

    @Test
    public void testExecuteUndo() {
        board = new ArrayBoard();
        board.setPieza(Square.e1, Piece.KING_WHITE);

        colorBoard = new ColorBoard();
        colorBoard.init(board);

        positionState.setCurrentTurn(Color.WHITE);

        PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.e1, Piece.KING_WHITE);
        PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.e2, Piece.KNIGHT_BLACK);

        moveExecutor = new CaptureKingMove(origen, destino);

        // execute
        moveExecutor.executeMove(board);
        moveExecutor.executeMove(kingCacheBoard);
        moveExecutor.executeMove(positionState);
        moveExecutor.executeMove(colorBoard);

        // asserts execute
        assertEquals(Piece.KING_WHITE, board.getPiece(Square.e2));
        assertNull(board.getPiece(Square.e1));

        assertEquals(Square.e2, kingCacheBoard.getSquareKingWhiteCache());

        assertEquals(Color.BLACK, positionState.getCurrentTurn());

        assertEquals(Color.WHITE, colorBoard.getColor(Square.e2));
        assertTrue(colorBoard.isEmpty(Square.e1));

        // undos
        moveExecutor.undoMove(board);
        moveExecutor.undoMove(kingCacheBoard);
        moveExecutor.undoMove(positionState);
        moveExecutor.undoMove(colorBoard);


        // asserts undos
        assertEquals(Piece.KING_WHITE, board.getPiece(Square.e1));
        assertEquals(Piece.KNIGHT_BLACK, board.getPiece(Square.e2));

        assertEquals(Square.e1, kingCacheBoard.getSquareKingWhiteCache());

        assertEquals(Color.WHITE, positionState.getCurrentTurn());

        assertEquals(Color.WHITE, colorBoard.getColor(Square.e1));
        assertEquals(Color.BLACK, colorBoard.getColor(Square.e2));
    }
}
