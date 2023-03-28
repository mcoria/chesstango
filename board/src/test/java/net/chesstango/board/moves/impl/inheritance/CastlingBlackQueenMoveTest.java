package net.chesstango.board.moves.impl.inheritance;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.debug.chess.ColorBoardDebug;
import net.chesstango.board.moves.impl.inheritance.CastlingBlackQueenMove;
import net.chesstango.board.movesgenerators.legal.MoveFilter;
import net.chesstango.board.position.ChessPosition;
import net.chesstango.board.position.PiecePlacement;
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
public class CastlingBlackQueenMoveTest {

    private PiecePlacement piecePlacement;

    private PositionState positionState;

    private KingCacheBoard kingCacheBoard;

    private ColorBoard colorBoard;

    private CastlingBlackQueenMove moveExecutor;

    private ZobristHash zobristHash;

    @Mock
    private ChessPosition chessPosition;

    @Mock
    private MoveFilter filter;

    @Before
    public void setUp() throws Exception {
        moveExecutor = new CastlingBlackQueenMove();

        positionState = new PositionState();
        positionState.setCurrentTurn(Color.BLACK);
        positionState.setCastlingBlackQueenAllowed(true);
        positionState.setCastlingBlackKingAllowed(false);
        positionState.setHalfMoveClock(3);
        positionState.setFullMoveClock(10);

        piecePlacement = new ArrayPiecePlacement();
        piecePlacement.setPieza(Square.a8, Piece.ROOK_BLACK);
        piecePlacement.setPieza(Square.e8, Piece.KING_BLACK);

        kingCacheBoard = new KingCacheBoard();
        colorBoard = new ColorBoardDebug();
        colorBoard.init(piecePlacement);

        zobristHash = new ZobristHash();
        zobristHash.init(piecePlacement, positionState);
    }

    @Test
    public void testPosicionPiezaBoard() {
        moveExecutor.executeMove(piecePlacement);

        assertEquals(Piece.KING_BLACK, piecePlacement.getPiece(Square.c8));
        assertEquals(Piece.ROOK_BLACK, piecePlacement.getPiece(Square.d8));

        assertTrue(piecePlacement.isEmpty(Square.a8));
        assertTrue(piecePlacement.isEmpty(Square.e8));

        moveExecutor.undoMove(piecePlacement);

        assertEquals(Piece.KING_BLACK, piecePlacement.getPiece(Square.e8));
        assertEquals(Piece.ROOK_BLACK, piecePlacement.getPiece(Square.a8));

        assertTrue(piecePlacement.isEmpty(Square.c8));
        assertTrue(piecePlacement.isEmpty(Square.d8));
    }

    @Test
    public void testBoardState() {
        moveExecutor.executeMove(positionState);

        assertNull(positionState.getEnPassantSquare());
        assertEquals(Color.WHITE, positionState.getCurrentTurn());
        assertFalse(positionState.isCastlingBlackQueenAllowed());
        assertFalse(positionState.isCastlingBlackKingAllowed());
        assertEquals(4, positionState.getHalfMoveClock());
        assertEquals(11, positionState.getFullMoveClock());

        moveExecutor.undoMove(positionState);

        assertNull(positionState.getEnPassantSquare());
        assertEquals(Color.BLACK, positionState.getCurrentTurn());
        assertTrue(positionState.isCastlingBlackQueenAllowed());
        assertFalse(positionState.isCastlingBlackKingAllowed());
        assertEquals(3, positionState.getHalfMoveClock());
        assertEquals(10, positionState.getFullMoveClock());

    }

    @Test
    public void testColorBoard() {
        // execute
        moveExecutor.executeMove(colorBoard);

        // asserts execute
        assertEquals(Color.BLACK, colorBoard.getColor(Square.c8));
        assertEquals(Color.BLACK, colorBoard.getColor(Square.d8));

        assertTrue(colorBoard.isEmpty(Square.a8));
        assertTrue(colorBoard.isEmpty(Square.e8));

        // undos
        moveExecutor.undoMove(colorBoard);

        // asserts undos
        assertEquals(Color.BLACK, colorBoard.getColor(Square.e8));
        assertEquals(Color.BLACK, colorBoard.getColor(Square.a8));

        assertTrue(colorBoard.isEmpty(Square.c8));
        assertTrue(colorBoard.isEmpty(Square.d8));
    }

    @Test
    public void testKingCacheBoard() {
        moveExecutor.executeMove(kingCacheBoard);

        assertEquals(Square.c8, kingCacheBoard.getSquareKingBlackCache());

        moveExecutor.undoMove(kingCacheBoard);

        assertEquals(Square.e8, kingCacheBoard.getSquareKingBlackCache());
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
    //TODO: Add test body
    public void testFilter() {
		/*
		// execute
		moveExecutor.filter(filter);

		// asserts execute
		verify(filter).filterKingMove(moveExecutor);
		*/
    }

}
