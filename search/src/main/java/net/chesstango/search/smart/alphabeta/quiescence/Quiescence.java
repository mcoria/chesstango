package net.chesstango.search.smart.alphabeta.quiescence;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.AlphaBetaHelper;
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

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public long maximize(final int currentPly, final int alpha, final int beta) {
        boolean search = true;
        Move bestMove = null;
        int maxValue = evaluator.evaluate();
        if (maxValue >= beta) {
            return AlphaBetaHelper.encode(maxValue);
        }

        Iterable<Move> sortedMoves = moveSorter.getOrderedMoves(currentPly);
        Iterator<Move> moveIterator = sortedMoves.iterator();
        while (moveIterator.hasNext() && search) {
            Move move = moveIterator.next();
            if (!move.isQuiet()) {
                move.executeMove();

                long bestMoveAndValue = next.minimize(currentPly + 1, Math.max(maxValue, alpha), beta);
                int currentValue = AlphaBetaHelper.decodeValue(bestMoveAndValue);
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
        }
        return AlphaBetaHelper.encode(bestMove, maxValue);
    }

    @Override
    public long minimize(final int currentPly, final int alpha, final int beta) {
        boolean search = true;
        Move bestMove = null;
        int minValue = evaluator.evaluate();
        if (minValue <= alpha) {
            return AlphaBetaHelper.encode(minValue);
        }

        Iterable<Move> sortedMoves = moveSorter.getOrderedMoves(currentPly);
        Iterator<Move> moveIterator = sortedMoves.iterator();
        while (moveIterator.hasNext() && search) {
            Move move = moveIterator.next();
            if (!move.isQuiet()) {
                move.executeMove();

                long bestMoveAndValue = next.maximize(currentPly + 1, alpha, Math.min(minValue, beta));
                int currentValue = AlphaBetaHelper.decodeValue(bestMoveAndValue);
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
        }
        return AlphaBetaHelper.encode(bestMove, minValue);
    }


}
