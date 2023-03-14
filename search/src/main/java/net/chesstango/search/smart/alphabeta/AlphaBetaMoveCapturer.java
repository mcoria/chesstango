package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;

import java.util.List;
import java.util.Set;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaMoveCapturer implements AlphaBetaSearch {
    private AlphaBetaSearch next;

    @Override
    public int maximize(Game game, int currentPly, int alpha, int beta, SearchContext context) {
        int[] visitedNodesCounter = context.getVisitedNodesCounter();
        visitedNodesCounter[currentPly - 1]++;

        trackMove(game, currentPly, context);

        return next.maximize(game, currentPly, alpha, beta, context);
    }

    @Override
    public int minimize(Game game, int currentPly, int alpha, int beta, SearchContext context) {
        int[] visitedNodesCounter = context.getVisitedNodesCounter();
        visitedNodesCounter[currentPly - 1]++;

        trackMove(game, currentPly, context);

        return next.minimize(game, currentPly, alpha, beta, context);
    }


    protected void trackMove(Game game, int currentPly, SearchContext context){
        Move lastMove = game.getState().getPreviosGameState().selectedMove;

        List<Set<Move>> distinctMoves = context.getDistinctMoves();

        Set<Move> currentMoveSet = distinctMoves.get(currentPly - 1);

        currentMoveSet.add(lastMove);
    }

    @Override
    public void stopSearching() {
    }

    public void setNext(AlphaBetaSearch next) {
        this.next = next;
    }
}
