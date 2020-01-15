package moveexecutor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import chess.DummyBoard;
import chess.Move;
import chess.Pieza;
import chess.Square;
import moveexecutors.CaptureMoveExecutor;
import parsers.FENParser;

public class CaptureMoveExecutorTest {
	
	@Test
	public void testEquals01() {
		assertEquals(new CaptureMoveExecutor(Pieza.TORRE_BLANCO, Pieza.ALFIL_NEGRO), new CaptureMoveExecutor(Pieza.TORRE_BLANCO, Pieza.ALFIL_NEGRO));
	}
	
	@Test
	public void testExecute() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/4p3/8/4R3/8/8/8/8");
		
		CaptureMoveExecutor moveExecutor = new CaptureMoveExecutor(Pieza.TORRE_BLANCO, Pieza.PEON_NEGRO);
		
		moveExecutor.execute(tablero, new Move(Square.e5, Square.e7, null)); //Pieza.PEON_NEGRO
		assertEquals(tablero.getPieza(Square.e7), Pieza.TORRE_BLANCO);
		assertTrue(tablero.isEmtpy(Square.e5));
	}

}
