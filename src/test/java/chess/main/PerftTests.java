/**
 * 
 */
package chess.main;

import static org.junit.Assert.*;

import org.junit.Test;

import chess.Game;
import chess.builder.ChessGameBuilder;
import chess.debug.builder.DebugChessFactory;
import chess.main.ChessMain;
import chess.main.PerftResult;
import chess.parsers.FENParser;

/**
 * @author Mauricio Coria
 *
 */
public class PerftTests {

	@Test
	public void test_1() {
		ChessMain main = new ChessMain();
		Game board = getGame("4k3/8/8/8/8/8/8/R3K2R w KQ - 0 1");
		
		PerftResult result= main.start(board, 5);
		
		assertEquals(532933, result.getTotalNodes());
	}
	
	private Game getGame(String string) {		
		ChessGameBuilder builder = new ChessGameBuilder(new DebugChessFactory());
		//ChessBuilderGame builder = new ChessBuilderGame();

		FENParser parser = new FENParser(builder);
		
		parser.parseFEN(string);
		
		return builder.getGame();
	}	
	
}
