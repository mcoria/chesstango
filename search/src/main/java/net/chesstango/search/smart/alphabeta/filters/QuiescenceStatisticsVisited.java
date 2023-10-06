package net.chesstango.search.smart.alphabeta.filters;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchContext;

/**
 * @author Mauricio Coria
 */
public class QuiescenceStatisticsVisited implements AlphaBetaFilter {
    private AlphaBetaFilter next;
    private int[] visitedNodesCounters;
    private int maxPly;

    @Override
    public void beforeSearch(Game game, int maxDepth) {
    }

    @Override
    public void afterSearch(SearchMoveResult result) {
        this.visitedNodesCounters = null;
    }

    @Override
    public void beforeSearchByDepth(SearchContext context) {
        this.maxPly = context.getMaxPly();
        this.visitedNodesCounters = context.getVisitedNodesCountersQuiescence();
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {
    }

    @Override
    public void stopSearching() {
    }

    @Override
    public void reset() {
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


    public void setNext(AlphaBetaFilter next) {
        this.next = next;
    }

    protected void updateCounters(final int currentPly) {
        final int qLevel = currentPly - maxPly;
        visitedNodesCounters[qLevel - 1]++;
    }
}