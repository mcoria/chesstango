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
		//bestMoveFinder = new MinMax(getMaxLevel());
		bestMoveFinder = new MinMaxPrunning(getMaxLevel());
	}

}