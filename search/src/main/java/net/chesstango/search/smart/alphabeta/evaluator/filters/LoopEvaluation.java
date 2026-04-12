package net.chesstango.search.smart.alphabeta.evaluator.filters;

import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;

/**
 * @author Mauricio Coria
 */
public class LoopEvaluation implements AlphaBetaFilter, Acceptor {

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int maximize(int currentPly, int alpha, int beta) {
        return 0;
    }

    @Override
    public int minimize(int currentPly, int alpha, int beta) {
        return 0;
    }
}
