package chess.moveexecutors;

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
import chess.movecalculators.MoveFilter;
import chess.moveexecutors.EnroqueBlancoReynaMove;


/**
 * @author Mauricio Coria
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class EnroqueBlancoReynaMoveTest {	
	
	private PosicionPiezaBoard piezaBoard;
	
	private BoardState boardState;
	
	private KingCacheBoard kingCacheBoard;
	
	private ColorBoard colorBoard;	
	
	private EnroqueBlancoReynaMove moveExecutor;

	@Mock
	private Board board;
	
	@Mock
	private MoveFilter filter;	
	
	@Before
	public void setUp() throws Exception {
		moveExecutor = new EnroqueBlancoReynaMove();
		
		boardState = new BoardState();		
		boardState.setTurnoActual(Color.BLANCO);
		boardState.setEnroqueBlancoReinaPermitido(true);
		boardState.setEnroqueBlancoReyPermitido(true);
		
		piezaBoard = new ArrayPosicionPiezaBoard();
		piezaBoard.setPieza(Square.a1, Pieza.TORRE_BLANCO);	
		piezaBoard.setPieza(Square.e1, Pieza.REY_BLANCO);	
		
		kingCacheBoard = new KingCacheBoard();
		colorBoard = new ColorBoard(piezaBoard);
	}
	
	@Test
	public void testPosicionPiezaBoard() {
		moveExecutor.executeMove(piezaBoard);
		
		assertEquals(Pieza.REY_BLANCO, piezaBoard.getPieza(Square.c1));		
		assertEquals(Pieza.TORRE_BLANCO, piezaBoard.getPieza(Square.d1));
		
		assertTrue(piezaBoard.isEmtpy(Square.a1));
		assertTrue(piezaBoard.isEmtpy(Square.e1));
		
		moveExecutor.undoMove(piezaBoard);
		
		assertEquals(Pieza.REY_BLANCO, piezaBoard.getPieza(Square.e1));
		assertEquals(Pieza.TORRE_BLANCO, piezaBoard.getPieza(Square.a1));
		
		assertTrue(piezaBoard.isEmtpy(Square.c1));
		assertTrue(piezaBoard.isEmtpy(Square.d1));		
	}

	@Test
	public void testBoardState() {
		moveExecutor.executeMove(boardState);		

		assertNull(boardState.getPeonPasanteSquare());
		assertEquals(Color.NEGRO, boardState.getTurnoActual());		
		assertFalse(boardState.isEnroqueBlancoReinaPermitido());
		assertFalse(boardState.isEnroqueBlancoReyPermitido());
		
		moveExecutor.undoMove(boardState);
		
		assertNull(boardState.getPeonPasanteSquare());
		assertEquals(Color.BLANCO, boardState.getTurnoActual());		
		assertTrue(boardState.isEnroqueBlancoReinaPermitido());
		assertTrue(boardState.isEnroqueBlancoReyPermitido());		
		
	}	
	
	@Test
	public void testColorBoard() {
		// execute
		moveExecutor.executeMove(colorBoard);

		// asserts execute
		assertEquals(Color.BLANCO, colorBoard.getColor(Square.c1));
		assertEquals(Color.BLANCO, colorBoard.getColor(Square.d1));
		
		assertTrue(colorBoard.isEmpty(Square.a1));
		assertTrue(colorBoard.isEmpty(Square.e1));

		// undos
		moveExecutor.undoMove(colorBoard);

		// asserts undos
		assertEquals(Color.BLANCO, colorBoard.getColor(Square.e1));
		assertEquals(Color.BLANCO, colorBoard.getColor(Square.a1));
		
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
