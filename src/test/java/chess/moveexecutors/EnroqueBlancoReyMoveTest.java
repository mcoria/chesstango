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
import chess.Color;
import chess.Pieza;
import chess.Square;
import chess.debug.chess.BoardStateDebug;
import chess.debug.chess.ColorBoardDebug;
import chess.debug.chess.KingCacheBoardDebug;
import chess.layers.PosicionPiezaBoard;
import chess.layers.imp.ArrayPosicionPiezaBoard;
import chess.movecalculators.MoveFilter;
import chess.moveexecutors.EnroqueBlancoReyMove;


/**
 * @author Mauricio Coria
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class EnroqueBlancoReyMoveTest {
	
	private PosicionPiezaBoard piezaBoard;
	
	private BoardStateDebug boardState;	
	
	private EnroqueBlancoReyMove moveExecutor;
	
	private KingCacheBoardDebug kingCacheBoard;
	
	private ColorBoardDebug colorBoard;
	
	@Mock
	private Board board;
	
	@Mock
	private MoveFilter filter;	

	@Before
	public void setUp() throws Exception {
		moveExecutor = new EnroqueBlancoReyMove();
		
		boardState = new BoardStateDebug();		
		boardState.setTurnoActual(Color.BLANCO);
		boardState.setEnroqueBlancoReinaPermitido(false);
		boardState.setEnroqueBlancoReyPermitido(true);
		
		piezaBoard = new ArrayPosicionPiezaBoard();
		piezaBoard.setPieza(Square.e1, Pieza.REY_BLANCO);
		piezaBoard.setPieza(Square.h1, Pieza.TORRE_BLANCO);		
		
		kingCacheBoard = new KingCacheBoardDebug(piezaBoard);
		colorBoard = new ColorBoardDebug(piezaBoard);
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
		// execute		
		moveExecutor.executeMove(boardState);		

		// asserts execute
		assertNull(boardState.getPeonPasanteSquare());
		assertEquals(Color.NEGRO, boardState.getTurnoActual());		
		assertFalse(boardState.isEnroqueBlancoReinaPermitido());
		assertFalse(boardState.isEnroqueBlancoReyPermitido());
		boardState.validar();
		
		// undos
		moveExecutor.undoMove(boardState);
		
		// asserts undos		
		assertNull(boardState.getPeonPasanteSquare());
		assertEquals(Color.BLANCO, boardState.getTurnoActual());		
		assertFalse(boardState.isEnroqueBlancoReinaPermitido());
		assertTrue(boardState.isEnroqueBlancoReyPermitido());
		boardState.validar();
		
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
	//TODO: Add test body
	public void testFilter() {
		/*
		// execute
		moveExecutor.filter(filter);

		// asserts execute
		verify(filter).filterKingMove(moveExecutor);
		*/
	}
	
	@Test
	public void testIntegrated() {
		// execute
		moveExecutor.executeMove(piezaBoard);
		moveExecutor.executeMove(boardState);
		moveExecutor.executeMove(colorBoard);
		moveExecutor.executeMove(kingCacheBoard);

		// asserts execute
		colorBoard.validar(piezaBoard);
		boardState.validar(piezaBoard);
		kingCacheBoard.validar(piezaBoard);
		
		// undos
		moveExecutor.undoMove(piezaBoard);
		moveExecutor.undoMove(boardState);
		moveExecutor.undoMove(colorBoard);
		moveExecutor.undoMove(kingCacheBoard);

		
		// asserts undos
		colorBoard.validar(piezaBoard);	
		boardState.validar(piezaBoard);
		kingCacheBoard.validar(piezaBoard);
	}	
}
