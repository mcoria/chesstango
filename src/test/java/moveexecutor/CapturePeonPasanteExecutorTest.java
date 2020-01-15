package moveexecutor;

import org.junit.Test;

import chess.DummyBoard;
import chess.Move;
import chess.Square;
import moveexecutors.CapturePeonPasanteExecutor;
import parsers.FENParser;

public class CapturePeonPasanteExecutorTest {

	@Test
	public void test() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/3pP3/8/8/8/8");
		
		CapturePeonPasanteExecutor moveExecutor = new CapturePeonPasanteExecutor(Square.d5);
		
		//moveExecutor.execute(tablero.getMediator(), new Move(Square.e5, Square.d6, null)); //Pieza.PEON_NEGRO
		//assertEquals(tablero.getPieza(Square.d6), Pieza.PEON_BLANCO);
		//assertTrue(tablero.isEmtpy(Square.e5));
	}

}
