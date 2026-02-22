package net.chesstango.search.smart.features.evaluator.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaHelper;

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
    public long maximize(int currentPly, int alpha, int beta) {
        return AlphaBetaHelper.encode(evaluator.evaluate());
    }

    @Override
    public long minimize(int currentPly, int alpha, int beta) {
        return AlphaBetaHelper.encode(evaluator.evaluate());
    }
}
