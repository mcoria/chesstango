package net.chesstango.search.smart.alphabeta.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaStatisticsVisited implements AlphaBetaFilter, SearchByCycleListener {
    @Setter
    @Getter
    private AlphaBetaFilter next;
    private int[] visitedNodesCounters;

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.visitedNodesCounters = context.getVisitedNodesCounters();
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
        visitedNodesCounters[currentPly - 1]++;
    }
}
