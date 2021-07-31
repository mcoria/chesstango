package moveexecutor;

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
import layers.ColorBoard;
import layers.KingCacheBoard;
import layers.PosicionPiezaBoard;
import layers.imp.ArrayPosicionPiezaBoard;
import movecalculators.MoveFilter;
import moveexecutors.EnroqueBlancoReyMove;

@RunWith(MockitoJUnitRunner.class)
public class EnroqueBlancoReyMoveTest {
	
	private PosicionPiezaBoard piezaBoard;
	
	private BoardState boardState;	
	
	private EnroqueBlancoReyMove moveExecutor;
	
	private KingCacheBoard kingCacheBoard;
	
	private ColorBoard colorBoard;
	
	@Mock
	private Board board;
	
	@Mock
	private MoveFilter filter;	

	@Before
	public void setUp() throws Exception {
		moveExecutor = new EnroqueBlancoReyMove();
		
		boardState = new BoardState();		
		boardState.setTurnoActual(Color.BLANCO);
		boardState.setEnroqueBlancoReinaPermitido(true);
		boardState.setEnroqueBlancoReyPermitido(true);
		
		piezaBoard = new ArrayPosicionPiezaBoard();
		piezaBoard.setPieza(Square.e1, Pieza.REY_BLANCO);
		piezaBoard.setPieza(Square.h1, Pieza.TORRE_BLANCO);		
		
		kingCacheBoard = new KingCacheBoard();
		colorBoard = new ColorBoard(piezaBoard);
	}
	
	@Test
	public void testPosicionPiezaBoard() {
		moveExecutor.executeMove(piezaBoard);
		
		assertEquals(Pieza.REY_BLANCO, piezaBoard.getPieza(Square.g1));		
		assertEquals(Pieza.TORRE_BLANCO, piezaBoard.getPieza(Square.f1));
		
		assertTrue(piezaBoard.isEmtpy(Square.e1));
		assertTrue(piezaBoard.isEmtpy(Square.h1));
		
		moveExecutor.undoMove(piezaBoard);
		
		assertEquals(Pieza.REY_BLANCO, piezaBoard.getPieza(Square.e1));
		assertEquals(Pieza.TORRE_BLANCO, piezaBoard.getPieza(Square.h1));
		
		assertTrue(piezaBoard.isEmtpy(Square.g1));
		assertTrue(piezaBoard.isEmtpy(Square.f1));		
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
		assertEquals(Color.BLANCO, colorBoard.getColor(Square.g1));
		assertEquals(Color.BLANCO, colorBoard.getColor(Square.f1));
		
		assertTrue(colorBoard.isEmpty(Square.e1));
		assertTrue(colorBoard.isEmpty(Square.h8));


		// undos
		moveExecutor.undoMove(colorBoard);

		// asserts undos
		assertEquals(Color.BLANCO, colorBoard.getColor(Square.e1));
		assertEquals(Color.BLANCO, colorBoard.getColor(Square.h1));
		
		assertTrue(colorBoard.isEmpty(Square.g1));
		assertTrue(colorBoard.isEmpty(Square.f1));		
	}	
	
	@Test
	public void testKingCacheBoard() {
		moveExecutor.executeMove(kingCacheBoard);

		assertEquals(Square.g1, kingCacheBoard.getSquareKingBlancoCache());

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
	public void testFilter() {
		// execute
		moveExecutor.filter(filter);

		// asserts execute
		verify(filter).filterKingMove(moveExecutor);
	}	
}
