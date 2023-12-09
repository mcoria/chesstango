package net.chesstango.search.smart.negamax;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.MoveEvaluation;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class NegaMax implements SmartAlgorithm, SearchByCycleListener, SearchByDepthListener {

    private static final int DEFAULT_MAX_PLIES = 4;
    private GameEvaluator evaluator;
    private Game game;
    private int maxPly;

    @Override
    public MoveEvaluation search() {
        final List<Move> bestMoves = new ArrayList<Move>();
        final Color currentTurn = game.getChessPosition().getCurrentTurn();

        final boolean minOrMax = Color.WHITE.equals(currentTurn) ? false : true;
        int betterEvaluation = GameEvaluator.INFINITE_NEGATIVE;

        for (Move move : game.getPossibleMoves()) {
            game.executeMove(move);

            int currentEvaluation = -negaMax(game, maxPly - 1);

            if (currentEvaluation == betterEvaluation) {
                bestMoves.add(move);
            } else {
                if (currentEvaluation > betterEvaluation) {
                    betterEvaluation = currentEvaluation;
                    bestMoves.clear();
                    bestMoves.add(move);
                }
            }

            game.undoMove();
        }

        Move bestMove = MoveSelector.selectMove(currentTurn, bestMoves);

        //return new SearchMoveResult(maxPly, minOrMax ? -betterEvaluation : betterEvaluation, bestMove, null);
        return new MoveEvaluation(bestMove, minOrMax ? -betterEvaluation : betterEvaluation);
    }


    protected int negaMax(Game game, final int currentPly) {
        int betterEvaluation = GameEvaluator.INFINITE_NEGATIVE;

        if (currentPly == 0 || !game.getStatus().isInProgress()) {
            betterEvaluation = evaluator.evaluate();
        } else {
            for (Move move : game.getPossibleMoves()) {
                game = game.executeMove(move);

                int currentEvaluation = -negaMax(game, currentPly - 1);
                if (currentEvaluation > betterEvaluation) {
                    betterEvaluation = currentEvaluation;
                }

                game = game.undoMove();
            }
        }
        return betterEvaluation;
    }

    @Override
    public void beforeSearch(Game game) {
        this.game = game;
        this.evaluator.setGame(game);
    }

    @Override
    public void afterSearch(SearchMoveResult result) {
    }

    @Override
    public void beforeSearchByDepth(SearchContext context) {
        this.maxPly = context.getMaxPly();
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {

    }

    public void setGameEvaluator(GameEvaluator evaluator) {
        this.evaluator = new NegaMaxEvaluatorWrapper(evaluator);
    }
}
