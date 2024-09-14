package net.chesstango.search.smart.features.statistics.node.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.SearchByDepthContext;
import net.chesstango.search.smart.SearchByDepthListener;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;

/**
 * @author Mauricio Coria
 */
public class QuiescenceStatisticsVisited implements AlphaBetaFilter, SearchByCycleListener, SearchByDepthListener {

    @Setter
    @Getter
    private AlphaBetaFilter next;
    private int[] visitedNodesCounters;
    private int maxPly;

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.visitedNodesCounters = context.getVisitedNodesCountersQuiescence();
    }

    @Override
    public void beforeSearchByDepth(SearchByDepthContext context) {
        this.maxPly = context.getMaxPly();
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
        final int qLevel = currentPly - maxPly;
        visitedNodesCounters[qLevel - 1]++;
    }
}
