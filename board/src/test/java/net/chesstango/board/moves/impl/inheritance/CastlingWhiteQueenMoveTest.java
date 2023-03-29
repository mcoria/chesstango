package net.chesstango.board.moves.impl.inheritance;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.debug.chess.ColorBoardDebug;
import net.chesstango.board.movesgenerators.legal.MoveFilter;
import net.chesstango.board.position.ChessPosition;
import net.chesstango.board.position.Board;
import net.chesstango.board.position.imp.*;
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
public class CastlingWhiteQueenMoveTest {

    private Board board;

    private PositionState positionState;

    private KingCacheBoard kingCacheBoard;

    private ColorBoard colorBoard;

    private CastlingWhiteQueenMove moveExecutor;

    private ZobristHash zobristHash;

    @Mock
    private ChessPosition chessPosition;

    @Mock
    private MoveFilter filter;

    @Before
    public void setUp() throws Exception {
        moveExecutor = new CastlingWhiteQueenMove();

        positionState = new PositionState();
        positionState.setCurrentTurn(Color.WHITE);
        positionState.setCastlingWhiteQueenAllowed(true);
        positionState.setCastlingWhiteKingAllowed(false);
        positionState.setHalfMoveClock(3);
        positionState.setFullMoveClock(10);

        board = new ArrayPiecePlacement();
        board.setPieza(Square.a1, Piece.ROOK_WHITE);
        board.setPieza(Square.e1, Piece.KING_WHITE);

        kingCacheBoard = new KingCacheBoard();
        colorBoard = new ColorBoardDebug();
        colorBoard.init(board);

        zobristHash = new ZobristHash();
        zobristHash.init(board, positionState);
    }

    @Test
    public void testPosicionPiezaBoard() {
        moveExecutor.executeMove(board);

        assertEquals(Piece.KING_WHITE, board.getPiece(Square.c1));
        assertEquals(Piece.ROOK_WHITE, board.getPiece(Square.d1));

        assertTrue(board.isEmpty(Square.a1));
        assertTrue(board.isEmpty(Square.e1));

        moveExecutor.undoMove(board);

        assertEquals(Piece.KING_WHITE, board.getPiece(Square.e1));
        assertEquals(Piece.ROOK_WHITE, board.getPiece(Square.a1));

        assertTrue(board.isEmpty(Square.c1));
        assertTrue(board.isEmpty(Square.d1));
    }

    @Test
    public void testBoardState() {
        moveExecutor.executeMove(positionState);

        assertNull(positionState.getEnPassantSquare());
        assertEquals(Color.BLACK, positionState.getCurrentTurn());
        assertFalse(positionState.isCastlingWhiteQueenAllowed());
        assertFalse(positionState.isCastlingWhiteKingAllowed());
        assertEquals(4, positionState.getHalfMoveClock());
        assertEquals(10, positionState.getFullMoveClock());

        moveExecutor.undoMove(positionState);

        assertNull(positionState.getEnPassantSquare());
        assertEquals(Color.WHITE, positionState.getCurrentTurn());
        assertTrue(positionState.isCastlingWhiteQueenAllowed());
        assertFalse(positionState.isCastlingWhiteKingAllowed());
        assertEquals(3, positionState.getHalfMoveClock());
        assertEquals(10, positionState.getFullMoveClock());

    }

    @Test
    public void testColorBoard() {
        // execute
        moveExecutor.executeMove(colorBoard);

        // asserts execute
        assertEquals(Color.WHITE, colorBoard.getColor(Square.c1));
        assertEquals(Color.WHITE, colorBoard.getColor(Square.d1));

        assertTrue(colorBoard.isEmpty(Square.a1));
        assertTrue(colorBoard.isEmpty(Square.e1));

        // undos
        moveExecutor.undoMove(colorBoard);

        // asserts undos
        assertEquals(Color.WHITE, colorBoard.getColor(Square.e1));
        assertEquals(Color.WHITE, colorBoard.getColor(Square.a1));

        assertTrue(colorBoard.isEmpty(Square.c1));
        assertTrue(colorBoard.isEmpty(Square.d1));
    }

    @Test
    public void testKingCacheBoard() {
        moveExecutor.executeMove(kingCacheBoard);

        assertEquals(Square.c1, kingCacheBoard.getSquareKingWhiteCache());

        moveExecutor.undoMove(kingCacheBoard);

        assertEquals(Square.e1, kingCacheBoard.getSquareKingWhiteCache());
    }

    @Test
    public void testBoard() {
        // execute
        moveExecutor.executeMove(chessPosition);

        // asserts execute
        verify(chessPosition).executeMoveKing(moveExecutor);

        // undos
        moveExecutor.undoMove(chessPosition);


        // asserts undos
        verify(chessPosition).undoMoveKing(moveExecutor);
    }

}
