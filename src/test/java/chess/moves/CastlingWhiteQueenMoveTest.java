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
public class CastlingWhiteQueenMoveTest {	
	
	private PosicionPiezaBoard piezaBoard;
	
	private BoardState boardState;
	
	private KingCacheBoard kingCacheBoard;
	
	private ColorBoard colorBoard;	
	
	private CastlingWhiteQueenMove moveExecutor;

	@Mock
	private Board board;
	
	@Mock
	private MoveFilter filter;	
	
	@Before
	public void setUp() throws Exception {
		moveExecutor = new CastlingWhiteQueenMove();
		
		boardState = new BoardState();		
		boardState.setTurnoActual(Color.WHITE);
		boardState.setCastlingWhiteReinaPermitido(true);
		boardState.setCastlingWhiteKingPermitido(true);
		
		piezaBoard = new ArrayPosicionPiezaBoard();
		piezaBoard.setPieza(Square.a1, Pieza.TORRE_WHITE);	
		piezaBoard.setPieza(Square.e1, Pieza.KING_WHITE);	
		
		kingCacheBoard = new KingCacheBoard();
		colorBoard = new ColorBoard(piezaBoard);
	}
	
	@Test
	public void testPosicionPiezaBoard() {
		moveExecutor.executeMove(piezaBoard);
		
		assertEquals(Pieza.KING_WHITE, piezaBoard.getPieza(Square.c1));		
		assertEquals(Pieza.TORRE_WHITE, piezaBoard.getPieza(Square.d1));
		
		assertTrue(piezaBoard.isEmtpy(Square.a1));
		assertTrue(piezaBoard.isEmtpy(Square.e1));
		
		moveExecutor.undoMove(piezaBoard);
		
		assertEquals(Pieza.KING_WHITE, piezaBoard.getPieza(Square.e1));
		assertEquals(Pieza.TORRE_WHITE, piezaBoard.getPieza(Square.a1));
		
		assertTrue(piezaBoard.isEmtpy(Square.c1));
		assertTrue(piezaBoard.isEmtpy(Square.d1));		
	}

	@Test
	public void testBoardState() {
		moveExecutor.executeMove(boardState);		

		assertNull(boardState.getPeonPasanteSquare());
		assertEquals(Color.BLACK, boardState.getTurnoActual());		
		assertFalse(boardState.isCastlingWhiteReinaPermitido());
		assertFalse(boardState.isCastlingWhiteKingPermitido());
		
		moveExecutor.undoMove(boardState);
		
		assertNull(boardState.getPeonPasanteSquare());
		assertEquals(Color.WHITE, boardState.getTurnoActual());		
		assertTrue(boardState.isCastlingWhiteReinaPermitido());
		assertTrue(boardState.isCastlingWhiteKingPermitido());		
		
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

		assertEquals(Square.c1, kingCacheBoard.getSquareKingBlancoCache());

		moveExecutor.undoMove(kingCacheBoard);

		assertEquals(Square.e1, kingCacheBoard.getSquareKingBlancoCache());
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
