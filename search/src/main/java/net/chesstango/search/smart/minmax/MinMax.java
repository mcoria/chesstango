package net.chesstango.search.smart.minmax;

import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.SearchMoveResult;

import java.util.*;

/**
 * @author Mauricio Coria
 */
public class MinMax extends AbstractSmart {

    private static final int DEFAULT_MAX_PLIES = 4;
    private GameEvaluator evaluator;


    @Override
    public SearchMoveResult searchBestMove(Game game) {
        return searchBestMove(game, 10);
    }

    // Beyond level 4, the performance is really bad

    @Override
    public SearchMoveResult searchBestMove(Game game, int depth) {
        this.keepProcessing = true;

        final boolean minOrMax = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? false : true;
        final List<Move> bestMoves = new ArrayList<Move>();

        int betterEvaluation = minOrMax ? GameEvaluator.INFINITE_POSITIVE : GameEvaluator.INFINITE_NEGATIVE;

        for (Move move : game.getPossibleMoves()) {
            game = game.executeMove(move);

            int currentEvaluation = minMax(game, !minOrMax, depth - 1);

            if (currentEvaluation == betterEvaluation) {
                bestMoves.add(move);
            } else {
                if (minOrMax) {
                    if (currentEvaluation < betterEvaluation) {
                        betterEvaluation = currentEvaluation;
                        bestMoves.clear();
                        bestMoves.add(move);
                    }
                } else {
                    if (currentEvaluation > betterEvaluation) {
                        betterEvaluation = currentEvaluation;
                        bestMoves.clear();
                        bestMoves.add(move);
                    }
                }
            }

            game = game.undoMove();
        }

        return new SearchMoveResult(betterEvaluation, bestMoves.size() - 1, selectMove(game.getChessPosition().getCurrentTurn(), bestMoves), null);
    }

    @Override
    public void setGameEvaluator(GameEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    protected int minMax(Game game, final boolean minOrMax, final int currentPly) {
        int betterEvaluation = minOrMax ? GameEvaluator.INFINITE_POSITIVE : GameEvaluator.INFINITE_NEGATIVE;
        if (currentPly == 0 || !game.getStatus().isInProgress()) {
            betterEvaluation = evaluator.evaluate(game);
        } else {
            for (Move move : game.getPossibleMoves()) {
                game = game.executeMove(move);

                int currentEvaluation = minMax(game, !minOrMax, currentPly - 1);
                if (minOrMax) {
                    if (currentEvaluation < betterEvaluation) {
                        betterEvaluation = currentEvaluation;
                    }
                } else {
                    if (currentEvaluation > betterEvaluation) {
                        betterEvaluation = currentEvaluation;
                    }
                }

                game = game.undoMove();
            }
        }
        return betterEvaluation;
    }
}
