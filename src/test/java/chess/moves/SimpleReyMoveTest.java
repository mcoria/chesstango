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
import chess.debug.chess.ColorBoardDebug;
import chess.debug.chess.KingCacheBoardDebug;
import chess.layers.PosicionPiezaBoard;
import chess.layers.imp.ArrayPosicionPiezaBoard;
import chess.moves.SimpleReyMove;
import chess.pseudomovesfilters.MoveFilter;


/**
 * @author Mauricio Coria
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class SimpleReyMoveTest {

	private SimpleReyMove moveExecutor;
	
	private PosicionPiezaBoard piezaBoard;
	
	private BoardState boardState;

	private KingCacheBoardDebug kingCacheBoard;
	
	private ColorBoardDebug colorBoard;
	
	@Mock
	private Board board;
	
	@Mock
	private MoveFilter filter;

	@Before
	public void setUp() throws Exception {
		piezaBoard = new ArrayPosicionPiezaBoard();
		piezaBoard.setPieza(Square.e1, Pieza.REY_BLANCO);
		
		colorBoard = new ColorBoardDebug(piezaBoard);
		kingCacheBoard = new KingCacheBoardDebug(piezaBoard);

		PosicionPieza origen = new PosicionPieza(Square.e1, Pieza.REY_BLANCO);
		PosicionPieza destino = new PosicionPieza(Square.e2, null);

		moveExecutor = new SimpleReyMove(origen, destino);
		
		boardState = new BoardState();
		boardState.setTurnoActual(Color.BLANCO);
		boardState.setEnroqueBlancoReyPermitido(true);
		boardState.setEnroqueBlancoReinaPermitido(true);
	}
	
	
	@Test
	public void testPosicionPiezaBoard() {
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
		moveExecutor.executeMove(boardState);

		assertEquals(Color.NEGRO, boardState.getTurnoActual());

		moveExecutor.undoMove(boardState);

		assertEquals(Color.BLANCO, boardState.getTurnoActual());
	}		

	@Test
	public void testKingCacheBoard() {
		moveExecutor.executeMove(kingCacheBoard);

		assertEquals(Square.e2, kingCacheBoard.getSquareKingBlancoCache());

		moveExecutor.undoMove(kingCacheBoard);

		assertEquals(Square.e1, kingCacheBoard.getSquareKingBlancoCache());
	}
	
	@Test
	public void testColorBoard() {
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
	
	@Test
	public void testIntegrated() {
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

		colorBoard.validar(piezaBoard);
		kingCacheBoard.validar(piezaBoard);
		
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
		
		colorBoard.validar(piezaBoard);
		kingCacheBoard.validar(piezaBoard);		
	}
}
