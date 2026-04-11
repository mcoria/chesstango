package net.chesstango.search.smart.alphabeta.quiescence;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;
import net.chesstango.search.smart.sorters.MoveSorter;

import java.util.Iterator;

/**
 * @author Mauricio Coria
 */
public class Quiescence implements AlphaBetaFilter {
    @Setter
    @Getter
    private AlphaBetaFilter next;

    @Setter
    @Getter
    private MoveSorter moveSorter;

    @Setter
    @Getter
    private Evaluator evaluator;

    @Setter
    private Move[] bestMoves;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int maximize(final int currentPly, final int alpha, final int beta) {
        boolean search = true;
        bestMoves[currentPly] = null;
        int maxValue = evaluator.evaluate();
        if (maxValue >= beta) {
            return maxValue;
        }

        Iterable<Move> sortedMoves = moveSorter.getOrderedMoves(currentPly);
        Iterator<Move> moveIterator = sortedMoves.iterator();
        while (moveIterator.hasNext() && search) {
            Move move = moveIterator.next();
            if (!move.isQuiet()) {
                move.executeMove();

                int currentValue = next.minimize(currentPly + 1, Math.max(maxValue, alpha), beta);
                if (currentValue > maxValue) {
                    maxValue = currentValue;
                    bestMoves[currentPly] = move;
                    if (maxValue >= beta) {
                        search = false;
                    } else if (maxValue == Evaluator.WHITE_WON) {
                        search = false;
                    }
                }

                move.undoMove();
            }
        }
        return maxValue;
    }

    @Override
    public int minimize(final int currentPly, final int alpha, final int beta) {
        boolean search = true;
        bestMoves[currentPly] = null;
        int minValue = evaluator.evaluate();
        if (minValue <= alpha) {
            return minValue;
        }

        Iterable<Move> sortedMoves = moveSorter.getOrderedMoves(currentPly);
        Iterator<Move> moveIterator = sortedMoves.iterator();
        while (moveIterator.hasNext() && search) {
            Move move = moveIterator.next();
            if (!move.isQuiet()) {
                move.executeMove();

                int currentValue = next.maximize(currentPly + 1, alpha, Math.min(minValue, beta));
                if (currentValue < minValue) {
                    minValue = currentValue;
                    bestMoves[currentPly] = move;
                    if (minValue <= alpha) {
                        search = false;
                    } else if (minValue == Evaluator.BLACK_WON) {
                        search = false;
                    }
                }

                move.undoMove();
            }
        }
        return minValue;
    }

}
