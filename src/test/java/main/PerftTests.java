/**
 * 
 */
package main;

import static org.junit.Assert.*;

import org.junit.Test;

import builder.ChessBuilderGame;
import chess.Game;
import debug.builder.DebugChessFactory;
import parsers.FENParser;

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
		ChessBuilderGame builder = new ChessBuilderGame(new DebugChessFactory());
		//ChessBuilderGame builder = new ChessBuilderGame();

		FENParser parser = new FENParser(builder);
		
		parser.parseFEN(string);
		
		return builder.getGame();
	}	
	
}
