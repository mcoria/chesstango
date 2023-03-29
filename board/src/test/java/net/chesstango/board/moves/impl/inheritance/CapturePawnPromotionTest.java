package net.chesstango.board.moves.impl.inheritance;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.debug.chess.ColorBoardDebug;
import net.chesstango.board.movesgenerators.legal.MoveFilter;
import net.chesstango.board.position.ChessPosition;
import net.chesstango.board.position.Board;
import net.chesstango.board.position.imp.ArrayPiecePlacement;
import net.chesstango.board.position.imp.ColorBoard;
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
public class CapturePawnPromotionTest {

    private Board board;

    private PositionState positionState;

    private CapturePawnPromotion moveExecutor;

    private ColorBoard colorBoard;

    @Mock
    private ChessPosition chessPosition;

    @Mock
    private MoveFilter filter;

    @Before
    public void setUp() throws Exception {
        positionState = new PositionState();
        positionState.setCurrentTurn(Color.WHITE);
        positionState.setHalfMoveClock(3);
        positionState.setFullMoveClock(5);

        board = new ArrayPiecePlacement();
        board.setPieza(Square.e7, Piece.PAWN_WHITE);
        board.setPieza(Square.f8, Piece.KNIGHT_BLACK);

        colorBoard = new ColorBoardDebug();
        colorBoard.init(board);

        PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.e7, Piece.PAWN_WHITE);
        PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.f8, Piece.KNIGHT_BLACK);

        moveExecutor = new CapturePawnPromotion(origen, destino, Piece.QUEEN_WHITE);
    }


    @Test
    public void testPosicionPiezaBoard() {
        // execute
        moveExecutor.executeMove(board);

        // asserts execute
        assertEquals(Piece.QUEEN_WHITE, board.getPiece(Square.f8));
        assertTrue(board.isEmpty(Square.e7));

        // undos
        moveExecutor.undoMove(board);

        // asserts undos
        assertEquals(Piece.PAWN_WHITE, board.getPiece(Square.e7));
        assertEquals(Piece.KNIGHT_BLACK, board.getPiece(Square.f8));
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
        assertEquals(Color.WHITE, positionState.getCurrentTurn());
        assertEquals(3, positionState.getHalfMoveClock());
        assertEquals(5, positionState.getFullMoveClock());
    }

    @Test
    public void testColorBoard() {
        // execute
        moveExecutor.executeMove(colorBoard);

        // asserts execute
        assertEquals(Color.WHITE, colorBoard.getColor(Square.f8));
        assertTrue(colorBoard.isEmpty(Square.e7));

        // undos
        moveExecutor.undoMove(colorBoard);

        // asserts undos
        assertEquals(Color.WHITE, colorBoard.getColor(Square.e7));
        assertEquals(Color.BLACK, colorBoard.getColor(Square.f8));
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
}
