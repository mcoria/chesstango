package net.chesstango.search.smart.alphabeta.filters;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchContext;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaStatistics implements AlphaBetaFilter {
    private AlphaBetaFilter next;
    private int[] visitedNodesCounters;
    private int[] expectedNodesCounters;
    private Set<Move>[] distinctMovesPerLevel;
    private Game game;

    @Override
    public void beforeSearch(Game game, int maxDepth) {
        this.game = game;
        this.visitedNodesCounters = new int[30];
        this.expectedNodesCounters = new int[30];
        this.distinctMovesPerLevel = new Set[30];
        IntStream.range(0, 30).forEach(i -> this.distinctMovesPerLevel[i] = new HashSet<>());
    }

    @Override
    public void afterSearch(SearchMoveResult result) {
        if (result != null) {
            result.setVisitedNodesCounters(visitedNodesCounters);
            result.setExpectedNodesCounters(expectedNodesCounters);
            result.setDistinctMovesPerLevel(distinctMovesPerLevel);
        }
        this.game = null;
        this.visitedNodesCounters = null;
        this.expectedNodesCounters = null;
        this.distinctMovesPerLevel = null;
    }

    @Override
    public void beforeSearchByDepth(SearchContext context) {
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {
        if (result != null) {
            result.setVisitedNodesCounters(visitedNodesCounters);
            result.setExpectedNodesCounters(expectedNodesCounters);
            result.setDistinctMovesPerLevel(distinctMovesPerLevel);
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
        expectedNodesCounters[currentPly] += game.getPossibleMoves().size();
        updateDistinctMoves(currentPly);
        if (currentPly > 0) {
            visitedNodesCounters[currentPly - 1]++;
        }
    }


    protected void updateDistinctMoves(int currentPly) {
        Set<Move> currentMoveSet = distinctMovesPerLevel[currentPly];
        for (Move move : game.getPossibleMoves()) {
            currentMoveSet.add(move);
        }
    }
}
