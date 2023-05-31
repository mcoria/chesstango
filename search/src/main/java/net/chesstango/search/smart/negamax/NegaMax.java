package net.chesstango.search.smart.negamax;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.MoveSelector;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.SearchSmart;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class NegaMax implements SearchSmart {

    private static final int DEFAULT_MAX_PLIES = 4;

    private GameEvaluator evaluator;

    private Game game;

    public void setGameEvaluator(GameEvaluator evaluator) {
        this.evaluator = new NegaMaxEvaluatorWrapper(evaluator);
    }

    @Override
    public SearchMoveResult search(Game game, int maxDepth) {
        initSearch(game, maxDepth);

        SearchMoveResult searchResult = search(new SearchContext(maxDepth));

        closeSearch(searchResult);
        return searchResult;
    }

    @Override
    public SearchMoveResult search(SearchContext context) {
        final List<Move> bestMoves = new ArrayList<Move>();
        final Color currentTurn = game.getChessPosition().getCurrentTurn();

        final boolean minOrMax = Color.WHITE.equals(currentTurn) ? false : true;
        int betterEvaluation = GameEvaluator.INFINITE_NEGATIVE;

        for (Move move : game.getPossibleMoves()) {
            game.executeMove(move);

            int currentEvaluation = -negaMax(game, context.getMaxPly() - 1);

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

        return new SearchMoveResult(context.getMaxPly(), minOrMax ? -betterEvaluation : betterEvaluation, bestMove, null)
                .setEvaluationCollisions(bestMoves.size() - 1);
    }

    @Override
    public void stopSearching() {

    }


    protected int negaMax(Game game, final int currentPly) {
        int betterEvaluation = GameEvaluator.INFINITE_NEGATIVE;

        if (currentPly == 0 || !game.getStatus().isInProgress()) {
            betterEvaluation = evaluator.evaluate(game);
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
    public void initSearch(Game game, int maxDepth) {
        this.game = game;
    }

    @Override
    public void closeSearch(SearchMoveResult result) {

    }

    @Override
    public void reset() {

    }

}
