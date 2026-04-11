package net.chesstango.search.smart.alphabeta.evaluator.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;

/**
 * @author Mauricio Coria
 */
@Setter
@Getter
public class AlphaBetaEvaluation implements AlphaBetaFilter {
    private Evaluator evaluator;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int maximize(int currentPly, int alpha, int beta) {
        return evaluator.evaluate();
    }

    @Override
    public int minimize(int currentPly, int alpha, int beta) {
        return evaluator.evaluate();
    }
}
