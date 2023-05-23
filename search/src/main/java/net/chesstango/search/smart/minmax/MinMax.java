package net.chesstango.search.smart.minmax;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.SearchListener;
import net.chesstango.search.smart.SearchSmart;
import net.chesstango.search.smart.MoveSelector;
import net.chesstango.search.smart.SearchContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class MinMax implements SearchSmart {
    // Beyond level 4, the performance is terrible
    private static final int DEFAULT_MAX_PLIES = 4;

    private int maxPly;
    private int[] visitedNodesCounter;
    private int[] expectedNodesCounters;
    private GameEvaluator evaluator;

    public void setGameEvaluator(GameEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    @Override
    public SearchMoveResult search(SearchContext context) {
        final Game game = context.getGame();
        this.maxPly = context.getMaxPly();
        this.visitedNodesCounter = context.getVisitedNodesCounters();
        this.expectedNodesCounters = context.getExpectedNodesCounters();

        final Color currentTurn = game.getChessPosition().getCurrentTurn();
        final boolean minOrMax = Color.WHITE.equals(currentTurn) ? false : true;
        final List<Move> bestMoves = new ArrayList<Move>();

        int betterEvaluation = minOrMax ? GameEvaluator.INFINITE_POSITIVE : GameEvaluator.INFINITE_NEGATIVE;

        expectedNodesCounters[0] += game.getPossibleMoves().size();
        for (Move move : game.getPossibleMoves()) {
            game.executeMove(move);

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

            game.undoMove();
        }


        return new SearchMoveResult(context.getMaxPly(), betterEvaluation, MoveSelector.selectMove(currentTurn, bestMoves), null)
                .setVisitedNodesCounters(visitedNodesCounter)
                .setExpectedNodesCounters(expectedNodesCounters)
                .setEvaluationCollisions(bestMoves.size() - 1)
                .setBestMoveOptions(bestMoves);
    }

    @Override
    public void stopSearching() {

    }

    protected int minMax(Game game, final boolean minOrMax, final int currentPly) {
        visitedNodesCounter[maxPly - currentPly - 1]++;
        expectedNodesCounters[maxPly - currentPly] += game.getPossibleMoves().size();

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
