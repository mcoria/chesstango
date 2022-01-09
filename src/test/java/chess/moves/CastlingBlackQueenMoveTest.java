package chess.moves;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import chess.ChessPosition;
import chess.BoardState;
import chess.Color;
import chess.Piece;
import chess.Square;
import chess.layers.ColorBoard;
import chess.layers.KingCacheBoard;
import chess.layers.PiecePlacement;
import chess.layers.imp.ArrayPiecePlacement;
import chess.pseudomovesfilters.MoveFilter;


/**
 * @author Mauricio Coria
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class CastlingBlackQueenMoveTest {
	
	
	private PiecePlacement piezaBoard;
	
	private BoardState boardState;
	
	private KingCacheBoard kingCacheBoard;
	
	private ColorBoard colorBoard;	
	
	private CastlingBlackQueenMove moveExecutor;

	@Mock
	private ChessPosition chessPosition;
	
	@Mock
	private MoveFilter filter;	
	
	@Before
	public void setUp() throws Exception {
		moveExecutor = new CastlingBlackQueenMove();
		
		boardState = new BoardState();		
		boardState.setTurnoActual(Color.WHITE);
		boardState.setCastlingBlackQueenPermitido(true);
		boardState.setCastlingBlackKingPermitido(true);
		
		piezaBoard = new ArrayPiecePlacement();
		piezaBoard.setPieza(Square.a8, Piece.ROOK_BLACK);	
		piezaBoard.setPieza(Square.e8, Piece.KING_BLACK);	
		
		kingCacheBoard = new KingCacheBoard();
		colorBoard = new ColorBoard(piezaBoard);
	}
	
	@Test
	public void testPosicionPiezaBoard() {
		moveExecutor.executeMove(piezaBoard);
		
		assertEquals(Piece.KING_BLACK, piezaBoard.getPieza(Square.c8));		
		assertEquals(Piece.ROOK_BLACK, piezaBoard.getPieza(Square.d8));
		
		assertTrue(piezaBoard.isEmtpy(Square.a8));
		assertTrue(piezaBoard.isEmtpy(Square.e8));
		
		moveExecutor.undoMove(piezaBoard);
		
		assertEquals(Piece.KING_BLACK, piezaBoard.getPieza(Square.e8));
		assertEquals(Piece.ROOK_BLACK, piezaBoard.getPieza(Square.a8));
		
		assertTrue(piezaBoard.isEmtpy(Square.c8));
		assertTrue(piezaBoard.isEmtpy(Square.d8));		
	}

	@Test
	public void testBoardState() {
		moveExecutor.executeMove(boardState);		

		assertNull(boardState.getPawnPasanteSquare());
		assertEquals(Color.BLACK, boardState.getTurnoActual());		
		assertFalse(boardState.isCastlingBlackQueenPermitido());
		assertFalse(boardState.isCastlingBlackKingPermitido());
		
		moveExecutor.undoMove(boardState);
		
		assertNull(boardState.getPawnPasanteSquare());
		assertEquals(Color.WHITE, boardState.getTurnoActual());		
		assertTrue(boardState.isCastlingBlackQueenPermitido());
		assertTrue(boardState.isCastlingBlackKingPermitido());		
		
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
		verify(chessPosition).executeKingMove(moveExecutor);

		// undos
		moveExecutor.undoMove(chessPosition);

		
		// asserts undos
		verify(chessPosition).undoKingMove(moveExecutor);
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
