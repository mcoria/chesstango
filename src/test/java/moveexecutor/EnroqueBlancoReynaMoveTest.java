package moveexecutor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import chess.Board;
import chess.Color;
import chess.Pieza;
import chess.Square;
import moveexecutors.EnroqueBlancoReynaMove;
import parsers.FENBoarBuilder;

public class EnroqueBlancoReynaMoveTest {	
	
	private EnroqueBlancoReynaMove moveExecutor;

	private FENBoarBuilder builder;

	@Before
	public void setUp() throws Exception {
		builder = new FENBoarBuilder();
		moveExecutor = new EnroqueBlancoReynaMove();
	}
	
	@Test
	public void testExecute() {
		Board board = builder.withFEN("4k3/8/8/8/8/8/8/R3K3 w KQkq - 0 1").buildBoard();

		moveExecutor.executeMove(board);
		assertEquals(Pieza.REY_BLANCO, board.getPieza(Square.c1));
		assertEquals(Pieza.TORRE_BLANCO, board.getPieza(Square.d1));


		moveExecutor.executeMove(board.getBoardState());
		assertFalse(board.getBoardState().isEnroqueBlancoReyPermitido());
		assertFalse(board.getBoardState().isEnroqueBlancoReinaPermitido());
		assertEquals(Color.NEGRO, board.getBoardState().getTurnoActual());
	}	

}
