/**
 *
 */
package net.chesstango.search.smart;

import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.evaluation.imp.GameEvaluatorImp02;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;

import java.util.Queue;

/**
 * @author Mauricio Coria
 */
public class MinMaxPruning extends AbstractSmart {
    private final MoveSorter moveSorter;
    private final Quiescence quiescence;

    public MinMaxPruning() {
        this(new Quiescence(new GameEvaluatorImp02(), new MoveSorter()), new MoveSorter());
    }

    public MinMaxPruning(Quiescence quiescence) {
        this(quiescence, new MoveSorter());
    }

    public MinMaxPruning(Quiescence quiescence, MoveSorter moveSorter) {
        this.moveSorter = moveSorter;
        this.quiescence = quiescence;
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
            for (Move move : game.getPossibleMoves()) {
                bestMove = move;
                break;
            }
        }

        return bestMove;
    }

    protected int minimize(Game game, final int currentPly, final int alpha, final int beta) {
        if (!game.getStatus().isInProgress()) {
            return GameEvaluator.evaluateFinalStatus(game);
        } if (currentPly == 0 ){
            return quiescence.quiescenceMin(game, alpha, beta);
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

    protected int maximize(Game game, final int currentPly, final int alpha, final int beta) {
        if (!game.getStatus().isInProgress()) {
            return GameEvaluator.evaluateFinalStatus(game);
        } if (currentPly == 0 ){
            return quiescence.quiescenceMax(game, alpha, beta);
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

}
