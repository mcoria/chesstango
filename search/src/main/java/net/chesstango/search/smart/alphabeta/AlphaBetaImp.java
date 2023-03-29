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
public class AlphaBetaImp implements AlphaBetaFilter {
    protected boolean keepProcessing;

    private AlphaBetaFilter next;

    private AlphaBetaFilter quiescence;

    private MoveSorter moveSorter;

    private SearchContext context;

    @Override
    public void init(Game game, SearchContext context) {
        this.context = context;
        this.keepProcessing = true;
    }

    @Override
    public int minimize(Game game, final int currentPly, final int alpha, final int beta) {
        if (currentPly == context.getMaxPly() || !game.getStatus().isInProgress()) {
            return quiescence.minimize(game, currentPly, alpha, beta);
        } else {
            boolean search = true;
            int minValue = GameEvaluator.INFINITE_POSITIVE;

            for (Queue<Move> sortedMoves = moveSorter.sortMoves(game.getPossibleMoves());
                 !sortedMoves.isEmpty() && search && keepProcessing; ) {
                Move move = sortedMoves.poll();

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
            return minValue;
        }
    }

    @Override
    public int maximize(Game game, final int currentPly, final int alpha, final int beta) {
        if (currentPly == context.getMaxPly() || !game.getStatus().isInProgress()) {
            return quiescence.maximize(game, currentPly, alpha, beta);
        } else {
            boolean search = true;
            int maxValue = GameEvaluator.INFINITE_NEGATIVE;

            for (Queue<Move> sortedMoves = moveSorter.sortMoves(game.getPossibleMoves());
                 !sortedMoves.isEmpty() && search && keepProcessing; ) {
                Move move = sortedMoves.poll();

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

    public void setNext(AlphaBetaFilter next) {
        this.next = next;
    }
}
