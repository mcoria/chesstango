package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.smart.MoveSorter;

import java.util.Queue;

public class AlphaBetaImp implements AlphaBetaSearch {
    protected boolean keepProcessing = true;

    private Quiescence quiescence;

    private MoveSorter moveSorter;

    public int minimize(Game game, final int currentPly, final int alpha, final int beta, final SearchContext context) {
        int[] visitedNodesCounter = context.getVisitedNodesCounter();
        visitedNodesCounter[currentPly - 1]++;

        if (currentPly == context.getMaxPly() || !game.getStatus().isInProgress()) {
            return quiescence.minimize(game, currentPly, alpha, beta, context);
        } else {
            boolean search = true;
            int minValue = GameEvaluator.INFINITE_POSITIVE;

            for (Queue<Move> sortedMoves = moveSorter.sortMoves(game.getPossibleMoves());
                 search && keepProcessing && !sortedMoves.isEmpty(); ) {
                Move move = sortedMoves.poll();

                game = game.executeMove(move);

                int currentValue = maximize(game, currentPly + 1, alpha, Math.min(minValue, beta), context);

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

    public int maximize(Game game, final int currentPly, final int alpha, final int beta, final SearchContext context) {
        int[] visitedNodesCounter = context.getVisitedNodesCounter();
        visitedNodesCounter[currentPly - 1]++;

        if (currentPly == context.getMaxPly() || !game.getStatus().isInProgress()) {
            return quiescence.maximize(game, currentPly, alpha, beta, context);
        } else {
            boolean search = true;
            int maxValue = GameEvaluator.INFINITE_NEGATIVE;

            for (Queue<Move> sortedMoves = moveSorter.sortMoves(game.getPossibleMoves());
                 search && keepProcessing && !sortedMoves.isEmpty(); ) {
                Move move = sortedMoves.poll();

                game = game.executeMove(move);

                int currentValue = minimize(game, currentPly + 1, Math.max(maxValue, alpha), beta, context);

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
        keepProcessing = false;
    }

    public void setQuiescence(Quiescence quiescence) {
        this.quiescence = quiescence;
    }

    public void setMoveSorter(MoveSorter moveSorter) {
        this.moveSorter = moveSorter;
    }
}
