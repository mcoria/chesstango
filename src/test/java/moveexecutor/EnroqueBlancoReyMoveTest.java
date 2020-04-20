package moveexecutor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import chess.Board;
import chess.Color;
import chess.Pieza;
import chess.Square;
import moveexecutors.EnroqueBlancoReyMove;
import parsers.FENBoarBuilder;

public class EnroqueBlancoReyMoveTest {
	
	private EnroqueBlancoReyMove moveExecutor;

	private FENBoarBuilder builder;

	@Before
	public void setUp() throws Exception {
		builder = new FENBoarBuilder();
		moveExecutor = new EnroqueBlancoReyMove();
	}
	
	@Test
	public void testExecute() {
		Board board = builder.withFEN("4k3/8/8/8/8/8/8/4K2R w KQkq - 0 1").buildBoard();

		moveExecutor.executeMove(board);
		assertEquals(Pieza.REY_BLANCO, board.getPieza(Square.g1));
		assertEquals(Pieza.TORRE_BLANCO, board.getPieza(Square.f1));


		moveExecutor.executeState(board.getBoardState());
		assertFalse(board.getBoardState().isEnroqueBlancoReyPermitido());
		assertFalse(board.getBoardState().isEnroqueBlancoReinaPermitido());
		assertEquals(Color.NEGRO, board.getBoardState().getTurnoActual());
	}	

}
