package net.chesstango.search.smart.negamax;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MovePromotion;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.smart.MoveSorter;

import java.util.Iterator;
import java.util.List;
import java.util.Queue;

/**
 * @author Mauricio Coria
 */
public class NegaQuiescence {
    private final MoveSorter moveSorter;

    private GameEvaluator evaluator;

    public NegaQuiescence() {
        this(new MoveSorter());
    }

    public NegaQuiescence(MoveSorter moveSorter) {
        this.moveSorter = moveSorter;
    }

    public int quiescenceMax(Game game, final int alpha, final int beta) {
        boolean search = true;

        int maxValue = evaluator.evaluate(game);

        if (maxValue >= beta) {
            return maxValue;
        }

        List<Move> sortedMoves = moveSorter.sortMoves(game.getPossibleMoves());
        Iterator<Move> moveIterator = sortedMoves.iterator();
        while (moveIterator.hasNext() && search) {
            Move move = moveIterator.next();

            if (move.getTo().getPiece() != null || move instanceof MovePromotion) {
                game = game.executeMove(move);

                int currentValue = - quiescenceMax(game, -beta, -Math.max(maxValue, alpha));

                if (currentValue > maxValue) {
                    maxValue = currentValue;
                    if (maxValue >= beta) {
                        search = false;
                    }
                }

                game = game.undoMove();
            }
        }
        return maxValue;
    }

    public void setGameEvaluator(GameEvaluator evaluator) {
        this.evaluator = new NegaMaxEvaluatorWrapper(evaluator);
    }
}
