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
import moveexecutors.EnroqueBlancoReyMove;
import parsers.FENBoarBuilder;

public class EnroqueBlancoReyMoveTest {
	
	private PosicionPiezaBoard board;
	
	private BoardState boardState;	
	
	private EnroqueBlancoReyMove moveExecutor;
	
	private FENBoarBuilder<ChessBuilder> builder;

	@Before
	public void setUp() throws Exception {
		builder = new FENBoarBuilder<ChessBuilder>(new ChessBuilder());
		
		moveExecutor = new EnroqueBlancoReyMove();
		
		boardState = new BoardState();		
		boardState.setTurnoActual(Color.NEGRO);
		boardState.setEnroqueBlancoReinaPermitido(true);
		boardState.setEnroqueBlancoReyPermitido(true);
	}
	
	@Test
	public void testExecuteMoveBoard() {
		board =  builder.constructTablero("8/8/8/8/8/8/8/4K2R").getBuilder().getPosicionPiezaBoard();
		
		moveExecutor.executeMove(board);
		
		assertEquals(Pieza.REY_BLANCO, board.getPieza(Square.g1));
		assertTrue(board.isEmtpy(Square.e1));
		
		assertEquals(Pieza.TORRE_BLANCO, board.getPieza(Square.f1));
		assertTrue(board.isEmtpy(Square.h1));	
	}

	@Test
	public void testExecuteMoveState() {
		boardState.setTurnoActual(Color.BLANCO);
		
		moveExecutor.executeMove(boardState);		

		assertNull(boardState.getPeonPasanteSquare());
		assertEquals(Color.NEGRO, boardState.getTurnoActual());		
		assertFalse(boardState.isEnroqueBlancoReinaPermitido());
		assertFalse(boardState.isEnroqueBlancoReyPermitido());
	}	

}
