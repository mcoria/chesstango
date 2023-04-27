package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Game;
import net.chesstango.board.iterators.Cardinal;
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
    private boolean keepProcessing;

    private AlphaBetaFilter next;
    private MoveSorter moveSorter;
    private GameEvaluator evaluator;

    @Override
    public void init(Game game, SearchContext context) {
        this.keepProcessing = true;
    }

    @Override
    public int minimize(Game game, final int currentPly, final int alpha, final int beta) {
        int minValue = evaluator.evaluate(game);

        if (alpha >= minValue) {
            return minValue;
        }
        boolean search = true;

        for (Queue<Move> sortedMoves = moveSorter.sortMoves(game.getPossibleMoves());
             !sortedMoves.isEmpty() && search && keepProcessing; ) {
            Move move = sortedMoves.poll();

            if (isNotQuiet(move)) {
                game = game.executeMove(move);

                int currentValue = next.maximize(game, currentPly + 1, alpha, Math.min(minValue, beta));

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
    public int maximize(Game game, final int currentPly, final int alpha, final int beta) {
        int maxValue = evaluator.evaluate(game);

        if (maxValue >= beta) {
            return maxValue;
        }
        boolean search = true;

        for (Queue<Move> sortedMoves = moveSorter.sortMoves(game.getPossibleMoves());
             !sortedMoves.isEmpty() && search && keepProcessing; ) {
            Move move = sortedMoves.poll();

            if (isNotQuiet(move)) {
                game = game.executeMove(move);

                int currentValue = next.minimize(game, currentPly + 1, Math.max(maxValue, alpha), beta);

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

    protected boolean isNotQuiet(Move move) {
        return move.getTo().getPiece() != null ||  // Captura
                move.getFrom().getPiece().isPawn() && !(Cardinal.Sur.equals(move.getMoveDirection()) || Cardinal.Norte.equals(move.getMoveDirection()) ) || // Captura de peon
                move instanceof MovePromotion;     // Promocion
    }

    @Override
    public void stopSearching() {
        this.keepProcessing = false;
    }

    public void setNext(AlphaBetaFilter next) {
        this.next = next;
    }

    public void setMoveSorter(MoveSorter moveSorter) {
        this.moveSorter = moveSorter;
    }

    public void setGameEvaluator(GameEvaluator evaluator) {
        this.evaluator = evaluator;
    }
}
