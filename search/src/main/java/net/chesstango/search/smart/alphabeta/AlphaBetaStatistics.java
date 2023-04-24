package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.smart.SearchContext;

import java.util.Set;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaStatistics implements AlphaBetaFilter {
    private AlphaBetaFilter next;
    private int[] visitedNodesCounter;
    private int[] expectedNodesCounters;
    private Set<Move>[] distinctMoves;

    @Override
    public void init(Game game, SearchContext context) {
        this.visitedNodesCounter = context.getVisitedNodesCounters();
        this.expectedNodesCounters = context.getExpectedNodesCounters();
        this.distinctMoves = context.getDistinctMovesPerLevel();
        if (visitedNodesCounter == null || distinctMoves == null || expectedNodesCounters == null) {
            throw new RuntimeException("Context not initiated");
        }
        expectedNodesCounters[0] = game.getPossibleMoves().size();
    }

    @Override
    public int maximize(final Game game, final int currentPly, final int alpha, final int beta) {
        updateCounters(game, currentPly);

        return next.maximize(game, currentPly, alpha, beta);
    }

    @Override
    public int minimize(final Game game, final int currentPly, final int alpha, final int beta) {
        updateCounters(game, currentPly);

        return next.minimize(game, currentPly, alpha, beta);
    }


    @Override
    public void stopSearching() {
        next.stopSearching();
    }

    public void setNext(AlphaBetaFilter next) {
        this.next = next;
    }

    protected void updateCounters(final Game game, final int currentPly){
        increaseVisitedNodesCounter(currentPly);

        updateDistinctMoves(game, currentPly);

        increaseExpectedNodesCounter(game, currentPly);
    }

    protected void increaseVisitedNodesCounter(int currentPly) {
        visitedNodesCounter[currentPly - 1]++;
    }

    protected void increaseExpectedNodesCounter(Game game, int currentPly) {
        expectedNodesCounters[currentPly] += game.getPossibleMoves().size();
    }

    protected void updateDistinctMoves(Game game, int currentPly) {
        Move lastMove = game.getState().getPreviosState().getSelectedMove();

        Set<Move> currentMoveSet = distinctMoves[currentPly - 1];

        currentMoveSet.add(lastMove);
    }
}
