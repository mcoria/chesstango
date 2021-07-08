package moveexecutor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import builder.ChessBuilder;
import chess.BoardState;
import chess.Color;
import chess.Pieza;
import chess.Square;
import layers.PosicionPiezaBoard;
import moveexecutors.EnroqueNegroReyMove;
import parsers.FENBoarBuilder;

public class EnroqueNegroReyMoveTest {
	
	private PosicionPiezaBoard board;
	
	private BoardState boardState;	
	
	private EnroqueNegroReyMove moveExecutor;
	
	private FENBoarBuilder<ChessBuilder> builder;
	
	@Before
	public void setUp() throws Exception {
		builder = new FENBoarBuilder<ChessBuilder>(new ChessBuilder());
		
		moveExecutor = new EnroqueNegroReyMove();
		
		boardState = new BoardState();		
		boardState.setTurnoActual(Color.NEGRO);
		boardState.setEnroqueNegroReinaPermitido(true);
		boardState.setEnroqueNegroReyPermitido(true);
	}
	
	@Test
	public void testExecuteMoveBoard() {
		board = builder.constructTablero("4k2r/8/8/8/8/8/8/8").getBuilder().getPosicionPiezaBoard();
		
		moveExecutor.executeMove(board);
		
		assertEquals(Pieza.REY_NEGRO, board.getPieza(Square.g8));
		assertTrue(board.isEmtpy(Square.e8));
		
		assertEquals(Pieza.TORRE_NEGRO, board.getPieza(Square.f8));
		assertTrue(board.isEmtpy(Square.h8));	
	}

	@Test
	public void testExecuteMoveState() {
		moveExecutor.executeMove(boardState);		

		assertNull(boardState.getPeonPasanteSquare());
		assertEquals(Color.BLANCO, boardState.getTurnoActual());		
		assertFalse(boardState.isEnroqueNegroReinaPermitido());
		assertFalse(boardState.isEnroqueNegroReyPermitido());
	}	
	
}
