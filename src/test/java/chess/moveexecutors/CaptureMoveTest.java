package chess.moveexecutors;

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
import chess.debug.chess.ColorBoardDebug;
import chess.layers.KingCacheBoard;
import chess.layers.PosicionPiezaBoard;
import chess.layers.imp.ArrayPosicionPiezaBoard;
import chess.movecalculators.MoveFilter;
import chess.moveexecutors.CaptureMove;


/**
 * @author Mauricio Coria
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class CaptureMoveTest {

	private PosicionPiezaBoard piezaBoard;
	
	private BoardState boardState;
	
	private CaptureMove moveExecutor;
	
	private ColorBoardDebug colorBoard;
	
	@Mock
	private Board board;
	
	@Mock
	private MoveFilter filter;		

	@Before
	public void setUp() throws Exception {
		boardState = new BoardState();
		boardState.setTurnoActual(Color.BLANCO);
		
		piezaBoard = new ArrayPosicionPiezaBoard();
		piezaBoard.setPieza(Square.e5, Pieza.TORRE_BLANCO);
		piezaBoard.setPieza(Square.e7, Pieza.PEON_NEGRO);
		
		colorBoard = new ColorBoardDebug(piezaBoard);
		
		PosicionPieza origen = new PosicionPieza(Square.e5, Pieza.TORRE_BLANCO);
		PosicionPieza destino = new PosicionPieza(Square.e7, Pieza.PEON_NEGRO);

		moveExecutor = new CaptureMove(origen, destino);
	}

	
	@Test
	public void testPosicionPiezaBoard() {
		// execute
		moveExecutor.executeMove(piezaBoard);
		
		// asserts execute	
		assertEquals(Pieza.TORRE_BLANCO, piezaBoard.getPieza(Square.e7));
		assertTrue(piezaBoard.isEmtpy(Square.e5));	
		
		// undos	
		moveExecutor.undoMove(piezaBoard);
		
		// asserts undos
		assertEquals(Pieza.TORRE_BLANCO, piezaBoard.getPieza(Square.e5));
		assertEquals(Pieza.PEON_NEGRO, piezaBoard.getPieza(Square.e7));
	}
	
	@Test
	public void testMoveState() {
		// execute
		moveExecutor.executeMove(boardState);		

		// asserts execute	
		assertNull(boardState.getPeonPasanteSquare());
		assertEquals(Color.NEGRO, boardState.getTurnoActual());
		
		// undos
		moveExecutor.undoMove(boardState);

		// asserts undos
		assertEquals(Color.BLANCO, boardState.getTurnoActual());		
	}
	
	@Test
	public void testColorBoard() {
		// execute
		moveExecutor.executeMove(colorBoard);

		// asserts execute
		assertEquals(Color.BLANCO, colorBoard.getColor(Square.e7));
		assertTrue(colorBoard.isEmpty(Square.e5));

		// undos
		moveExecutor.undoMove(colorBoard);
		
		// asserts undos
		assertEquals(Color.BLANCO, colorBoard.getColor(Square.e5));
		assertEquals(Color.NEGRO, colorBoard.getColor(Square.e7));
	}
	
	@Test(expected = RuntimeException.class)
	public void testKingCacheBoardMoveRuntimeException() {
		moveExecutor.executeMove(new KingCacheBoard());
	}
	
	@Test(expected = RuntimeException.class)
	public void testKingCacheBoardUndoMoveRuntimeException() {
		moveExecutor.undoMove(new KingCacheBoard());
	}	
	
	@Test
	public void testBoard() {
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
		// execute
		moveExecutor.filter(filter);

		// asserts execute
		verify(filter).filterMove(moveExecutor);
	}
	
	@Test
	public void testIntegrated() {
		// execute
		moveExecutor.executeMove(piezaBoard);		
		moveExecutor.executeMove(colorBoard);
		moveExecutor.executeMove(boardState);
		
		colorBoard.validar(piezaBoard);
		
		// undos
		moveExecutor.undoMove(piezaBoard);		
		moveExecutor.undoMove(colorBoard);
		moveExecutor.undoMove(boardState);		
		
		colorBoard.validar(piezaBoard);
	}
	
	
}
