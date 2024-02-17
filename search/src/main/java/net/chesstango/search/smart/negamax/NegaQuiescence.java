package net.chesstango.search.smart.negamax;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MovePromotion;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.smart.sorters.MoveSorter;

import java.util.Iterator;

/**
 * @author Mauricio Coria
 */
public class NegaQuiescence {
    private MoveSorter moveSorter;

    private GameEvaluator evaluator;

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
                game.executeMove(move);

                int currentValue = -quiescenceMax(game, -beta, -Math.max(maxValue, alpha));

                if (currentValue > maxValue) {
                    maxValue = currentValue;
                    if (maxValue >= beta) {
                        search = false;
                    }
                }

                game.undoMove();
            }
        }
        return maxValue;
    }

    public void setGameEvaluator(GameEvaluator evaluator) {
        this.evaluator = new NegaMaxEvaluatorWrapper(evaluator);
    }

    public void setMoveSorter(MoveSorter moveSorter) {
        this.moveSorter = moveSorter;
    }

    public void setupGameEvaluator(Game game) {
        this.evaluator.setGame(game);
    }
}
