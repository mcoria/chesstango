package net.chesstango.search.smart.negamax;

import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MovePromotion;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.smart.sorters.MoveSorter;

import java.util.Iterator;

/**
 * @author Mauricio Coria
 */
public class NegaQuiescence {
    @Setter
    private MoveSorter moveSorter;

    private Evaluator evaluator;

    public int quiescenceMax(final Game game, final int alpha, final int beta) {
        boolean search = true;

        int maxValue = evaluator.evaluate();

        if (maxValue >= beta) {
            return maxValue;
        }

        Iterable<Move> sortedMoves = moveSorter.getOrderedMoves(0);
        Iterator<Move> moveIterator = sortedMoves.iterator();
        while (moveIterator.hasNext() && search) {
            Move move = moveIterator.next();

            if (move.getTo().getPiece() != null || move instanceof MovePromotion) {
                move.executeMove();

                int currentValue = -quiescenceMax(game, -beta, -Math.max(maxValue, alpha));

                if (currentValue > maxValue) {
                    maxValue = currentValue;
                    if (maxValue >= beta) {
                        search = false;
                    }
                }

                move.undoMove();
            }
        }
        return maxValue;
    }

    public void setGameEvaluator(Evaluator evaluator) {
        this.evaluator = new NegaMaxEvaluatorWrapper(evaluator);
    }

    public void setupGameEvaluator(Game game) {
        this.evaluator.setGame(game);
    }
}
