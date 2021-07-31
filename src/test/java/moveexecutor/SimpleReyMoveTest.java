package moveexecutor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.verify;

import chess.Board;
import chess.BoardState;
import chess.Color;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;
import layers.ColorBoard;
import layers.KingCacheBoard;
import layers.PosicionPiezaBoard;
import layers.imp.ArrayPosicionPiezaBoard;
import movecalculators.MoveFilter;
import moveexecutors.SimpleReyMove;

@RunWith(MockitoJUnitRunner.class)
public class SimpleReyMoveTest {

	private SimpleReyMove moveExecutor;
	
	private PosicionPiezaBoard piezaBoard;
	
	private BoardState boardState;

	private KingCacheBoard kingCacheBoard;
	
	private ColorBoard colorBoard;
	
	@Mock
	private Board board;
	
	@Mock
	private MoveFilter filter;

	@Before
	public void setUp() throws Exception {
		boardState = new BoardState();
		kingCacheBoard = new KingCacheBoard();
		moveExecutor = null;
		piezaBoard = null;
	}
	
	
	@Test
	public void testPosicionPiezaBoard() {
		piezaBoard = new ArrayPosicionPiezaBoard();
		piezaBoard.setPieza(Square.e1, Pieza.REY_BLANCO);

		PosicionPieza origen = new PosicionPieza(Square.e1, Pieza.REY_BLANCO);
		PosicionPieza destino = new PosicionPieza(Square.e2, null);

		moveExecutor = new SimpleReyMove(origen, destino);

		// execute
		moveExecutor.executeMove(piezaBoard);

		// asserts execute
		assertEquals(Pieza.REY_BLANCO, piezaBoard.getPieza(Square.e2));
		assertNull(piezaBoard.getPieza(Square.e1));

		// undos
		moveExecutor.undoMove(piezaBoard);
		
		// asserts undos
		assertEquals(Pieza.REY_BLANCO, piezaBoard.getPieza(Square.e1));
		assertTrue(piezaBoard.isEmtpy(Square.e2));
	}	
	
	@Test
	public void testBoardState() {
		boardState.setTurnoActual(Color.BLANCO);

		PosicionPieza origen = new PosicionPieza(Square.d2, Pieza.REY_BLANCO);
		PosicionPieza destino = new PosicionPieza(Square.e3, null);

		moveExecutor = new SimpleReyMove(origen, destino);

		moveExecutor.executeMove(boardState);

		assertEquals(Color.NEGRO, boardState.getTurnoActual());

		moveExecutor.undoMove(boardState);

		assertEquals(Color.BLANCO, boardState.getTurnoActual());
	}
	
	@Test
	public void testSimpleReyMoveBlancoPierdeEnroque() {
		boardState.setTurnoActual(Color.BLANCO);
		boardState.setEnroqueBlancoReyPermitido(true);
		boardState.setEnroqueBlancoReinaPermitido(false);

		PosicionPieza origen = new PosicionPieza(Square.e1, Pieza.REY_BLANCO);
		PosicionPieza destino = new PosicionPieza(Square.f1, null);

		moveExecutor = new SimpleReyMove(origen, destino);

		moveExecutor.executeMove(boardState);

		assertEquals(Color.NEGRO, boardState.getTurnoActual());
		assertFalse(boardState.isEnroqueBlancoReyPermitido());
		assertFalse(boardState.isEnroqueBlancoReinaPermitido());
	}
	
	
	@Test
	public void testSimpleReyMoveNegroPierdeEnroque() {
		boardState.setTurnoActual(Color.NEGRO);
		boardState.setEnroqueNegroReinaPermitido(true);
		boardState.setEnroqueNegroReyPermitido(false);

		PosicionPieza origen = new PosicionPieza(Square.e8, Pieza.REY_NEGRO);
		PosicionPieza destino = new PosicionPieza(Square.f8, null);

		moveExecutor = new SimpleReyMove(origen, destino);

		moveExecutor.executeMove(boardState);

		assertEquals(Color.BLANCO, boardState.getTurnoActual());
		assertFalse(boardState.isEnroqueNegroReyPermitido());
		assertFalse(boardState.isEnroqueNegroReinaPermitido());
	}		

	@Test
	public void testMoveCacheBoard() {
		kingCacheBoard.setKingSquare(Color.BLANCO, Square.d2);

		PosicionPieza origen = new PosicionPieza(Square.d2, Pieza.REY_BLANCO);
		PosicionPieza destino = new PosicionPieza(Square.e3, null);

		moveExecutor = new SimpleReyMove(origen, destino);

		moveExecutor.executeMove(kingCacheBoard);

		assertEquals(Square.e3, kingCacheBoard.getSquareKingBlancoCache());

		moveExecutor.undoMove(kingCacheBoard);

		assertEquals(Square.d2, kingCacheBoard.getSquareKingBlancoCache());
	}
	
	@Test
	public void testColorBoard() {
		piezaBoard = new ArrayPosicionPiezaBoard();
		piezaBoard.setPieza(Square.e1, Pieza.REY_BLANCO);
		
		colorBoard = new ColorBoard(piezaBoard);

		PosicionPieza origen = new PosicionPieza(Square.e1, Pieza.REY_BLANCO);
		PosicionPieza destino = new PosicionPieza(Square.e2, null);

		moveExecutor = new SimpleReyMove(origen, destino);

		// execute
		moveExecutor.executeMove(colorBoard);

		// asserts execute
		assertEquals(Color.BLANCO, colorBoard.getColor(Square.e2));
		assertTrue(colorBoard.isEmpty(Square.e1));

		// undos
		moveExecutor.undoMove(colorBoard);

		
		// asserts undos
		assertEquals(Color.BLANCO, colorBoard.getColor(Square.e1));
		assertTrue(colorBoard.isEmpty(Square.e2));
	}
	
	@Test
	public void testBoard() {
		PosicionPieza origen = new PosicionPieza(Square.e1, Pieza.REY_BLANCO);
		PosicionPieza destino = new PosicionPieza(Square.e2, null);

		moveExecutor = new SimpleReyMove(origen, destino);

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
		PosicionPieza origen = new PosicionPieza(Square.e1, Pieza.REY_BLANCO);
		PosicionPieza destino = new PosicionPieza(Square.e2, null);

		moveExecutor = new SimpleReyMove(origen, destino);

		// execute
		moveExecutor.filter(filter);

		// asserts execute
		verify(filter).filterKingMove(moveExecutor);
	}	
	
	@Test
	public void testExecuteUndo() {
		piezaBoard = new ArrayPosicionPiezaBoard();
		piezaBoard.setPieza(Square.e1, Pieza.REY_BLANCO);
		
		colorBoard = new ColorBoard(piezaBoard);
		
		boardState.setTurnoActual(Color.BLANCO);

		PosicionPieza origen = new PosicionPieza(Square.e1, Pieza.REY_BLANCO);
		PosicionPieza destino = new PosicionPieza(Square.e2, null);

		moveExecutor = new SimpleReyMove(origen, destino);

		// execute
		moveExecutor.executeMove(piezaBoard);
		moveExecutor.executeMove(kingCacheBoard);
		moveExecutor.executeMove(boardState);
		moveExecutor.executeMove(colorBoard);

		// asserts execute
		assertEquals(Pieza.REY_BLANCO, piezaBoard.getPieza(Square.e2));
		assertNull(piezaBoard.getPieza(Square.e1));
		
		assertEquals(Square.e2, kingCacheBoard.getSquareKingBlancoCache());
		
		assertEquals(Color.NEGRO, boardState.getTurnoActual());
		
		assertEquals(Color.BLANCO, colorBoard.getColor(Square.e2));
		assertTrue(colorBoard.isEmpty(Square.e1));

		// undos
		moveExecutor.undoMove(piezaBoard);
		moveExecutor.undoMove(kingCacheBoard);
		moveExecutor.undoMove(boardState);
		moveExecutor.undoMove(colorBoard);

		
		// asserts undos
		assertEquals(Pieza.REY_BLANCO, piezaBoard.getPieza(Square.e1));
		assertTrue(piezaBoard.isEmtpy(Square.e2));
		
		assertEquals(Square.e1, kingCacheBoard.getSquareKingBlancoCache());
		
		assertEquals(Color.BLANCO, boardState.getTurnoActual());
		
		assertEquals(Color.BLANCO, colorBoard.getColor(Square.e1));
		assertTrue(colorBoard.isEmpty(Square.e2));
	}
}
