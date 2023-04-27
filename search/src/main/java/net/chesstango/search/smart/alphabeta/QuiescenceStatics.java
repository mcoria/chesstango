package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Game;
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
    public int minimize(Game game, final int currentPly, final int alpha, final int beta) {
        visitedNodesQuiescenceCounter[currentPly - maxPly]++;
        return next.minimize(game, currentPly, alpha, beta);
    }

    @Override
    public int maximize(Game game, final int currentPly, final int alpha, final int beta) {
        visitedNodesQuiescenceCounter[currentPly - maxPly]++;
        return next.maximize(game, currentPly, alpha, beta);
    }

    @Override
    public void stopSearching() {
    }

    public void setNext(AlphaBetaFilter next) {
        this.next = next;
    }
}
