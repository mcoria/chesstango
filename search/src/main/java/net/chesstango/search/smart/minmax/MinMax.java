package net.chesstango.search.smart.minmax;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.MoveEvaluation;
import net.chesstango.search.MoveEvaluationType;
import net.chesstango.search.smart.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class MinMax implements SmartAlgorithm, SearchByCycleListener, SearchByDepthListener {
    // Beyond level 4, the performance is terrible
    private static final int DEFAULT_MAX_PLIES = 4;
    private Game game;
    private int maxPly;
    private int[] visitedNodesCounter;
    private int[] expectedNodesCounters;
    private Evaluator evaluator;

    @Override
    public MoveEvaluation search() {
        final Color currentTurn = game.getChessPosition().getCurrentTurn();
        final boolean minOrMax = !Color.WHITE.equals(currentTurn);
        final List<Move> bestMoves = new ArrayList<Move>();

        int betterEvaluation = minOrMax ? Evaluator.INFINITE_POSITIVE : Evaluator.INFINITE_NEGATIVE;

        expectedNodesCounters[0] += game.getPossibleMoves().size();
        for (Move move : game.getPossibleMoves()) {
            game.executeMove(move);

            int currentEvaluation = minMax(game, !minOrMax, maxPly - 1);

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

        return new MoveEvaluation(MoveSelector.selectMove(currentTurn, bestMoves), betterEvaluation, MoveEvaluationType.EXACT);
    }

    protected int minMax(Game game, final boolean minOrMax, final int currentPly) {
        visitedNodesCounter[maxPly - currentPly - 1]++;
        expectedNodesCounters[maxPly - currentPly] += game.getPossibleMoves().size();

        int betterEvaluation = minOrMax ? Evaluator.INFINITE_POSITIVE : Evaluator.INFINITE_NEGATIVE;
        if (currentPly == 0 || !game.getStatus().isInProgress()) {
            betterEvaluation = evaluator.evaluate();
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

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.game = context.getGame();
        this.visitedNodesCounter = new int[30];
        this.expectedNodesCounters = new int[30];
        this.evaluator.setGame(game);
    }

    @Override
    public void beforeSearchByDepth(SearchByDepthContext context) {
        this.maxPly = context.getMaxPly();
    }

    public void setGameEvaluator(Evaluator evaluator) {
        this.evaluator = evaluator;
    }
}
