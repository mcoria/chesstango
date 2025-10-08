package net.chesstango.search.smart.alphabeta.filters;

import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.Visitor;

/**
 * @author Mauricio Coria
 */
public class QuiescenceNull implements AlphaBetaFilter {

    private Evaluator evaluator;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
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
