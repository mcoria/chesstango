package net.chesstango.search.smart.alphabeta.core.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;
import net.chesstango.search.smart.sorters.MoveSorter;

import java.util.Iterator;

/**
 * @author Mauricio Coria
 */
@Setter
public class AlphaBeta implements AlphaBetaFilter, Acceptor {

    @Getter
    private AlphaBetaFilter next;

    @Getter
    private MoveSorter moveSorter;

    private Move[] bestMoves;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int alphaBeta(final int currentPly, final int alpha, final int beta) {
        boolean search = true;
        bestMoves[currentPly] = null;
        int maxValue = Evaluator.INFINITE_NEGATIVE;

        Iterable<Move> sortedMoves = moveSorter.getOrderedMoves(currentPly);
        Iterator<Move> moveIterator = sortedMoves.iterator();
        while (moveIterator.hasNext() && search) {
            Move move = moveIterator.next();
            move.executeMove();

            int currentValue = next.alphaBeta(currentPly + 1, Math.max(maxValue, alpha), beta);
            if (currentValue > maxValue) {
                maxValue = currentValue;
                bestMoves[currentPly] = move;
                if (maxValue >= beta) {
                    search = false;
                } else if (maxValue == Evaluator.WON) {
                    search = false;
                }
            }
            move.undoMove();
        }

        return maxValue;
    }

}
