package net.chesstango.search;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.GameEvaluator;

/**
 * @author Mauricio Coria
 *
 */
public interface SearchMove {
    Move searchBestMove(Game game);

	Move searchBestMove(Game game, int depth);

    void stopSearching();

    void setGameEvaluator(GameEvaluator evaluator);
}
