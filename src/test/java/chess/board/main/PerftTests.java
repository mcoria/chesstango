/**
 * 
 */
package chess.board.main;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import chess.board.Game;
import chess.board.builder.imp.GameBuilder;
import chess.board.debug.builder.DebugChessFactory;
import chess.board.fen.FENDecoder;
import chess.board.main.ChessMain;
import chess.board.main.PerftResult;

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
		GameBuilder builder = new GameBuilder(new DebugChessFactory());
		//ChessBuilderGame builder = new ChessBuilderGame();

		FENDecoder parser = new FENDecoder(builder);
		
		parser.parseFEN(string);
		
		return builder.getResult();
	}	
	
}
