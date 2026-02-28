package net.chesstango.search.smart.alphabeta.statistics.node.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;

/**
 * @author Mauricio Coria
 */
@Setter
@Getter
public class QuiescenceStatisticsVisited implements AlphaBetaFilter {

    private AlphaBetaFilter next;

    private long[] visitedNodesCounters;

    private int depth;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public long maximize(final int currentPly, final int alpha, final int beta) {
        updateCounters(currentPly);
        return next.maximize(currentPly, alpha, beta);
    }

    @Override
    public long minimize(final int currentPly, final int alpha, final int beta) {
        updateCounters(currentPly);
        return next.minimize(currentPly, alpha, beta);
    }

    protected void updateCounters(final int currentPly) {
        final int qLevel = currentPly - depth;
        visitedNodesCounters[qLevel - 1]++;
    }
}
