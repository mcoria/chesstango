/**
 * 
 */
package chess.ai.imp.smart;

import chess.ai.BestMoveFinder;
import chess.board.Game;
import chess.board.builder.imp.GameBuilder;
import chess.board.representations.fen.FENDecoder;

/**
 * @author Mauricio Coria
 *
 */
public class AbstractSmartTest {

	protected BestMoveFinder bestMoveFinder = null;
	

	public void setUp() {
		//bestMoveFinder = new MinMax();
		bestMoveFinder = new MinMaxPrunning();
	}

	protected Game getGame(String string) {
		GameBuilder builder = new GameBuilder();
	
		FENDecoder parser = new FENDecoder(builder);
		
		parser.parseFEN(string);
		
		return builder.getResult();
	}

}