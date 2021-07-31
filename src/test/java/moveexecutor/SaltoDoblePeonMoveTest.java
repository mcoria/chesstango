package moveexecutor;

import static org.junit.Assert.assertEquals;
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
import chess.PosicionPieza;
import chess.Square;
import layers.ColorBoard;
import layers.KingCacheBoard;
import layers.PosicionPiezaBoard;
import layers.imp.ArrayPosicionPiezaBoard;
import movecalculators.MoveFilter;
import moveexecutors.SaltoDoblePeonMove;


@RunWith(MockitoJUnitRunner.class)
public class SaltoDoblePeonMoveTest {

	private PosicionPiezaBoard piezaBoard;
	
	private BoardState boardState;
	
	private SaltoDoblePeonMove moveExecutor;
	
	private ColorBoard colorBoard;
	
	@Mock
	private Board board;
	
	@Mock
	private MoveFilter filter;	

	@Before
	public void setUp() throws Exception {
		boardState = new BoardState();
	}
	
	
	@Test
	public void testPosicionPiezaBoard() {
		piezaBoard = new ArrayPosicionPiezaBoard();
		piezaBoard.setPieza(Square.e2, Pieza.PEON_BLANCO);
		
		PosicionPieza origen = new PosicionPieza(Square.e2, Pieza.PEON_BLANCO);
		PosicionPieza destino = new PosicionPieza(Square.e4, null);
		moveExecutor =  new SaltoDoblePeonMove(origen, destino, Square.e3);

		// execute
		moveExecutor.executeMove(piezaBoard);
		
		// asserts execute		
		assertEquals(Pieza.PEON_BLANCO, piezaBoard.getPieza(Square.e4));
		assertTrue(piezaBoard.isEmtpy(Square.e2));
		
		// undos		
		moveExecutor.undoMove(piezaBoard);
		
		// asserts undos		
		assertEquals(Pieza.PEON_BLANCO, piezaBoard.getPieza(Square.e2));
		assertTrue(piezaBoard.isEmtpy(Square.e4));		
	}
		
	@Test
	public void testMoveState() {
		boardState.setTurnoActual(Color.BLANCO);
		
		PosicionPieza origen = new PosicionPieza(Square.e2, Pieza.TORRE_BLANCO);
		PosicionPieza destino = new PosicionPieza(Square.e4, null);
		moveExecutor =  new SaltoDoblePeonMove(origen, destino, Square.e3);

		// execute
		moveExecutor.executeMove(boardState);
		
		// asserts execute
		assertEquals(Square.e3, boardState.getPeonPasanteSquare());
		assertEquals(Color.NEGRO, boardState.getTurnoActual());
		
		// undos
		moveExecutor.undoMove(boardState);

		// asserts undos
		assertNull(boardState.getPeonPasanteSquare());		
		assertEquals(Color.BLANCO, boardState.getTurnoActual());
	}
	
	@Test
	public void testColorBoard() {
		piezaBoard = new ArrayPosicionPiezaBoard();
		piezaBoard.setPieza(Square.e2, Pieza.PEON_BLANCO);
		
		colorBoard = new ColorBoard(piezaBoard);
		
		PosicionPieza origen = new PosicionPieza(Square.e2, Pieza.TORRE_BLANCO);
		PosicionPieza destino = new PosicionPieza(Square.e4, null);
		moveExecutor =  new SaltoDoblePeonMove(origen, destino, Square.e3);

		// execute
		moveExecutor.executeMove(colorBoard);

		// asserts execute
		assertTrue(colorBoard.isEmpty(Square.e2));		
		assertEquals(Color.BLANCO, colorBoard.getColor(Square.e4));

		// undos
		moveExecutor.undoMove(colorBoard);
		
		// asserts undos
		assertEquals(Color.BLANCO, colorBoard.getColor(Square.e2));
		assertTrue(colorBoard.isEmpty(Square.e4));
	}
	
	@Test(expected = RuntimeException.class)
	public void testKingCacheBoardMoveRuntimeException() {
		piezaBoard = new ArrayPosicionPiezaBoard();
		piezaBoard.setPieza(Square.e2, Pieza.PEON_BLANCO);
		
		PosicionPieza origen = new PosicionPieza(Square.e2, Pieza.TORRE_BLANCO);
		PosicionPieza destino = new PosicionPieza(Square.e4, null);
		moveExecutor =  new SaltoDoblePeonMove(origen, destino, Square.e3);

		moveExecutor.executeMove(new KingCacheBoard());
	}
	
	@Test(expected = RuntimeException.class)
	public void testKingCacheBoardUndoMoveRuntimeException() {
		piezaBoard = new ArrayPosicionPiezaBoard();
		piezaBoard.setPieza(Square.e2, Pieza.PEON_BLANCO);
		
		PosicionPieza origen = new PosicionPieza(Square.e2, Pieza.TORRE_BLANCO);
		PosicionPieza destino = new PosicionPieza(Square.e4, null);
		moveExecutor =  new SaltoDoblePeonMove(origen, destino, Square.e3);

		moveExecutor.undoMove(new KingCacheBoard());
	}	
	
	@Test
	public void testBoard() {
		piezaBoard = new ArrayPosicionPiezaBoard();
		piezaBoard.setPieza(Square.e2, Pieza.PEON_BLANCO);
		
		PosicionPieza origen = new PosicionPieza(Square.e2, Pieza.TORRE_BLANCO);
		PosicionPieza destino = new PosicionPieza(Square.e4, null);
		moveExecutor =  new SaltoDoblePeonMove(origen, destino, Square.e3);

		// execute
		moveExecutor.executeMove(board);

		// asserts execute
		verify(board).executeMove(moveExecutor);

		// undos
		moveExecutor.undoMove(board);

		
		// asserts undos
		verify(board).undoMove(moveExecutor);
	}
	
	
	@Test
	public void testFilter() {
		piezaBoard = new ArrayPosicionPiezaBoard();
		piezaBoard.setPieza(Square.e2, Pieza.PEON_BLANCO);
		
		PosicionPieza origen = new PosicionPieza(Square.e2, Pieza.TORRE_BLANCO);
		PosicionPieza destino = new PosicionPieza(Square.e4, null);
		moveExecutor =  new SaltoDoblePeonMove(origen, destino, Square.e3);

		// execute
		moveExecutor.filter(filter);

		// asserts execute
		verify(filter).filterMove(moveExecutor);
	}	
}
