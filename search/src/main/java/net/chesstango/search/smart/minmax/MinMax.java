package net.chesstango.search.smart.minmax;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.AbstractSmart;
import net.chesstango.search.smart.MoveSelector;
import net.chesstango.search.smart.SearchContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class MinMax extends AbstractSmart {
    // Beyond level 4, the performance is really bad
    private static final int DEFAULT_MAX_PLIES = 4;
    private int[] visitedNodesCounter;
    private GameEvaluator evaluator;
    public void setGameEvaluator(GameEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    @Override
    public SearchMoveResult searchBestMove(Game game, SearchContext context) {
        this.keepProcessing = true;
        this.visitedNodesCounter = new int[context.getMaxPly()];

        final Color currentTurn =  game.getChessPosition().getCurrentTurn();
        final boolean minOrMax = Color.WHITE.equals(currentTurn) ? false : true;
        final List<Move> bestMoves = new ArrayList<Move>();

        int betterEvaluation = minOrMax ? GameEvaluator.INFINITE_POSITIVE : GameEvaluator.INFINITE_NEGATIVE;

        for (Move move : game.getPossibleMoves()) {
            game = game.executeMove(move);

            int currentEvaluation = minMax(game, !minOrMax, context.getMaxPly() - 1);

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


        return new SearchMoveResult(context.getMaxPly(), betterEvaluation, MoveSelector.selectMove(currentTurn, bestMoves), null)
                .setVisitedNodesCounters(visitedNodesCounter)
                .setEvaluationCollisions(bestMoves.size() - 1)
                .setBestMoveOptions(bestMoves);
    }

    protected int minMax(Game game, final boolean minOrMax, final int currentPly) {
        visitedNodesCounter[visitedNodesCounter.length - currentPly - 1]++;
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
