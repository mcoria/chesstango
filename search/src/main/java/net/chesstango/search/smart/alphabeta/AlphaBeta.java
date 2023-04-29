package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.smart.MoveSorter;
import net.chesstango.search.smart.SearchContext;

import java.util.Queue;

/**
 * @author Mauricio Coria
 */
public class AlphaBeta implements AlphaBetaFilter {
    protected boolean keepProcessing;

    private GameEvaluator evaluator;

    private AlphaBetaFilter next;

    private AlphaBetaFilter quiescence;

    private MoveSorter moveSorter;

    private int maxPly;

    @Override
    public void init(Game game, SearchContext context) {
        this.maxPly = context.getMaxPly();
        this.keepProcessing = true;
    }

    @Override
    public long minimize(Game game, final int currentPly, final int alpha, final int beta) {
        if (!game.getStatus().isInProgress()) {
            return evaluator.evaluate(game);
        }
        if (currentPly == maxPly) {
            return quiescence.minimize(game, currentPly, alpha, beta);
        } else {
            boolean search = true;
            short bestMove = 0;
            int minValue = GameEvaluator.INFINITE_POSITIVE;

            for (Queue<Move> sortedMoves = moveSorter.sortMoves(game.getPossibleMoves());
                 !sortedMoves.isEmpty() && search && keepProcessing; ) {
                Move move = sortedMoves.poll();

                game = game.executeMove(move);

                long bestMoveAndValue = next.maximize(game, currentPly + 1, alpha, Math.min(minValue, beta));

                int currentValue = (int) bestMoveAndValue;

                if (currentValue < minValue) {
                    minValue = currentValue;
                    bestMove = move.binaryEncoding();
                    if (alpha >= minValue) {
                        search = false;
                    }
                }

                game = game.undoMove();
            }

            //return ((long) bestMove << 32) | ((long) minValue);
            return minValue;
        }
    }

    @Override
    public long maximize(Game game, final int currentPly, final int alpha, final int beta) {
        if (!game.getStatus().isInProgress()) {
            return evaluator.evaluate(game);
        }
        if (currentPly == maxPly) {
            return quiescence.maximize(game, currentPly, alpha, beta);
        } else {
            boolean search = true;
            short bestMove = 0;
            int maxValue = GameEvaluator.INFINITE_NEGATIVE;

            for (Queue<Move> sortedMoves = moveSorter.sortMoves(game.getPossibleMoves());
                 !sortedMoves.isEmpty() && search && keepProcessing; ) {
                Move move = sortedMoves.poll();

                game = game.executeMove(move);

                long bestMoveAndValue = next.minimize(game, currentPly + 1, Math.max(maxValue, alpha), beta);

                int currentValue = (int) bestMoveAndValue;

                if (currentValue > maxValue) {
                    maxValue = currentValue;
                    bestMove = move.binaryEncoding();
                    if (maxValue >= beta) {
                        search = false;
                    }
                }

                game = game.undoMove();
            }

            //return ((long) bestMove << 32) | ((long) maxValue);
            return maxValue;
        }
    }

    @Override
    public void stopSearching() {
        this.keepProcessing = false;
    }

    public void setMoveSorter(MoveSorter moveSorter) {
        this.moveSorter = moveSorter;
    }

    public void setQuiescence(AlphaBetaFilter quiescence) {
        this.quiescence = quiescence;
    }

    public void setGameEvaluator(GameEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    public void setNext(AlphaBetaFilter next) {
        this.next = next;
    }
}
