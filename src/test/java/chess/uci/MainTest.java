/**
 * 
 */
package chess.uci;

import org.junit.Assert;
import org.junit.Test;

import chess.board.Game;
import chess.board.representations.fen.FENEncoder;

/**
 * @author Mauricio Coria
 *
 */
public class MainTest {

	@Test
	public void tes1t() {
		Main main = new Main();
		
		main.processInput("uci");
		main.processInput("isready");
		main.processInput("ucinewgame");
		main.processInput("isready");
		main.processInput("position startpos moves e2e4");
		
		Game game = main.getEngine().getGame();
		Assert.assertEquals("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1", fenCode(game));
	}
	
	
	private String fenCode(Game board) {
		FENEncoder coder = new FENEncoder();
		board.getChessPositionReader().constructBoardRepresentation(coder);
		return coder.getResult();
	}		
}
