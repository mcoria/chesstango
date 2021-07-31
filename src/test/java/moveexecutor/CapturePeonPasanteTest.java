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
import moveexecutors.CaptureMove;
import moveexecutors.CapturePeonPasante;

@RunWith(MockitoJUnitRunner.class)
public class CapturePeonPasanteTest {

	private PosicionPiezaBoard piezaBoard;
	
	private BoardState boardState;
	
	private CapturePeonPasante moveExecutor;
	
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
		piezaBoard.setPieza(Square.b5, Pieza.PEON_BLANCO);
		piezaBoard.setPieza(Square.a5, Pieza.PEON_NEGRO);
		
		boardState.setPeonPasanteSquare(Square.a6);
		
		PosicionPieza peonBlanco = new PosicionPieza(Square.b5, Pieza.PEON_BLANCO);
		PosicionPieza peonNegro = new PosicionPieza(Square.a5, Pieza.PEON_NEGRO);
		PosicionPieza peonPasanteSquare = new PosicionPieza(Square.a6, null);
		
		moveExecutor = new CapturePeonPasante(peonBlanco, peonPasanteSquare, peonNegro);

		// execute
		moveExecutor.executeMove(piezaBoard);
		
		// asserts execute
		assertTrue(piezaBoard.isEmtpy(Square.a5));
		assertTrue(piezaBoard.isEmtpy(Square.b5));
		assertEquals(Pieza.PEON_BLANCO, piezaBoard.getPieza(Square.a6));
		
		// undos
		moveExecutor.undoMove(piezaBoard);
		
		// asserts undos
		assertTrue(piezaBoard.isEmtpy(Square.a6));
		assertEquals(Pieza.PEON_BLANCO, piezaBoard.getPieza(Square.b5));
		assertEquals(Pieza.PEON_NEGRO, piezaBoard.getPieza(Square.a5));		
		
	}
	
	@Test
	public void testMoveState() {
		boardState.setPeonPasanteSquare(Square.a6);
		boardState.setTurnoActual(Color.BLANCO);
		
		PosicionPieza peonBlanco = new PosicionPieza(Square.b5, Pieza.PEON_BLANCO);
		PosicionPieza peonNegro = new PosicionPieza(Square.a5, Pieza.PEON_NEGRO);
		PosicionPieza peonPasanteSquare = new PosicionPieza(Square.a6, null);
		
		moveExecutor = new CapturePeonPasante(peonBlanco, peonPasanteSquare, peonNegro);

		// execute
		moveExecutor.executeMove(boardState);		
		
		// asserts execute
		assertNull(boardState.getPeonPasanteSquare());
		assertEquals(Color.NEGRO, boardState.getTurnoActual());	
		
		// undos
		moveExecutor.undoMove(boardState);	
		
		// asserts undos
		assertEquals(Square.a6, boardState.getPeonPasanteSquare());
		assertEquals(Color.BLANCO, boardState.getTurnoActual());			
		
	}
	
	@Test
	public void testColorBoard() {
		piezaBoard = new ArrayPosicionPiezaBoard();
		piezaBoard.setPieza(Square.b5, Pieza.PEON_BLANCO);
		piezaBoard.setPieza(Square.a5, Pieza.PEON_NEGRO);
		
		colorBoard = new ColorBoard(piezaBoard);
		
		PosicionPieza peonBlanco = new PosicionPieza(Square.b5, Pieza.PEON_BLANCO);
		PosicionPieza peonNegro = new PosicionPieza(Square.a5, Pieza.PEON_NEGRO);
		PosicionPieza peonPasanteSquare = new PosicionPieza(Square.a6, null);
		
		moveExecutor = new CapturePeonPasante(peonBlanco, peonPasanteSquare, peonNegro);

		// execute
		moveExecutor.executeMove(colorBoard);
		
		// asserts execute
		assertEquals(Color.BLANCO, colorBoard.getColor(Square.a6));
		assertTrue(colorBoard.isEmpty(Square.a5));
		assertTrue(colorBoard.isEmpty(Square.b5));

		// undos
		moveExecutor.undoMove(colorBoard);
		
		// asserts undos
		assertTrue(colorBoard.isEmpty(Square.a6));
		assertEquals(Color.NEGRO, colorBoard.getColor(Square.a5));
		assertEquals(Color.BLANCO, colorBoard.getColor(Square.b5));
		
	}	
	
	@Test
	public void testBoard() {
		piezaBoard = new ArrayPosicionPiezaBoard();
		piezaBoard.setPieza(Square.b5, Pieza.PEON_BLANCO);
		piezaBoard.setPieza(Square.a5, Pieza.PEON_NEGRO);
		
		boardState.setPeonPasanteSquare(Square.a6);
		
		PosicionPieza peonBlanco = new PosicionPieza(Square.b5, Pieza.PEON_BLANCO);
		PosicionPieza peonNegro = new PosicionPieza(Square.a5, Pieza.PEON_NEGRO);
		PosicionPieza peonPasanteSquare = new PosicionPieza(Square.a6, null);
		
		moveExecutor = new CapturePeonPasante(peonBlanco, peonPasanteSquare, peonNegro);

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
		piezaBoard.setPieza(Square.b5, Pieza.PEON_BLANCO);
		piezaBoard.setPieza(Square.a5, Pieza.PEON_NEGRO);
		
		boardState.setPeonPasanteSquare(Square.a6);
		
		PosicionPieza peonBlanco = new PosicionPieza(Square.b5, Pieza.PEON_BLANCO);
		PosicionPieza peonNegro = new PosicionPieza(Square.a5, Pieza.PEON_NEGRO);
		PosicionPieza peonPasanteSquare = new PosicionPieza(Square.a6, null);
		
		moveExecutor = new CapturePeonPasante(peonBlanco, peonPasanteSquare, peonNegro);

		// execute
		moveExecutor.filter(filter);

		// asserts execute
		verify(filter).filterMove(moveExecutor);
	}
	
	@Test(expected = RuntimeException.class)
	public void testKingCacheBoardMoveRuntimeException() {
		piezaBoard = new ArrayPosicionPiezaBoard();
		piezaBoard.setPieza(Square.b5, Pieza.PEON_BLANCO);
		piezaBoard.setPieza(Square.a5, Pieza.PEON_NEGRO);
		
		boardState.setPeonPasanteSquare(Square.a6);
		
		PosicionPieza peonBlanco = new PosicionPieza(Square.b5, Pieza.PEON_BLANCO);
		PosicionPieza peonNegro = new PosicionPieza(Square.a5, Pieza.PEON_NEGRO);
		PosicionPieza peonPasanteSquare = new PosicionPieza(Square.a6, null);
		
		moveExecutor = new CapturePeonPasante(peonBlanco, peonPasanteSquare, peonNegro);

		moveExecutor.executeMove(new KingCacheBoard());
	}
	
	@Test(expected = RuntimeException.class)
	public void testKingCacheBoardUndoMoveRuntimeException() {
		piezaBoard = new ArrayPosicionPiezaBoard();
		piezaBoard.setPieza(Square.b5, Pieza.PEON_BLANCO);
		piezaBoard.setPieza(Square.a5, Pieza.PEON_NEGRO);
		
		boardState.setPeonPasanteSquare(Square.a6);
		
		PosicionPieza peonBlanco = new PosicionPieza(Square.b5, Pieza.PEON_BLANCO);
		PosicionPieza peonNegro = new PosicionPieza(Square.a5, Pieza.PEON_NEGRO);
		PosicionPieza peonPasanteSquare = new PosicionPieza(Square.a6, null);
		
		moveExecutor = new CapturePeonPasante(peonBlanco, peonPasanteSquare, peonNegro);

		moveExecutor.undoMove(new KingCacheBoard());
	}		

}
