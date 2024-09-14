package net.chesstango.search.smart.negamax;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.MoveEvaluation;
import net.chesstango.search.MoveEvaluationType;
import net.chesstango.search.SearchResultByDepth;
import net.chesstango.search.SearchResult;
import net.chesstango.search.smart.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class NegaMax implements SearchAlgorithm {

    private static final int DEFAULT_MAX_PLIES = 4;
    private Evaluator evaluator;
    private Game game;
    private int maxPly;
    private MoveEvaluation bestMoveEvaluation;

    @Override
    public void search() {
        final List<Move> bestMoves = new ArrayList<Move>();
        final Color currentTurn = game.getChessPosition().getCurrentTurn();

        final boolean minOrMax = Color.WHITE.equals(currentTurn) ? false : true;
        int betterEvaluation = Evaluator.INFINITE_NEGATIVE;

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

        bestMoveEvaluation =  new MoveEvaluation(bestMove, minOrMax ? -betterEvaluation : betterEvaluation, MoveEvaluationType.EXACT);
    }


    protected int negaMax(Game game, final int currentPly) {
        int betterEvaluation = Evaluator.INFINITE_NEGATIVE;

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
    public void beforeSearch(SearchByCycleContext context) {
        this.game = context.getGame();
        this.evaluator.setGame(context.getGame());
    }

    @Override
    public void afterSearch(SearchResult result) {
        result.setBestMoveEvaluation(bestMoveEvaluation);
    }

    @Override
    public void beforeSearchByDepth(SearchByDepthContext context) {
        this.maxPly = context.getMaxPly();
        this.bestMoveEvaluation = null;
    }

    @Override
    public void afterSearchByDepth(SearchResultByDepth result) {
        result.setBestMoveEvaluation(bestMoveEvaluation);
    }

    public void setGameEvaluator(Evaluator evaluator) {
        this.evaluator = new NegaMaxEvaluatorWrapper(evaluator);
    }
}
