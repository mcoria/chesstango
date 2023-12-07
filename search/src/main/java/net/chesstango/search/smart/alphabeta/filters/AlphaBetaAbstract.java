package net.chesstango.search.smart.alphabeta.filters;

import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import java.util.Iterator;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public abstract class AlphaBetaAbstract implements AlphaBetaFilter {

    @Setter
    private AlphaBetaFilter next;

    protected Game game;

    protected abstract List<Move> getSortedMoves();

    @Override
    public long maximize(final int currentPly, final int alpha, final int beta) {
        boolean search = true;
        Move bestMove = null;
        int maxValue = GameEvaluator.INFINITE_NEGATIVE;

        List<Move> sortedMoves = getSortedMoves();
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
                }
            }
            game = game.undoMove();
        }
        return TranspositionEntry.encode(bestMove, maxValue);
    }

    @Override
    public long minimize(final int currentPly, final int alpha, final int beta) {
        boolean search = true;
        Move bestMove = null;
        int minValue = GameEvaluator.INFINITE_POSITIVE;

        List<Move> sortedMoves = getSortedMoves();
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
                }
            }
            game = game.undoMove();
        }
        return TranspositionEntry.encode(bestMove, minValue);
    }

}
