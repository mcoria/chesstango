package net.chesstango.search.smart.alphabeta.filters;

import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchByDepthListener;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.SearchByCycleListener;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaStatisticsVisited implements AlphaBetaFilter, SearchByCycleListener, SearchByDepthListener {
    @Setter
    private AlphaBetaFilter next;
    private int[] visitedNodesCounters;

    @Override
    public void beforeSearch(Game game) {
    }

    @Override
    public void afterSearch(SearchMoveResult result) {
        this.visitedNodesCounters = null;
    }

    @Override
    public void beforeSearchByDepth(SearchContext context) {
        this.visitedNodesCounters = context.getVisitedNodesCounters();
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {
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
