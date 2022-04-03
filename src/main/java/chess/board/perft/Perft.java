/**
 * 
 */
package chess.board.perft;

import chess.board.Game;

/**
 * @author Mauricio Coria
 *
 */
public interface Perft {

	PerftResult start(Game game, int maxLevel);
	
	void printResult(PerftResult result);

}