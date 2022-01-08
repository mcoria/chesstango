package chess.moves;

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
import chess.layers.ColorBoard;
import chess.layers.KingCacheBoard;
import chess.layers.PosicionPiezaBoard;
import chess.layers.imp.ArrayPosicionPiezaBoard;
import chess.moves.CapturaPeonPromocion;
import chess.pseudomovesfilters.MoveFilter;


/**
 * @author Mauricio Coria
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class CapturePeonPromocionTest {

	private PosicionPiezaBoard piezaBoard;
	
	private BoardState boardState;
	
	private CapturaPeonPromocion moveExecutor;
	
	private ColorBoard colorBoard;
	
	@Mock
	private Board board;
	
	@Mock
	private MoveFilter filter;	

	@Before
	public void setUp() throws Exception {
		boardState = new BoardState();
		boardState.setTurnoActual(Color.WHITE);
		
		piezaBoard = new ArrayPosicionPiezaBoard();
		piezaBoard.setPieza(Square.e7, Pieza.PEON_WHITE);
		piezaBoard.setPieza(Square.f8, Pieza.CABALLO_BLACK);
		
		colorBoard = new ColorBoard(piezaBoard);
		
		PosicionPieza origen = new PosicionPieza(Square.e7, Pieza.PEON_WHITE);
		PosicionPieza destino = new PosicionPieza(Square.f8, Pieza.CABALLO_BLACK);
		
		moveExecutor =  new CapturaPeonPromocion(origen, destino, Pieza.QUEEN_WHITE);		
	}
	
	
	@Test
	public void testPosicionPiezaBoard() {
		// execute
		moveExecutor.executeMove(piezaBoard);
		
		// asserts execute		
		assertEquals(Pieza.QUEEN_WHITE, piezaBoard.getPieza(Square.f8));
		assertTrue(piezaBoard.isEmtpy(Square.e7));
		
		// undos		
		moveExecutor.undoMove(piezaBoard);
		
		// asserts undos		
		assertEquals(Pieza.PEON_WHITE, piezaBoard.getPieza(Square.e7));
		assertEquals(Pieza.CABALLO_BLACK, piezaBoard.getPieza(Square.f8));		
	}
		
	@Test
	public void testMoveState() {
		// execute
		moveExecutor.executeMove(boardState);
		
		// asserts execute
		assertNull(boardState.getPeonPasanteSquare());
		assertEquals(Color.BLACK, boardState.getTurnoActual());
		
		// undos
		moveExecutor.undoMove(boardState);

		// asserts undos	
		assertEquals(Color.WHITE, boardState.getTurnoActual());
	}
	
	@Test
	public void testColorBoard() {
		// execute
		moveExecutor.executeMove(colorBoard);

		// asserts execute
		assertEquals(Color.WHITE, colorBoard.getColor(Square.f8));
		assertTrue(colorBoard.isEmpty(Square.e7));

		// undos
		moveExecutor.undoMove(colorBoard);
		
		// asserts undos
		assertEquals(Color.WHITE, colorBoard.getColor(Square.e7));
		assertEquals(Color.BLACK, colorBoard.getColor(Square.f8));
	}
	
	@Test(expected = RuntimeException.class)
	public void testKingCacheBoardMoveRuntimeException() {
		piezaBoard = new ArrayPosicionPiezaBoard();
		piezaBoard.setPieza(Square.e7, Pieza.PEON_WHITE);

		PosicionPieza origen = new PosicionPieza(Square.e7, Pieza.PEON_WHITE);
		PosicionPieza destino = new PosicionPieza(Square.f8, Pieza.CABALLO_BLACK);
		moveExecutor =  new CapturaPeonPromocion(origen, destino, Pieza.QUEEN_WHITE);

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
}
