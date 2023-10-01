package net.chesstango.search.smart.alphabeta.filters;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.sorters.MoveSorter;
import net.chesstango.search.smart.transposition.TranspositionBound;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import java.util.Iterator;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class AlphaBeta implements AlphaBetaFilter {

    private AlphaBetaFilter next;

    private MoveSorter moveSorter;

    private Game game;

    @Override
    public void beforeSearch(Game game, int maxDepth) {
        this.game = game;
    }

    @Override
    public void afterSearch(SearchMoveResult result) {
    }

    @Override
    public void beforeSearchByDepth(SearchContext context) {
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {
    }

    @Override
    public void reset() {
    }

    @Override
    public long maximize(final int currentPly, final int alpha, final int beta) {
        Move bestMove = null;
        boolean search = true;
        int maxValue = GameEvaluator.INFINITE_NEGATIVE;
        TranspositionBound bound = TranspositionBound.EXACT;

        List<Move> sortedMoves = moveSorter.getSortedMoves();
        Iterator<Move> moveIterator = sortedMoves.iterator();
        while (moveIterator.hasNext() && search) {
            Move move = moveIterator.next();

            game = game.executeMove(move);

            long bestMoveAndValue = next.minimize(currentPly + 1, Math.max(maxValue, alpha), beta);
            int currentValue = TranspositionEntry.decodeValue(bestMoveAndValue);
            if (currentValue > maxValue) {
                maxValue = currentValue;
                bestMove = move;
                if (maxValue >= beta) {
                    search = false;
                    bound = TranspositionBound.LOWER_BOUND;
                }
            }

            game = game.undoMove();
        }

        return TranspositionEntry.encode(bound, bestMove, maxValue);
    }

    @Override
    public long minimize(final int currentPly, final int alpha, final int beta) {
        Move bestMove = null;
        boolean search = true;
        int minValue = GameEvaluator.INFINITE_POSITIVE;
        TranspositionBound bound = TranspositionBound.EXACT;

        List<Move> sortedMoves = moveSorter.getSortedMoves();
        Iterator<Move> moveIterator = sortedMoves.iterator();
        while (moveIterator.hasNext() && search) {
            Move move = moveIterator.next();

            game = game.executeMove(move);

            long bestMoveAndValue = next.maximize(currentPly + 1, alpha, Math.min(minValue, beta));
            int currentValue = TranspositionEntry.decodeValue(bestMoveAndValue);
            if (currentValue < minValue) {
                minValue = currentValue;
                bestMove = move;
                if (minValue <= alpha) {
                    search = false;
                    bound = TranspositionBound.UPPER_BOUND;
                }
            }

            game = game.undoMove();
        }

        return TranspositionEntry.encode(bound, bestMove, minValue);

    }

    @Override
    public void stopSearching() {
    }

    public void setMoveSorter(MoveSorter moveSorter) {
        this.moveSorter = moveSorter;
    }

    public void setNext(AlphaBetaFilter next) {
        this.next = next;
    }

}
