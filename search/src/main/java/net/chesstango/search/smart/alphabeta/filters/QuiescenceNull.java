package net.chesstango.search.smart.alphabeta.filters;

import net.chesstango.board.Game;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchByCycleListener;

/**
 * @author Mauricio Coria
 */
public class QuiescenceNull implements AlphaBetaFilter, SearchByCycleListener {

    private GameEvaluator evaluator;

    private Game game;

    @Override
    public void beforeSearch(Game game) {
        this.game = game;
    }

    @Override
    public void afterSearch(SearchMoveResult result) {
    }

    @Override
    public long minimize(final int currentPly, final int alpha, final int beta) {
        return evaluator.evaluate();
    }

    @Override
    public long maximize(final int currentPly, final int alpha, final int beta) {
        return evaluator.evaluate();
    }

    public void setGameEvaluator(GameEvaluator evaluator) {
        this.evaluator = evaluator;
    }

}
