package net.chesstango.search.smart.alphabeta.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.features.transposition.TranspositionEntry;
import net.chesstango.search.smart.sorters.MoveSorter;

import java.util.Iterator;

/**
 * @author Mauricio Coria
 */
public class AlphaBeta implements AlphaBetaFilter, SearchByCycleListener {

    @Setter
    @Getter
    private AlphaBetaFilter next;

    @Setter
    @Getter
    private MoveSorter moveSorter;

    protected Game game;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.game = context.getGame();
    }

    @Override
    public long maximize(final int currentPly, final int alpha, final int beta) {
        boolean search = true;
        Move bestMove = null;
        int maxValue = Evaluator.INFINITE_NEGATIVE;

        Iterable<Move> sortedMoves = moveSorter.getOrderedMoves(currentPly);
        Iterator<Move> moveIterator = sortedMoves.iterator();
        while (moveIterator.hasNext() && search) {
            Move move = moveIterator.next();
            move.executeMove();

            long bestMoveAndValue = next.minimize(currentPly + 1, Math.max(maxValue, alpha), beta);
            int currentValue = TranspositionEntry.decodeValue(bestMoveAndValue);
            if (currentValue > maxValue) {
                maxValue = currentValue;
                bestMove = move;
                if (maxValue >= beta) {
                    search = false;
                } else if (maxValue == Evaluator.WHITE_WON) {
                    search = false;
                }
            }
            move.undoMove();
        }
        return TranspositionEntry.encode(bestMove, maxValue);
    }

    @Override
    public long minimize(final int currentPly, final int alpha, final int beta) {
        boolean search = true;
        Move bestMove = null;
        int minValue = Evaluator.INFINITE_POSITIVE;

        Iterable<Move> sortedMoves = moveSorter.getOrderedMoves(currentPly);
        Iterator<Move> moveIterator = sortedMoves.iterator();
        while (moveIterator.hasNext() && search) {
            Move move = moveIterator.next();
            move.executeMove();

            long bestMoveAndValue = next.maximize(currentPly + 1, alpha, Math.min(minValue, beta));
            int currentValue = TranspositionEntry.decodeValue(bestMoveAndValue);
            if (currentValue < minValue) {
                minValue = currentValue;
                bestMove = move;
                if (minValue <= alpha) {
                    search = false;
                } else if (minValue == Evaluator.BLACK_WON) {
                    search = false;
                }
            }
            move.undoMove();
        }
        return TranspositionEntry.encode(bestMove, minValue);
    }

}
