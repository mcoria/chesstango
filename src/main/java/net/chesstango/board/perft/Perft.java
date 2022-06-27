/**
 * 
 */
package net.chesstango.board.perft;

import net.chesstango.board.Game;

/**
 * @author Mauricio Coria
 *
 */
public interface Perft {

	PerftResult start(Game game, int maxLevel);
	
	void printResult(PerftResult result);

}