package net.chesstango.ai;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;

/**
 * @author Mauricio Coria
 *
 */
public interface BestMoveFinder {
	
	Move searchBestMove(Game game, int depth);

    void stopSearching();
}
