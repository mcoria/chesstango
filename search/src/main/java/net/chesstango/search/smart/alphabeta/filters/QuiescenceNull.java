package net.chesstango.search.smart.alphabeta.filters;

import net.chesstango.board.Game;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;

/**
 * @author Mauricio Coria
 */
public class QuiescenceNull implements AlphaBetaFilter, SearchByCycleListener {

    private Evaluator evaluator;

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        Game game = context.getGame();
    }

    @Override
    public long minimize(final int currentPly, final int alpha, final int beta) {
        return evaluator.evaluate();
    }

    @Override
    public long maximize(final int currentPly, final int alpha, final int beta) {
        return evaluator.evaluate();
    }

    public void setGameEvaluator(Evaluator evaluator) {
        this.evaluator = evaluator;
    }

}
