package net.chesstango.search.smart.alphabeta.filters;

import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchByDepthListener;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.SearchCycleListener;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaStatisticsExpected implements AlphaBetaFilter, SearchCycleListener, SearchByDepthListener {

    @Setter
    private AlphaBetaFilter next;
    private int[] expectedNodesCounters;
    private Game game;

    @Override
    public void beforeSearch(Game game) {
        this.game = game;
    }

    @Override
    public void afterSearch(SearchMoveResult result) {
        this.game = null;
        this.expectedNodesCounters =  null;
    }

    @Override
    public void beforeSearchByDepth(SearchContext context) {
        this.expectedNodesCounters = context.getExpectedNodesCounters();
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
        expectedNodesCounters[currentPly] += game.getPossibleMoves().size();
    }
}
