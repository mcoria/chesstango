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
    public void init(Game game, SearchContext context) {
        this.maxPly = context.getMaxPly();
        this.visitedNodesQuiescenceCounter = context.getVisitedNodesQuiescenceCounter();
    }

    @Override
    public void close(SearchMoveResult result) {
        result.setVisitedNodesQuiescenceCounter(visitedNodesQuiescenceCounter);
    }

    @Override
    public long minimize(final int currentPly, final int alpha, final int beta) {
        visitedNodesQuiescenceCounter[currentPly - maxPly]++;
        return next.minimize(currentPly, alpha, beta);
    }

    @Override
    public long maximize(final int currentPly, final int alpha, final int beta) {
        visitedNodesQuiescenceCounter[currentPly - maxPly]++;
        return next.maximize(currentPly, alpha, beta);
    }


    public void setNext(AlphaBetaFilter next) {
        this.next = next;
    }
}