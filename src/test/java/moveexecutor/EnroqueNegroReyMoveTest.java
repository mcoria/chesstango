package moveexecutor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import chess.Board;
import chess.Color;
import chess.Pieza;
import chess.Square;
import moveexecutors.EnroqueNegroReyMove;
import parsers.FENBoarBuilder;

public class EnroqueNegroReyMoveTest {
	
	private EnroqueNegroReyMove moveExecutor;

	private FENBoarBuilder builder;

	@Before
	public void setUp() throws Exception {
		builder = new FENBoarBuilder();
		moveExecutor = new EnroqueNegroReyMove();
	}
	
	@Test
	public void testExecute() {
		Board board = builder.withFEN("4k2r/8/8/8/8/8/8/4K3 b KQkq - 0 1").buildBoard();

		moveExecutor.executeMove(board);
		assertEquals(Pieza.REY_NEGRO, board.getPieza(Square.g8));
		assertEquals(Pieza.TORRE_NEGRO, board.getPieza(Square.f8));


		moveExecutor.executeState(board.getBoardState());
		assertFalse(board.getBoardState().isEnroqueNegroReyPermitido());
		assertFalse(board.getBoardState().isEnroqueNegroReinaPermitido());
		assertEquals(Color.BLANCO, board.getBoardState().getTurnoActual());
	}	

}
