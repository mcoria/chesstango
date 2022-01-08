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
import chess.moveexecutors.EnroqueNegroReyMove;


/**
 * @author Mauricio Coria
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class EnroqueNegroReyMoveTest {
	
	private PosicionPiezaBoard piezaBoard;
	
	private BoardState boardState;	
	
	private EnroqueNegroReyMove moveExecutor;
	
	private KingCacheBoard kingCacheBoard;
	
	private ColorBoard colorBoard;
	
	@Mock
	private Board board;
	
	@Mock
	private MoveFilter filter;	

	@Before
	public void setUp() throws Exception {
		moveExecutor = new EnroqueNegroReyMove();
		
		boardState = new BoardState();		
		boardState.setTurnoActual(Color.NEGRO);
		boardState.setEnroqueNegroReinaPermitido(true);
		boardState.setEnroqueNegroReyPermitido(true);
		
		piezaBoard = new ArrayPosicionPiezaBoard();
		piezaBoard.setPieza(Square.e8, Pieza.REY_NEGRO);
		piezaBoard.setPieza(Square.h8, Pieza.TORRE_NEGRO);		
		
		kingCacheBoard = new KingCacheBoard();
		colorBoard = new ColorBoard(piezaBoard);
	}
	
	@Test
	public void testPosicionPiezaBoard() {
		moveExecutor.executeMove(piezaBoard);
		
		assertEquals(Pieza.REY_NEGRO, piezaBoard.getPieza(Square.g8));		
		assertEquals(Pieza.TORRE_NEGRO, piezaBoard.getPieza(Square.f8));
		
		assertTrue(piezaBoard.isEmtpy(Square.e8));
		assertTrue(piezaBoard.isEmtpy(Square.h8));
		
		moveExecutor.undoMove(piezaBoard);
		
		assertEquals(Pieza.REY_NEGRO, piezaBoard.getPieza(Square.e8));
		assertEquals(Pieza.TORRE_NEGRO, piezaBoard.getPieza(Square.h8));
		
		assertTrue(piezaBoard.isEmtpy(Square.g8));
		assertTrue(piezaBoard.isEmtpy(Square.f8));		
	}

	@Test
	public void testBoardState() {
		moveExecutor.executeMove(boardState);		

		assertNull(boardState.getPeonPasanteSquare());
		assertEquals(Color.BLANCO, boardState.getTurnoActual());		
		assertFalse(boardState.isEnroqueNegroReinaPermitido());
		assertFalse(boardState.isEnroqueNegroReyPermitido());
		
		moveExecutor.undoMove(boardState);
		
		assertNull(boardState.getPeonPasanteSquare());
		assertEquals(Color.NEGRO, boardState.getTurnoActual());		
		assertTrue(boardState.isEnroqueNegroReinaPermitido());
		assertTrue(boardState.isEnroqueNegroReyPermitido());		
		
	}
	
	@Test
	public void testColorBoard() {
		// execute
		moveExecutor.executeMove(colorBoard);

		// asserts execute
		assertEquals(Color.NEGRO, colorBoard.getColor(Square.g8));
		assertEquals(Color.NEGRO, colorBoard.getColor(Square.f8));
		
		assertTrue(colorBoard.isEmpty(Square.e8));
		assertTrue(colorBoard.isEmpty(Square.h8));


		// undos
		moveExecutor.undoMove(colorBoard);

		// asserts undos
		assertEquals(Color.NEGRO, colorBoard.getColor(Square.e8));
		assertEquals(Color.NEGRO, colorBoard.getColor(Square.h8));
		
		assertTrue(colorBoard.isEmpty(Square.g8));
		assertTrue(colorBoard.isEmpty(Square.f8));		
	}	
	
	@Test
	public void testKingCacheBoard() {
		moveExecutor.executeMove(kingCacheBoard);

		assertEquals(Square.g8, kingCacheBoard.getSquareKingNegroCache());

		moveExecutor.undoMove(kingCacheBoard);

		assertEquals(Square.e8, kingCacheBoard.getSquareKingNegroCache());
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
