package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.smart.SearchContext;

import java.util.List;
import java.util.Set;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaStatistics implements AlphaBetaFilter {
    private AlphaBetaFilter next;

    private int[] visitedNodesCounter;

    private List<Set<Move>> distinctMoves;

    @Override
    public void init(SearchContext context) {
        this.visitedNodesCounter = context.getVisitedNodesCounters();
        this.distinctMoves = context.getDistinctMovesPerLevel();
        if (visitedNodesCounter == null || distinctMoves == null) {
            throw new RuntimeException("Context not initiated");
        }
    }

    @Override
    public int maximize(final Game game, final int currentPly, final int alpha, final int beta) {
        increaseVisitedNodesCounter(currentPly);

        trackMove(game, currentPly);

        return next.maximize(game, currentPly, alpha, beta);
    }

    @Override
    public int minimize(final Game game, final int currentPly, final int alpha, final int beta) {
        increaseVisitedNodesCounter(currentPly);

        trackMove(game, currentPly);

        return next.minimize(game, currentPly, alpha, beta);
    }

    @Override
    public void stopSearching() {
        next.stopSearching();
    }

    public void setNext(AlphaBetaFilter next) {
        this.next = next;
    }

    protected void increaseVisitedNodesCounter(int currentPly) {
        visitedNodesCounter[currentPly - 1]++;
    }

    protected void trackMove(Game game, int currentPly) {
        Move lastMove = game.getState().getPreviosGameState().selectedMove;

        Set<Move> currentMoveSet = distinctMoves.get(currentPly - 1);

        currentMoveSet.add(lastMove);
    }
}
