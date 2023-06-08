package net.chesstango.board.moves.impl.inheritance;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.debug.chess.ColorBoardDebug;
import net.chesstango.board.movesgenerators.legal.MoveFilter;
import net.chesstango.board.position.Board;
import net.chesstango.board.position.ChessPosition;
import net.chesstango.board.position.imp.ArrayBoard;
import net.chesstango.board.position.imp.PositionState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;


/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class CapturePawnEnPassantTest {

    private Board board;

    private PositionState positionState;

    private CapturePawnEnPassant moveExecutor;

    private ColorBoardDebug colorBoard;

    @Mock
    private ChessPosition chessPosition;

    @Mock
    private MoveFilter filter;

    @BeforeEach
    public void setUp() throws Exception {
        positionState = new PositionState();
        positionState.setCurrentTurn(Color.WHITE);
        positionState.setEnPassantSquare(Square.a6);
        positionState.setHalfMoveClock(2);
        positionState.setFullMoveClock(5);

        board = new ArrayBoard();
        board.setPiece(Square.b5, Piece.PAWN_WHITE);
        board.setPiece(Square.a5, Piece.PAWN_BLACK);

        colorBoard = new ColorBoardDebug();
        colorBoard.init(board);

        PiecePositioned pawnWhite = PiecePositioned.getPiecePositioned(Square.b5, Piece.PAWN_WHITE);
        PiecePositioned pawnBlack = PiecePositioned.getPiecePositioned(Square.a5, Piece.PAWN_BLACK);
        PiecePositioned pawnPasanteSquare = PiecePositioned.getPiecePositioned(Square.a6, null);

        moveExecutor = new CapturePawnEnPassant(pawnWhite, pawnPasanteSquare, pawnBlack);
    }

    @Test
    public void testPosicionPiezaBoard() {
        // execute
        moveExecutor.executeMove(board);

        // asserts execute
        assertTrue(board.isEmpty(Square.a5));
        assertTrue(board.isEmpty(Square.b5));
        assertEquals(Piece.PAWN_WHITE, board.getPiece(Square.a6));

        // undos
        moveExecutor.undoMove(board);

        // asserts undos
        assertTrue(board.isEmpty(Square.a6));
        assertEquals(Piece.PAWN_WHITE, board.getPiece(Square.b5));
        assertEquals(Piece.PAWN_BLACK, board.getPiece(Square.a5));

    }

    @Test
    public void testMoveState() {
        // execute
        moveExecutor.executeMove(positionState);

        // asserts execute
        assertNull(positionState.getEnPassantSquare());
        assertEquals(Color.BLACK, positionState.getCurrentTurn());
        assertEquals(0, positionState.getHalfMoveClock());
        assertEquals(5, positionState.getFullMoveClock());

        // undos
        moveExecutor.undoMove(positionState);

        // asserts undos
        assertEquals(Square.a6, positionState.getEnPassantSquare());
        assertEquals(Color.WHITE, positionState.getCurrentTurn());
        assertEquals(2, positionState.getHalfMoveClock());
        assertEquals(5, positionState.getFullMoveClock());

    }

    @Test
    public void testColorBoard() {
        // execute
        moveExecutor.executeMove(colorBoard);

        // asserts execute
        assertEquals(Color.WHITE, colorBoard.getColor(Square.a6));
        assertTrue(colorBoard.isEmpty(Square.a5));
        assertTrue(colorBoard.isEmpty(Square.b5));

        // undos
        moveExecutor.undoMove(colorBoard);

        // asserts undos
        assertTrue(colorBoard.isEmpty(Square.a6));
        assertEquals(Color.BLACK, colorBoard.getColor(Square.a5));
        assertEquals(Color.WHITE, colorBoard.getColor(Square.b5));

    }

    @Test
    public void testBoard() {
        // execute
        moveExecutor.executeMove(chessPosition);

        // asserts execute
        verify(chessPosition).executeMove(moveExecutor);

        // undos
        moveExecutor.undoMove(chessPosition);


        // asserts undos
        verify(chessPosition).undoMove(moveExecutor);
    }


    @Test
    public void testFilter() {
        // execute
        moveExecutor.filter(filter);

        // asserts execute
        verify(filter).filterMove(moveExecutor);
    }

    @Test
    public void testIntegrated() {
        // execute
        moveExecutor.executeMove(board);
        moveExecutor.executeMove(positionState);
        moveExecutor.executeMove(colorBoard);

        // asserts execute
        assertTrue(board.isEmpty(Square.a5));
        assertTrue(board.isEmpty(Square.b5));
        assertEquals(Piece.PAWN_WHITE, board.getPiece(Square.a6));

        assertNull(positionState.getEnPassantSquare());
        assertEquals(Color.BLACK, positionState.getCurrentTurn());

        assertEquals(Color.WHITE, colorBoard.getColor(Square.a6));
        assertTrue(colorBoard.isEmpty(Square.a5));
        assertTrue(colorBoard.isEmpty(Square.b5));
        colorBoard.validar(board);

        // undos
        moveExecutor.undoMove(board);
        moveExecutor.undoMove(positionState);
        moveExecutor.undoMove(colorBoard);


        // asserts undos
        assertTrue(board.isEmpty(Square.a6));
        assertEquals(Piece.PAWN_WHITE, board.getPiece(Square.b5));
        assertEquals(Piece.PAWN_BLACK, board.getPiece(Square.a5));

        assertEquals(Square.a6, positionState.getEnPassantSquare());
        assertEquals(Color.WHITE, positionState.getCurrentTurn());

        assertTrue(colorBoard.isEmpty(Square.a6));
        assertEquals(Color.BLACK, colorBoard.getColor(Square.a5));
        assertEquals(Color.WHITE, colorBoard.getColor(Square.b5));
        colorBoard.validar(board);
    }
}
