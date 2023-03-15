package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MovePromotion;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.smart.MoveSorter;
import net.chesstango.search.smart.SearchContext;

import java.util.Queue;

/**
 * @author Mauricio Coria
 */
public class Quiescence implements AlphaBetaFilter {
    private MoveSorter moveSorter;
    private GameEvaluator evaluator;

    private boolean keepProcessing;

    private SearchContext context;

    @Override
    public void init(SearchContext context) {
        this.context = context;
    }
    @Override
    public int minimize(Game game, final int currentPly, final int alpha, final int beta) {
        this.keepProcessing = true;
        boolean search = true;
        int minValue = evaluator.evaluate(game);

        if (alpha >= minValue) {
            return minValue;
        }

        for (Queue<Move> sortedMoves = moveSorter.sortMoves(game.getPossibleMoves());
             search && !sortedMoves.isEmpty() && keepProcessing; ) {
            Move move = sortedMoves.poll();

            if (move.getTo().getPiece() != null || move instanceof MovePromotion) {
                game = game.executeMove(move);

                int currentValue = maximize(game, currentPly + 1, alpha, Math.min(minValue, beta));

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

    @Override
    public int maximize(Game game, final int currentPly, final int alpha, final int beta){
        this.keepProcessing = true;
        boolean search = true;
        int maxValue = evaluator.evaluate(game);

        if (maxValue >= beta) {
            return maxValue;
        }

        for (Queue<Move> sortedMoves = moveSorter.sortMoves(game.getPossibleMoves());
             search && !sortedMoves.isEmpty() && keepProcessing; ) {
            Move move = sortedMoves.poll();

            if (move.getTo().getPiece() != null || move instanceof MovePromotion) {
                game = game.executeMove(move);

                int currentValue = minimize(game, currentPly + 1, Math.max(maxValue, alpha), beta);

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

    @Override
    public void stopSearching() {
        this.keepProcessing = false;
    }

    public void setGameEvaluator(GameEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    public void setMoveSorter(MoveSorter moveSorter) {
        this.moveSorter = moveSorter;
    }
}
