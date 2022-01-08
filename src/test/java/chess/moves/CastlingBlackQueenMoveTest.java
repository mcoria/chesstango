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

import chess.Board;
import chess.BoardState;
import chess.Color;
import chess.Pieza;
import chess.Square;
import chess.layers.ColorBoard;
import chess.layers.KingCacheBoard;
import chess.layers.PosicionPiezaBoard;
import chess.layers.imp.ArrayPosicionPiezaBoard;
import chess.pseudomovesfilters.MoveFilter;


/**
 * @author Mauricio Coria
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class CastlingBlackQueenMoveTest {
	
	
	private PosicionPiezaBoard piezaBoard;
	
	private BoardState boardState;
	
	private KingCacheBoard kingCacheBoard;
	
	private ColorBoard colorBoard;	
	
	private CastlingBlackQueenMove moveExecutor;

	@Mock
	private Board board;
	
	@Mock
	private MoveFilter filter;	
	
	@Before
	public void setUp() throws Exception {
		moveExecutor = new CastlingBlackQueenMove();
		
		boardState = new BoardState();		
		boardState.setTurnoActual(Color.WHITE);
		boardState.setCastlingBlackReinaPermitido(true);
		boardState.setCastlingBlackKingPermitido(true);
		
		piezaBoard = new ArrayPosicionPiezaBoard();
		piezaBoard.setPieza(Square.a8, Pieza.TORRE_BLACK);	
		piezaBoard.setPieza(Square.e8, Pieza.KING_BLACK);	
		
		kingCacheBoard = new KingCacheBoard();
		colorBoard = new ColorBoard(piezaBoard);
	}
	
	@Test
	public void testPosicionPiezaBoard() {
		moveExecutor.executeMove(piezaBoard);
		
		assertEquals(Pieza.KING_BLACK, piezaBoard.getPieza(Square.c8));		
		assertEquals(Pieza.TORRE_BLACK, piezaBoard.getPieza(Square.d8));
		
		assertTrue(piezaBoard.isEmtpy(Square.a8));
		assertTrue(piezaBoard.isEmtpy(Square.e8));
		
		moveExecutor.undoMove(piezaBoard);
		
		assertEquals(Pieza.KING_BLACK, piezaBoard.getPieza(Square.e8));
		assertEquals(Pieza.TORRE_BLACK, piezaBoard.getPieza(Square.a8));
		
		assertTrue(piezaBoard.isEmtpy(Square.c8));
		assertTrue(piezaBoard.isEmtpy(Square.d8));		
	}

	@Test
	public void testBoardState() {
		moveExecutor.executeMove(boardState);		

		assertNull(boardState.getPeonPasanteSquare());
		assertEquals(Color.BLACK, boardState.getTurnoActual());		
		assertFalse(boardState.isCastlingBlackReinaPermitido());
		assertFalse(boardState.isCastlingBlackKingPermitido());
		
		moveExecutor.undoMove(boardState);
		
		assertNull(boardState.getPeonPasanteSquare());
		assertEquals(Color.WHITE, boardState.getTurnoActual());		
		assertTrue(boardState.isCastlingBlackReinaPermitido());
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

		assertEquals(Square.c8, kingCacheBoard.getSquareKingNegroCache());

		moveExecutor.undoMove(kingCacheBoard);

		assertEquals(Square.e8, kingCacheBoard.getSquareKingNegroCache());
	}
	
	@Test
	public void testBoard() {
		// execute
		moveExecutor.executeMove(board);

		// asserts execute
		verify(board).executeKingMove(moveExecutor);

		// undos
		moveExecutor.undoMove(board);

		
		// asserts undos
		verify(board).undoKingMove(moveExecutor);
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
