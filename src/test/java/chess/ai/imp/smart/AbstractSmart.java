/**
 * 
 */
package chess.ai.imp.smart;

import chess.ai.BestMoveFinder;
import chess.board.Game;
import chess.board.builder.imp.GameBuilder;
import chess.board.debug.builder.DebugChessFactory;
import chess.board.fen.FENDecoder;

/**
 * @author Mauricio Coria
 *
 */
public class AbstractSmart {

	protected BestMoveFinder bestMoveFinder = null;
	

	public void setUp() {
		//bestMoveFinder = new SmartMinMax(); 
		bestMoveFinder = new MinMaxPrunning();
	}

	protected Game getGame(String string) {		
		GameBuilder builder = new GameBuilder(new DebugChessFactory());
	
		FENDecoder parser = new FENDecoder(builder);
		
		parser.parseFEN(string);
		
		return builder.getResult();
	}

}