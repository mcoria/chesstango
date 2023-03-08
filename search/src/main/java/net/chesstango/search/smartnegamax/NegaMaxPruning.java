package net.chesstango.search.smartnegamax;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMoveResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * @author Mauricio Coria
 */
public class NegaMaxPruning extends AbstractSmart {
    private final MoveSorter moveSorter;
    private final Quiescence quiescence;

    public NegaMaxPruning() {
        this(new Quiescence(new MoveSorter()), new MoveSorter());
    }

    public NegaMaxPruning(Quiescence quiescence) {
        this(quiescence, new MoveSorter());
    }

    public NegaMaxPruning(Quiescence quiescence, MoveSorter moveSorter) {
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

        final boolean minOrMax = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? false : true;
        final List<Move> bestMoves = new ArrayList<Move>();

        int bestValue = GameEvaluator.INFINITE_NEGATIVE;
        boolean search = true;

        Queue<Move> sortedMoves = moveSorter.sortMoves(game.getPossibleMoves());
        while (!sortedMoves.isEmpty() && search && keepProcessing) {
            Move move = sortedMoves.poll();

            game = game.executeMove(move);

            int currentValue = -negaMax(game, depth - 1, GameEvaluator.INFINITE_NEGATIVE, -bestValue);

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

        return new SearchMoveResult(minOrMax ? -bestValue : bestValue, bestMoves.size() - 1, selectMove(game.getChessPosition().getCurrentTurn(), bestMoves), null);
    }

    protected int negaMax(Game game, final int currentPly, final int alpha, final int beta) {

        final boolean minOrMax = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? false : true;

        if (!game.getStatus().isInProgress()) {
            return minOrMax ? -GameEvaluator.evaluateFinalStatus(game) : GameEvaluator.evaluateFinalStatus(game);
        }
        if (currentPly == 0) {
            return quiescence.quiescenceMax(game, alpha, beta);
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
