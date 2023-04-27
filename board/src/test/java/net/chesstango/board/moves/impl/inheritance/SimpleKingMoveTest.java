package net.chesstango.board.moves.impl.inheritance;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.debug.chess.ColorBoardDebug;
import net.chesstango.board.debug.chess.KingCacheBoardDebug;
import net.chesstango.board.movesgenerators.legal.MoveFilter;
import net.chesstango.board.position.Board;
import net.chesstango.board.position.ChessPosition;
import net.chesstango.board.position.imp.ArrayBoard;
import net.chesstango.board.position.imp.PositionState;
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
public class SimpleKingMoveTest {

    private SimpleKingMove moveExecutor;

    private Board board;

    private PositionState positionState;

    private KingCacheBoardDebug kingCacheBoard;

    private ColorBoardDebug colorBoard;

    private ZobristHash zobristHash;

    @Mock
    private ChessPosition chessPosition;

    @Mock
    private MoveFilter filter;

    @BeforeEach
    public void setUp() throws Exception {
        board = new ArrayBoard();
        board.setPieza(Square.e1, Piece.KING_WHITE);

        colorBoard = new ColorBoardDebug();
        colorBoard.init(board);

        kingCacheBoard = new KingCacheBoardDebug();
        kingCacheBoard.init(board);

        PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.e1, Piece.KING_WHITE);
        PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.e2, null);

        moveExecutor = new SimpleKingMove(origen, destino);

        positionState = new PositionState();
        positionState.setCurrentTurn(Color.WHITE);
        positionState.setCastlingWhiteKingAllowed(true);
        positionState.setCastlingWhiteQueenAllowed(true);
        positionState.setHalfMoveClock(2);
        positionState.setFullMoveClock(5);

        zobristHash = new ZobristHash();
        zobristHash.init(board, positionState);
    }


    @Test
    public void testPosicionPiezaBoard() {
        // execute
        moveExecutor.executeMove(board);

        // asserts execute
        assertEquals(Piece.KING_WHITE, board.getPiece(Square.e2));
        assertNull(board.getPiece(Square.e1));

        // undos
        moveExecutor.undoMove(board);

        // asserts undos
        assertEquals(Piece.KING_WHITE, board.getPiece(Square.e1));
        assertTrue(board.isEmpty(Square.e2));
    }

    @Test
    public void testPositionState() {
        moveExecutor.executeMove(positionState);

        assertEquals(Color.BLACK, positionState.getCurrentTurn());
        assertEquals(3, positionState.getHalfMoveClock());
        assertEquals(5, positionState.getFullMoveClock());
        assertEquals(false, positionState.isCastlingWhiteKingAllowed());
        assertEquals(false, positionState.isCastlingWhiteQueenAllowed());

        moveExecutor.undoMove(positionState);

        assertEquals(Color.WHITE, positionState.getCurrentTurn());
        assertEquals(2, positionState.getHalfMoveClock());
        assertEquals(5, positionState.getFullMoveClock());
        assertEquals(true, positionState.isCastlingWhiteKingAllowed());
        assertEquals(true, positionState.isCastlingWhiteQueenAllowed());
    }

    @Test
    public void testKingCacheBoard() {
        moveExecutor.executeMove(kingCacheBoard);

        assertEquals(Square.e2, kingCacheBoard.getSquareKingWhiteCache());

        moveExecutor.undoMove(kingCacheBoard);

        assertEquals(Square.e1, kingCacheBoard.getSquareKingWhiteCache());
    }

    @Test
    public void testColorBoard() {
        // execute
        moveExecutor.executeMove(colorBoard);

        // asserts execute
        assertEquals(Color.WHITE, colorBoard.getColor(Square.e2));
        assertTrue(colorBoard.isEmpty(Square.e1));

        // undos
        moveExecutor.undoMove(colorBoard);


        // asserts undos
        assertEquals(Color.WHITE, colorBoard.getColor(Square.e1));
        assertTrue(colorBoard.isEmpty(Square.e2));
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
    public void testFilter() {
        // execute
        moveExecutor.filter(filter);

        // asserts execute
        verify(filter).filterMoveKing(moveExecutor);
    }

    @Test
    public void testIntegrated() {
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

        colorBoard.validar(board);
        kingCacheBoard.validar(board);

        // undos
        moveExecutor.undoMove(board);
        moveExecutor.undoMove(kingCacheBoard);
        moveExecutor.undoMove(positionState);
        moveExecutor.undoMove(colorBoard);


        // asserts undos
        assertEquals(Piece.KING_WHITE, board.getPiece(Square.e1));
        assertTrue(board.isEmpty(Square.e2));

        assertEquals(Square.e1, kingCacheBoard.getSquareKingWhiteCache());

        assertEquals(Color.WHITE, positionState.getCurrentTurn());

        assertEquals(Color.WHITE, colorBoard.getColor(Square.e1));
        assertTrue(colorBoard.isEmpty(Square.e2));

        colorBoard.validar(board);
        kingCacheBoard.validar(board);
    }
}
