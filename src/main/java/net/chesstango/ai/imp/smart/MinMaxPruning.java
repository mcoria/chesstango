/**
 *
 */
package net.chesstango.ai.imp.smart;

import net.chesstango.ai.imp.smart.evaluation.GameEvaluator;
import net.chesstango.ai.imp.smart.evaluation.imp.GameEvaluatorImp01;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

/**
 * @author Mauricio Coria
 */
public class MinMaxPruning extends AbstractSmart {
    private final GameEvaluator evaluator;
    private final MoveSorter moveSorter;

    public MinMaxPruning(GameEvaluator evaluator) {
        this(evaluator, new MoveSorter());
    }

    public MinMaxPruning(GameEvaluator evaluator, MoveSorter moveSorter) {
        this.evaluator = evaluator;
        this.moveSorter = moveSorter;
    }

    @Override
    public Move searchBestMove(Game game) {
        return searchBestMove(game, 10);
    }

    @Override
    public Move searchBestMove(Game game, final int depth) {
        this.keepProcessing = true;

        final boolean minOrMax = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? false : true;

        int bestValue = minOrMax ? GameEvaluator.INFINITE_POSITIVE : GameEvaluator.INFINITE_NEGATIVE;
        Move bestMove = null;
        boolean search = true;

        Queue<Move> sortedMoves = moveSorter.sortMoves(game.getPossibleMoves());
        while (!sortedMoves.isEmpty() && search && keepProcessing) {
            Move move = sortedMoves.poll();

            game = game.executeMove(move);

            int currentValue = minOrMax ? maximize(game,depth - 1, GameEvaluator.INFINITE_NEGATIVE, bestValue) :
                                            minimize(game,depth - 1, bestValue, GameEvaluator.INFINITE_POSITIVE);

            if (minOrMax && currentValue < bestValue || !minOrMax && currentValue > bestValue) {
                bestValue = currentValue;
                bestMove = move;
                if (minOrMax && bestValue == GameEvaluator.BLACK_WON ||             //Black wins
                        !minOrMax && bestValue == GameEvaluator.WHITE_WON) {        //White wins
                    search = false;
                }

            }

            game = game.undoMove();
        }
        evaluation = bestValue;


        if (bestMove == null && (minOrMax && evaluation == GameEvaluator.WHITE_WON  || !minOrMax && evaluation == GameEvaluator.BLACK_WON) ) {
            // Seleccionamos el primer movimiento
            Iterator<Move> possibleMovesIterator = game.getPossibleMoves().iterator();
            while (possibleMovesIterator.hasNext()) {
                Move move = possibleMovesIterator.next();
                bestMove = move;
                break;
            }
        }

        return bestMove;
    }

    protected int minimize(Game game, final int currentPly, final int alpha, final int beta) {
        if (!game.getStatus().isInProgress()) {
            return evaluator.evaluate(game);
        } if (currentPly == 0 ){
            return quiescenceMin(game, alpha, beta);
        } else {
            boolean search = true;
            int minValue = GameEvaluator.INFINITE_POSITIVE;

            for (Queue<Move> sortedMoves = moveSorter.sortMoves(game.getPossibleMoves());
                 search && keepProcessing && !sortedMoves.isEmpty(); ) {
                Move move = sortedMoves.poll();

                game = game.executeMove(move);

                int currentValue = maximize(game, currentPly - 1, alpha, Math.min(minValue, beta));

                if(currentValue < minValue) {
                    minValue = currentValue;
                    if (alpha >= minValue) {
                        search = false;
                    }
                }

                game = game.undoMove();
            }
            return minValue;
        }
    }

    private int quiescenceMin(Game game, final int alpha, final int beta) {
        boolean search = true;
        int minValue = evaluator.evaluate(game);

        if (alpha >= minValue) {
            return minValue;
        }

        for (Queue<Move> sortedMoves = moveSorter.sortMoves(game.getPossibleMoves());
             search && keepProcessing && !sortedMoves.isEmpty(); ) {
            Move move = sortedMoves.poll();

            if(move.getTo().getValue() != null) {
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

    protected int maximize(Game game, final int currentPly, final int alpha, final int beta) {
        if (!game.getStatus().isInProgress()) {
            return evaluator.evaluate(game);
        } if (currentPly == 0 ){
            return quiescenceMax(game, alpha, beta);
        } else {
            boolean search = true;
            int maxValue = GameEvaluator.INFINITE_NEGATIVE;

            for (Queue<Move> sortedMoves = moveSorter.sortMoves(game.getPossibleMoves());
                 search && keepProcessing && !sortedMoves.isEmpty(); ) {
                Move move = sortedMoves.poll();

                game = game.executeMove(move);

                int currentValue = minimize(game,currentPly - 1, Math.max(maxValue, alpha), beta);

                if(currentValue > maxValue) {
                    maxValue = currentValue;
                    if (maxValue >= beta) {
                        search = false;
                    }
                }

                game = game.undoMove();
            }
            return maxValue;
        }
    }


    private int quiescenceMax(Game game, final int alpha, final int beta) {
        boolean search = true;

        int maxValue = evaluator.evaluate(game);

        if (maxValue >= beta) {
            return maxValue;
        }

        for (Queue<Move> sortedMoves = moveSorter.sortMoves(game.getPossibleMoves());
             search && keepProcessing && !sortedMoves.isEmpty(); ) {
            Move move = sortedMoves.poll();

            if(move.getTo().getValue() != null) {
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

}
