package net.chesstango.search.smart.negamax;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.AbstractSmart;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * @author Mauricio Coria
 */
public class NegaMaxPruning extends AbstractSmart {
    private final MoveSorter moveSorter;
    private final NegaQuiescence negaQuiescence;

    public NegaMaxPruning() {
        this(new NegaQuiescence(new MoveSorter()), new MoveSorter());
    }

    public NegaMaxPruning(NegaQuiescence negaQuiescence) {
        this(negaQuiescence, new MoveSorter());
    }

    public NegaMaxPruning(NegaQuiescence negaQuiescence, MoveSorter moveSorter) {
        this.moveSorter = moveSorter;
        this.negaQuiescence = negaQuiescence;
    }

    @Override
    public SearchMoveResult searchBestMove(Game game) {
        return searchBestMove(game, 10);
    }

    @Override
    public void setGameEvaluator(GameEvaluator evaluator) {
        negaQuiescence.setGameEvaluator(evaluator);
    }

    @Override
    public SearchMoveResult searchBestMove(Game game, final int depth) {
        this.keepProcessing = true;

        final boolean minOrMax = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? false : true;
        final List<Move> bestMoves = new ArrayList<Move>();

        int bestValue = GameEvaluator.INFINITE_NEGATIVE;
        boolean search = true;

        Queue<Move> sortedMoves = moveSorter.sortMoves(game.getPossibleMoves());
        while (!sortedMoves.isEmpty() && search && keepProcessing) {
            Move move = sortedMoves.poll();

            game = game.executeMove(move);

            int currentValue = -negaMax(game, depth - 1, GameEvaluator.INFINITE_NEGATIVE, -bestValue);

            if (currentValue > bestValue) {
                bestValue = currentValue;
                bestMoves.clear();
                bestMoves.add(move);

                // Stop searching if we have found checkmate
                if (bestValue == GameEvaluator.INFINITE_POSITIVE) {
                    search = false;
                }

            } else if (currentValue == bestValue) {
                bestMoves.add(move);
            }

            game = game.undoMove();
        }

        if (bestMoves.size() == 0 && bestValue == GameEvaluator.INFINITE_NEGATIVE) {
            game.getPossibleMoves().forEach(bestMoves::add);
        }

        return new SearchMoveResult(minOrMax ? -bestValue : bestValue, bestMoves.size() - 1, selectMove(game.getChessPosition().getCurrentTurn(), bestMoves), null);
    }

    protected int negaMax(Game game, final int currentPly, final int alpha, final int beta) {
        if (currentPly == 0 || !game.getStatus().isInProgress()) {
            return negaQuiescence.quiescenceMax(game, alpha, beta);
        } else {
            boolean search = true;
            int maxValue = GameEvaluator.INFINITE_NEGATIVE;

            for (Queue<Move> sortedMoves = moveSorter.sortMoves(game.getPossibleMoves());
                 search && keepProcessing && !sortedMoves.isEmpty(); ) {
                Move move = sortedMoves.poll();

                game = game.executeMove(move);

                int currentValue = -negaMax(game, currentPly - 1, -beta, -Math.max(maxValue, alpha));

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
