package net.chesstango.board.moves.impl.inheritance;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.debug.chess.ColorBoardDebug;
import net.chesstango.board.debug.chess.KingCacheBoardDebug;
import net.chesstango.board.debug.chess.PositionStateDebug;
import net.chesstango.board.moves.impl.inheritance.CastlingWhiteKingMove;
import net.chesstango.board.movesgenerators.legal.MoveFilter;
import net.chesstango.board.position.ChessPosition;
import net.chesstango.board.position.PiecePlacement;
import net.chesstango.board.position.imp.ArrayPiecePlacement;
import net.chesstango.board.position.imp.ZobristHash;
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
public class CastlingWhiteKingMoveTest {

    private PiecePlacement piecePlacement;

    private PositionStateDebug positionState;

    private CastlingWhiteKingMove moveExecutor;

    private KingCacheBoardDebug kingCacheBoard;

    private ColorBoardDebug colorBoard;

    private ZobristHash zobristHash;

    @Mock
    private ChessPosition chessPosition;

    @Mock
    private MoveFilter filter;

    @Before
    public void setUp() throws Exception {
        moveExecutor = new CastlingWhiteKingMove();

        positionState = new PositionStateDebug();
        positionState.setCurrentTurn(Color.WHITE);
        positionState.setCastlingWhiteQueenAllowed(false);
        positionState.setCastlingWhiteKingAllowed(true);
        positionState.setHalfMoveClock(3);
        positionState.setFullMoveClock(10);

        piecePlacement = new ArrayPiecePlacement();
        piecePlacement.setPieza(Square.e1, Piece.KING_WHITE);
        piecePlacement.setPieza(Square.h1, Piece.ROOK_WHITE);

        kingCacheBoard = new KingCacheBoardDebug();
        kingCacheBoard.init(piecePlacement);

        colorBoard = new ColorBoardDebug();
        colorBoard.init(piecePlacement);

        zobristHash = new ZobristHash();
        zobristHash.init(piecePlacement, positionState);
    }


    @Test
    public void testPosicionPiezaBoard() {
        moveExecutor.executeMove(piecePlacement);

        assertEquals(Piece.KING_WHITE, piecePlacement.getPiece(Square.g1));
        assertEquals(Piece.ROOK_WHITE, piecePlacement.getPiece(Square.f1));

        assertTrue(piecePlacement.isEmpty(Square.e1));
        assertTrue(piecePlacement.isEmpty(Square.h1));

        moveExecutor.undoMove(piecePlacement);

        assertEquals(Piece.KING_WHITE, piecePlacement.getPiece(Square.e1));
        assertEquals(Piece.ROOK_WHITE, piecePlacement.getPiece(Square.h1));

        assertTrue(piecePlacement.isEmpty(Square.g1));
        assertTrue(piecePlacement.isEmpty(Square.f1));
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
        moveExecutor.executeMove(piecePlacement);
        moveExecutor.executeMove(positionState);
        moveExecutor.executeMove(colorBoard);
        moveExecutor.executeMove(kingCacheBoard);

        // asserts execute
        colorBoard.validar(piecePlacement);
        positionState.validar(piecePlacement);
        kingCacheBoard.validar(piecePlacement);

        // undos
        moveExecutor.undoMove(piecePlacement);
        moveExecutor.undoMove(positionState);
        moveExecutor.undoMove(colorBoard);
        moveExecutor.undoMove(kingCacheBoard);


        // asserts undos
        colorBoard.validar(piecePlacement);
        positionState.validar(piecePlacement);
        kingCacheBoard.validar(piecePlacement);
    }
}
