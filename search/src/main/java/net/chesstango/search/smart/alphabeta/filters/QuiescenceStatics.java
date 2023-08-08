package net.chesstango.search.smart.alphabeta.filters;

import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchContext;

/**
 * @author Mauricio Coria
 */
public class QuiescenceStatics implements AlphaBetaFilter {
    private AlphaBetaFilter next;
    private int[] visitedNodesQuiescenceCounter;
    private int maxPly;

    @Override
    public void beforeSearch(Game game, int maxDepth) {
        this.visitedNodesQuiescenceCounter = new int[30];
    }

    @Override
    public void afterSearch(SearchMoveResult result) {
        if (result != null) {
            result.setVisitedNodesQuiescenceCounter(visitedNodesQuiescenceCounter);
        }
        this.visitedNodesQuiescenceCounter = null;
    }

    @Override
    public void beforeSearchByDepth(SearchContext context) {
        this.maxPly = context.getMaxPly();
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {
        if (result != null) {
            result.setVisitedNodesQuiescenceCounter(visitedNodesQuiescenceCounter);
        }
    }

    @Override
    public void stopSearching() {

    }

    @Override
    public void reset() {

    }

    @Override
    public long maximize(final int currentPly, final int alpha, final int beta) {
        visitedNodesQuiescenceCounter[currentPly - maxPly]++;
        return next.maximize(currentPly, alpha, beta);
    }

    @Override
    public long minimize(final int currentPly, final int alpha, final int beta) {
        visitedNodesQuiescenceCounter[currentPly - maxPly]++;
        return next.minimize(currentPly, alpha, beta);
    }


    public void setNext(AlphaBetaFilter next) {
        this.next = next;
    }
}
