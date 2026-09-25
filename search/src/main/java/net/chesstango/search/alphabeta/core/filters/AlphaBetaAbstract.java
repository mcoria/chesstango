package net.chesstango.search.alphabeta.core.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.alphabeta.AlphaBetaFilter;
import net.chesstango.search.sorters.MoveSorter;

import java.util.Iterator;

/**
 * @author Mauricio Coria
 */
@Setter
public class AlphaBetaAbstract {
    @Getter
    private AlphaBetaFilter next;

    @Getter
    private MoveSorter moveSorter;

    private Move[] bestMoves;

    public int alphaBeta(final int currentPly, final int alpha, final int beta) {
        boolean search = true;
        bestMoves[currentPly] = null;
        int bestValue = Evaluator.INFINITE_NEGATIVE;

        Iterable<Move> sortedMoves = moveSorter.getOrderedMoves(currentPly);
        Iterator<Move> moveIterator = sortedMoves.iterator();
        while (moveIterator.hasNext() && search) {
            Move move = moveIterator.next();
            move.executeMove();

            int currentValue = next.alphaBeta(currentPly, Math.max(bestValue, alpha), beta);
            if (currentValue > bestValue) {
                bestValue = currentValue;
                bestMoves[currentPly] = move;
                if (bestValue >= beta || bestValue == Evaluator.WON) {
                    search = false;
                }
            }

            move.undoMove();
        }

        return bestValue;
    }
}
