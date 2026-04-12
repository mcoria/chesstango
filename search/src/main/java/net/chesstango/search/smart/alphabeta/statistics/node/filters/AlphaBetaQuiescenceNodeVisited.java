package net.chesstango.search.smart.alphabeta.statistics.node.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.statistics.node.NodeCounters;

/**
 * @author Mauricio Coria
 */
@Setter
public class AlphaBetaQuiescenceNodeVisited implements AlphaBetaFilter, Acceptor {

    @Getter
    private AlphaBetaFilter next;

    @Setter
    private int depth;

    private NodeCounters nodeCounters;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int maximize(final int currentPly, final int alpha, final int beta) {
        updateCounters(currentPly);
        return next.maximize(currentPly, alpha, beta);
    }

    @Override
    public int minimize(final int currentPly, final int alpha, final int beta) {
        updateCounters(currentPly);
        return next.minimize(currentPly, alpha, beta);
    }

    protected void updateCounters(final int currentPly) {
        nodeCounters.increaseQuiescenceCounter();

        if (currentPly == depth) {
            nodeCounters.increaseRegularCounter();
        }

        nodeCounters.increaseVisitedCounter(currentPly);
    }
}

