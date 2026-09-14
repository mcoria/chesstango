package net.chesstango.search.alphabeta.evaluator.filters;

import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.alphabeta.AlphaBetaFilter;

/**
 * @author Mauricio Coria
 */
public class LoopEvaluation implements AlphaBetaFilter, Acceptor {

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int alphaBeta(int currentPly, int alpha, int beta) {
        return 0;
    }

}
