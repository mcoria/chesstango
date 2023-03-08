package net.chesstango.search.smartnegamax;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MovePromotion;
import net.chesstango.evaluation.GameEvaluator;

import java.util.Queue;

/**
 * @author Mauricio Coria
 */
public class Quiescence {
    private final MoveSorter moveSorter;

    private GameEvaluator evaluator;

    public Quiescence() {
        this(new MoveSorter());
    }

    public Quiescence(MoveSorter moveSorter) {
        this.moveSorter = moveSorter;
    }

    public int quiescenceMin(Game game, final int alpha, final int beta) {
        boolean search = true;
        int minValue = evaluator.evaluate(game);

        if (alpha >= minValue) {
            return minValue;
        }

        for (Queue<Move> sortedMoves = moveSorter.sortMoves(game.getPossibleMoves());
             search && !sortedMoves.isEmpty(); ) {
            Move move = sortedMoves.poll();

            if (move.getTo().getPiece() != null || move instanceof MovePromotion) {
                game = game.executeMove(move);

                int currentValue = quiescenceMax(game, alpha, Math.min(minValue, beta));

                if (currentValue < minValue) {
                    minValue = currentValue;
                    if (alpha >= minValue) {
                        search = false;
                    }
                }

                game = game.undoMove();
            }
        }
        return minValue;
    }

    public int quiescenceMax(Game game, final int alpha, final int beta) {
        boolean search = true;

        int maxValue = evaluator.evaluate(game);

        if (maxValue >= beta) {
            return maxValue;
        }

        for (Queue<Move> sortedMoves = moveSorter.sortMoves(game.getPossibleMoves());
             search && !sortedMoves.isEmpty(); ) {
            Move move = sortedMoves.poll();

            if (move.getTo().getPiece() != null || move instanceof MovePromotion) {
                game = game.executeMove(move);

                int currentValue = quiescenceMin(game, Math.max(maxValue, alpha), beta);

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
        this.evaluator = evaluator;
    }
}
