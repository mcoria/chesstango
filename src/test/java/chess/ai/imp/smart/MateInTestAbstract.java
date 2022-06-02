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
public abstract class MateInTestAbstract {

	protected BestMoveFinder bestMoveFinder = null;
	

	protected abstract int getMaxLevel();

	public void setUp() {
		//bestMoveFinder = new MinMaxPrunning(getMaxLevel());
		//bestMoveFinder = new MinMax(getMaxLevel());
		//bestMoveFinder = new NegaMinMax(getMaxLevel());
		bestMoveFinder = new NegaMinMaxPrunning(getMaxLevel());
	}

	protected Game getGame(String string) {
		GameBuilder builder = new GameBuilder();
	
		FENDecoder parser = new FENDecoder(builder);
		
		parser.parseFEN(string);
		
		return builder.getResult();
	}

}