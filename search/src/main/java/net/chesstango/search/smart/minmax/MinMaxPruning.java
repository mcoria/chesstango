package net.chesstango.search.smart.minmax;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.AbstractSmart;
import net.chesstango.search.smart.MoveSorter;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * @author Mauricio Coria
 */
public class MinMaxPruning extends AbstractSmart {
    private final MoveSorter moveSorter;
    private final Quiescence quiescence;

    private int[] visitedNodesCounter;

    public MinMaxPruning() {
        this(new Quiescence(new MoveSorter()), new MoveSorter());
    }

    public MinMaxPruning(Quiescence quiescence) {
        this(quiescence, new MoveSorter());
    }

    public MinMaxPruning(Quiescence quiescence, MoveSorter moveSorter) {
        this.moveSorter = moveSorter;
        this.quiescence = quiescence;
    }

    @Override
    public SearchMoveResult searchBestMove(Game game) {
        return searchBestMove(game, 10);
    }

    @Override
    public void setGameEvaluator(GameEvaluator evaluator) {
        quiescence.setGameEvaluator(evaluator);
    }

    @Override
    public SearchMoveResult searchBestMove(Game game, final int depth) {
        this.keepProcessing = true;
        this.visitedNodesCounter = new int[depth];

        final boolean minOrMax = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? false : true;
        final List<Move> bestMoves = new ArrayList<Move>();

        int bestValue = minOrMax ? GameEvaluator.INFINITE_POSITIVE : GameEvaluator.INFINITE_NEGATIVE;
        boolean search = true;

        Queue<Move> sortedMoves = moveSorter.sortMoves(game.getPossibleMoves());
        while (!sortedMoves.isEmpty() && search && keepProcessing) {
            Move move = sortedMoves.poll();

            game = game.executeMove(move);

            int currentValue = minOrMax ?
                    maximize(game, depth - 1, GameEvaluator.INFINITE_NEGATIVE, bestValue) :
                    minimize(game, depth - 1, bestValue, GameEvaluator.INFINITE_POSITIVE);

            if (minOrMax && currentValue < bestValue || !minOrMax && currentValue > bestValue) {
                bestValue = currentValue;
                bestMoves.clear();
                bestMoves.add(move);
                if (minOrMax && bestValue == GameEvaluator.BLACK_WON ||             //Black wins
                        !minOrMax && bestValue == GameEvaluator.WHITE_WON) {        //White wins
                    search = false;
                }

            } else if (currentValue == bestValue) {
                bestMoves.add(move);
            }

            game = game.undoMove();
        }

        if (bestMoves.size() == 0 &&
                (minOrMax && bestValue == GameEvaluator.WHITE_WON || !minOrMax && bestValue == GameEvaluator.BLACK_WON)) {
            game.getPossibleMoves().forEach(bestMoves::add);
        }

        SearchMoveResult searchMoveResult = new SearchMoveResult(depth, bestValue, bestMoves.size() - 1, selectMove(game.getChessPosition().getCurrentTurn(), bestMoves), null);
        searchMoveResult.setVisitedNodesCounter(visitedNodesCounter);

        return searchMoveResult;
    }

    protected int minimize(Game game, final int currentPly, final int alpha, final int beta) {
        visitedNodesCounter[visitedNodesCounter.length - currentPly - 1]++;
        if (currentPly == 0 || !game.getStatus().isInProgress()) {
            return quiescence.minimize(game, alpha, beta);
        } else {
            boolean search = true;
            int minValue = GameEvaluator.INFINITE_POSITIVE;

            for (Queue<Move> sortedMoves = moveSorter.sortMoves(game.getPossibleMoves());
                 search && keepProcessing && !sortedMoves.isEmpty(); ) {
                Move move = sortedMoves.poll();

                game = game.executeMove(move);

                int currentValue = maximize(game, currentPly - 1, alpha, Math.min(minValue, beta));

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

    protected int maximize(Game game, final int currentPly, final int alpha, final int beta) {
        visitedNodesCounter[visitedNodesCounter.length - currentPly - 1]++;
        if (currentPly == 0 || !game.getStatus().isInProgress()) {
            return quiescence.maximize(game, alpha, beta);
        } else {
            boolean search = true;
            int maxValue = GameEvaluator.INFINITE_NEGATIVE;

            for (Queue<Move> sortedMoves = moveSorter.sortMoves(game.getPossibleMoves());
                 search && keepProcessing && !sortedMoves.isEmpty(); ) {
                Move move = sortedMoves.poll();

                game = game.executeMove(move);

                int currentValue = minimize(game, currentPly - 1, Math.max(maxValue, alpha), beta);

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

}
