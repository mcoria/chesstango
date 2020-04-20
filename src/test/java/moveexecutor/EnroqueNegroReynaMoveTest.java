package moveexecutor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import chess.Board;
import chess.Color;
import chess.Pieza;
import chess.Square;
import moveexecutors.EnroqueNegroReynaMove;
import parsers.FENBoarBuilder;

public class EnroqueNegroReynaMoveTest {
	
	private EnroqueNegroReynaMove moveExecutor;

	private FENBoarBuilder builder;

	@Before
	public void setUp() throws Exception {
		builder = new FENBoarBuilder();
		moveExecutor = new EnroqueNegroReynaMove();
	}
	
	@Test
	public void testExecute() {
		Board board = builder.withFEN("r3k3/8/8/8/8/8/8/4K3 b KQkq - 0 1").buildBoard();

		moveExecutor.executeMove(board);
		assertEquals(Pieza.REY_NEGRO, board.getPieza(Square.c8));
		assertEquals(Pieza.TORRE_NEGRO, board.getPieza(Square.d8));


		moveExecutor.executeMove(board.getBoardState());
		assertFalse(board.getBoardState().isEnroqueNegroReyPermitido());
		assertFalse(board.getBoardState().isEnroqueNegroReinaPermitido());
		assertEquals(Color.BLANCO, board.getBoardState().getTurnoActual());
	}	

}
