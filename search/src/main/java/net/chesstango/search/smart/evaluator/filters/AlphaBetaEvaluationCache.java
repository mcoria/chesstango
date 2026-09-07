package net.chesstango.search.smart.evaluator.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.AlphaBetaFilter;

/**
 * @author Mauricio Coria
 */
@Setter
@Getter
public class AlphaBetaEvaluationCache implements AlphaBetaFilter, Acceptor {

    private AlphaBetaFilter next;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int alphaBeta(int currentPly, int alpha, int beta) {
        return next.alphaBeta(currentPly, alpha, beta);
    }
}
