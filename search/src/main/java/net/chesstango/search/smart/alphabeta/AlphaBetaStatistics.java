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

    @Override
    public int maximize(final Game game, final int currentPly, final int alpha, final int beta, final SearchContext context) {
        increaseVisitedNodesCounter(currentPly, context);

        trackMove(game, currentPly, context);

        return next.maximize(game, currentPly, alpha, beta, context);
    }

    @Override
    public int minimize(final Game game, final int currentPly, final int alpha, final int beta, final SearchContext context) {
        increaseVisitedNodesCounter(currentPly, context);

        trackMove(game, currentPly, context);

        return next.minimize(game, currentPly, alpha, beta, context);
    }

    @Override
    public void stopSearching() {
        next.stopSearching();
    }

    public void setNext(AlphaBetaFilter next) {
        this.next = next;
    }

    protected void increaseVisitedNodesCounter(int currentPly, SearchContext context) {
        int[] visitedNodesCounter = context.getVisitedNodesCounters();
        visitedNodesCounter[currentPly - 1]++;
    }

    protected void trackMove(Game game, int currentPly, SearchContext context){
        Move lastMove = game.getState().getPreviosGameState().selectedMove;

        List<Set<Move>> distinctMoves = context.getDistinctMovesPerLevel();

        Set<Move> currentMoveSet = distinctMoves.get(currentPly - 1);

        currentMoveSet.add(lastMove);
    }
}
