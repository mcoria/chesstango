package moveexecutor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import builder.ChessBuilderParts;
import chess.BoardState;
import chess.Color;
import chess.Pieza;
import chess.Square;
import layers.PosicionPiezaBoard;
import moveexecutors.EnroqueNegroReynaMove;
import parsers.FENParser;

public class EnroqueNegroReynaMoveTest {
	
	private PosicionPiezaBoard board;
	
	private BoardState boardState;	
	
	private EnroqueNegroReynaMove moveExecutor;

	@Before
	public void setUp() throws Exception {
		moveExecutor = new EnroqueNegroReynaMove();
		
		boardState = new BoardState();		
		boardState.setTurnoActual(Color.NEGRO);
		boardState.setEnroqueNegroReinaPermitido(true);
		boardState.setEnroqueNegroReyPermitido(true);
	}
	
	@Test
	public void testExecuteMoveBoard() {
		board =  getTablero("r3k3/8/8/8/8/8/8/8");
		
		moveExecutor.executeMove(board);
		
		assertEquals(Pieza.REY_NEGRO, board.getPieza(Square.c8));
		assertTrue(board.isEmtpy(Square.e8));
		
		assertEquals(Pieza.TORRE_NEGRO, board.getPieza(Square.d8));
		assertTrue(board.isEmtpy(Square.a8));	
	}

	@Test
	public void testExecuteMoveState() {
		boardState.setTurnoActual(Color.NEGRO);
		
		moveExecutor.executeMove(boardState);		

		assertNull(boardState.getPeonPasanteSquare());
		assertEquals(Color.BLANCO, boardState.getTurnoActual());		
		assertFalse(boardState.isEnroqueNegroReinaPermitido());
		assertFalse(boardState.isEnroqueNegroReyPermitido());
	}		
	
	private PosicionPiezaBoard getTablero(String string) {		
		ChessBuilderParts builder = new ChessBuilderParts();
		FENParser parser = new FENParser(builder);
		
		parser.parsePiecePlacement(string);
		
		return builder.getPosicionPiezaBoard();
	}	

}
