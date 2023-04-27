package net.chesstango.board.moves.impl.inheritance;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.debug.chess.ColorBoardDebug;
import net.chesstango.board.debug.chess.KingCacheBoardDebug;
import net.chesstango.board.debug.chess.PositionStateDebug;
import net.chesstango.board.movesgenerators.legal.MoveFilter;
import net.chesstango.board.position.Board;
import net.chesstango.board.position.ChessPosition;
import net.chesstango.board.position.imp.ArrayBoard;
import net.chesstango.board.position.imp.ZobristHash;
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
public class CastlingWhiteKingMoveTest {

    private Board board;

    private PositionStateDebug positionState;

    private CastlingWhiteKingMove moveExecutor;

    private KingCacheBoardDebug kingCacheBoard;

    private ColorBoardDebug colorBoard;

    private ZobristHash zobristHash;

    @Mock
    private ChessPosition chessPosition;

    @Mock
    private MoveFilter filter;

    @BeforeEach
    public void setUp() throws Exception {
        moveExecutor = new CastlingWhiteKingMove();

        positionState = new PositionStateDebug();
        positionState.setCurrentTurn(Color.WHITE);
        positionState.setCastlingWhiteQueenAllowed(false);
        positionState.setCastlingWhiteKingAllowed(true);
        positionState.setHalfMoveClock(3);
        positionState.setFullMoveClock(10);

        board = new ArrayBoard();
        board.setPieza(Square.e1, Piece.KING_WHITE);
        board.setPieza(Square.h1, Piece.ROOK_WHITE);

        kingCacheBoard = new KingCacheBoardDebug();
        kingCacheBoard.init(board);

        colorBoard = new ColorBoardDebug();
        colorBoard.init(board);

        zobristHash = new ZobristHash();
        zobristHash.init(board, positionState);
    }


    @Test
    public void testPosicionPiezaBoard() {
        moveExecutor.executeMove(board);

        assertEquals(Piece.KING_WHITE, board.getPiece(Square.g1));
        assertEquals(Piece.ROOK_WHITE, board.getPiece(Square.f1));

        assertTrue(board.isEmpty(Square.e1));
        assertTrue(board.isEmpty(Square.h1));

        moveExecutor.undoMove(board);

        assertEquals(Piece.KING_WHITE, board.getPiece(Square.e1));
        assertEquals(Piece.ROOK_WHITE, board.getPiece(Square.h1));

        assertTrue(board.isEmpty(Square.g1));
        assertTrue(board.isEmpty(Square.f1));
    }

    @Test
    public void testBoardState() {
        // execute
        moveExecutor.executeMove(positionState);

        // asserts execute
        assertNull(positionState.getEnPassantSquare());
        assertEquals(Color.BLACK, positionState.getCurrentTurn());
        assertFalse(positionState.isCastlingWhiteQueenAllowed());
        assertFalse(positionState.isCastlingWhiteKingAllowed());
        assertEquals(4, positionState.getHalfMoveClock());
        assertEquals(10, positionState.getFullMoveClock());
        positionState.validar();

        // undos
        moveExecutor.undoMove(positionState);

        // asserts undos
        assertNull(positionState.getEnPassantSquare());
        assertEquals(Color.WHITE, positionState.getCurrentTurn());
        assertFalse(positionState.isCastlingWhiteQueenAllowed());
        assertTrue(positionState.isCastlingWhiteKingAllowed());
        assertEquals(3, positionState.getHalfMoveClock());
        assertEquals(10, positionState.getFullMoveClock());
        positionState.validar();

    }

    @Test
    public void testColorBoard() {
        // execute
        moveExecutor.executeMove(colorBoard);

        // asserts execute
        assertEquals(Color.WHITE, colorBoard.getColor(Square.g1));
        assertEquals(Color.WHITE, colorBoard.getColor(Square.f1));

        assertTrue(colorBoard.isEmpty(Square.e1));
        assertTrue(colorBoard.isEmpty(Square.h8));


        // undos
        moveExecutor.undoMove(colorBoard);

        // asserts undos
        assertEquals(Color.WHITE, colorBoard.getColor(Square.e1));
        assertEquals(Color.WHITE, colorBoard.getColor(Square.h1));

        assertTrue(colorBoard.isEmpty(Square.g1));
        assertTrue(colorBoard.isEmpty(Square.f1));
    }

    @Test
    public void testKingCacheBoard() {
        moveExecutor.executeMove(kingCacheBoard);

        assertEquals(Square.g1, kingCacheBoard.getSquareKingWhiteCache());

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

    @Test
    public void testIntegrated() {
        // execute
        moveExecutor.executeMove(board);
        moveExecutor.executeMove(positionState);
        moveExecutor.executeMove(colorBoard);
        moveExecutor.executeMove(kingCacheBoard);

        // asserts execute
        colorBoard.validar(board);
        positionState.validar(board);
        kingCacheBoard.validar(board);

        // undos
        moveExecutor.undoMove(board);
        moveExecutor.undoMove(positionState);
        moveExecutor.undoMove(colorBoard);
        moveExecutor.undoMove(kingCacheBoard);


        // asserts undos
        colorBoard.validar(board);
        positionState.validar(board);
        kingCacheBoard.validar(board);
    }
}
