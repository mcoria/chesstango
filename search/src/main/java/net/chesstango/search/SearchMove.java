package net.chesstango.search;

import net.chesstango.board.Game;
import net.chesstango.evaluation.GameEvaluator;

/**
 * @author Mauricio Coria
 */
public interface SearchMove {
    SearchMoveResult searchBestMove(Game game);

    SearchMoveResult searchBestMove(Game game, int depth);

    void stopSearching();

    void setGameEvaluator(GameEvaluator evaluator);
}
