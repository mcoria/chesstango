package net.chesstango.search.smart.alphabeta.filters;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.statistics.NodeStatistics;

/**
 * @author Mauricio Coria
 */
public class QuiescenceStatistics implements AlphaBetaFilter {
    private AlphaBetaFilter next;
    private int[] visitedNodesCounters;
    private int[] expectedNodesCounters;
    private Game game;
    private int maxPly;

    @Override
    public void beforeSearch(Game game, int maxDepth) {
        this.game = game;
        this.visitedNodesCounters = new int[30];
        this.expectedNodesCounters = new int[30];
    }

    @Override
    public void afterSearch(SearchMoveResult result) {
        if (result != null) {
            result.setQuiescenceNodeStatistics(new NodeStatistics(expectedNodesCounters, visitedNodesCounters));
        }
        this.game = null;
        this.visitedNodesCounters = null;
        this.expectedNodesCounters =  null;
    }

    @Override
    public void beforeSearchByDepth(SearchContext context) {
        this.maxPly = context.getMaxPly();
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
        int expectedMoves = 0;
        for (Move move: game.getPossibleMoves()) {
            if(Quiescence.isNotQuiet(move)){
                expectedMoves++;
            }
        }
        expectedNodesCounters[qLevel] += expectedMoves;
        if (qLevel > 0) {
            visitedNodesCounters[qLevel - 1]++;
        }
    }
}
