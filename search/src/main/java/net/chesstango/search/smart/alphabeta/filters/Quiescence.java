package net.chesstango.search.smart.alphabeta.filters;

import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.sorters.MoveSorter;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import java.util.Iterator;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class Quiescence implements AlphaBetaFilter {
    @Setter
    private AlphaBetaFilter next;

    @Setter
    private MoveSorter moveSorter;

    @Setter
    private GameEvaluator gameEvaluator;
    private Game game;

    @Override
    public void beforeSearch(Game game) {
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
        int maxValue = gameEvaluator.evaluate();
        if (maxValue >= beta) {
            return TranspositionEntry.encode(maxValue);
        }

        Move bestMove = null;
        boolean search = true;

        List<Move> sortedMoves = moveSorter.getSortedMoves();
        Iterator<Move> moveIterator = sortedMoves.iterator();
        while (moveIterator.hasNext() && search) {
            Move move = moveIterator.next();

            if (!move.isQuiet()) {
                game = game.executeMove(move);

                long bestMoveAndValue = next.minimize(currentPly + 1, Math.max(maxValue, alpha), beta);
                int currentValue = TranspositionEntry.decodeValue(bestMoveAndValue);

                if (currentValue > maxValue) {
                    maxValue = currentValue;
                    bestMove = move;

                    if (maxValue >= beta) {
                        search = false;
                    }
                }

                game = game.undoMove();
            }
        }
        return TranspositionEntry.encode(bestMove, maxValue);
    }

    @Override
    public long minimize(final int currentPly, final int alpha, final int beta) {
        int minValue = gameEvaluator.evaluate();
        if (minValue <= alpha) {
            return TranspositionEntry.encode(minValue);
        }

        Move bestMove = null;
        boolean search = true;

        List<Move> sortedMoves = moveSorter.getSortedMoves();
        Iterator<Move> moveIterator = sortedMoves.iterator();
        while (moveIterator.hasNext() && search) {
            Move move = moveIterator.next();

            if (!move.isQuiet()) {
                game = game.executeMove(move);

                long bestMoveAndValue = next.maximize(currentPly + 1, alpha, Math.min(minValue, beta));
                int currentValue = TranspositionEntry.decodeValue(bestMoveAndValue);

                if (currentValue < minValue) {
                    minValue = currentValue;
                    bestMove = move;

                    if (minValue <= alpha) {
                        search = false;
                    }
                }

                game = game.undoMove();
            }
        }
        return TranspositionEntry.encode(bestMove, minValue);
    }


    public void stopSearching() {
    }

}
