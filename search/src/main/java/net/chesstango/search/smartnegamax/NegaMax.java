package net.chesstango.search.smartnegamax;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMoveResult;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class NegaMax extends AbstractSmart {

    private static final int DEFAULT_MAX_PLIES = 4;
    private GameEvaluator evaluator;

    @Override
    public void setGameEvaluator(GameEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    @Override
    public SearchMoveResult searchBestMove(Game game) {
        return searchBestMove(game, 10);
    }

    // Beyond level 4, the performance is really bad

    @Override
    public SearchMoveResult searchBestMove(Game game, int depth) {
        this.keepProcessing = true;
        final List<Move> bestMoves = new ArrayList<Move>();

        final boolean minOrMax = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? false : true;
        int betterEvaluation = GameEvaluator.INFINITE_NEGATIVE;

        for (Move move : game.getPossibleMoves()) {
            game = game.executeMove(move);

            int currentEvaluation = -minMax(game, depth - 1);

            if (currentEvaluation == betterEvaluation) {
                bestMoves.add(move);
            } else {
                if (currentEvaluation > betterEvaluation) {
                    betterEvaluation = currentEvaluation;
                    bestMoves.clear();
                    bestMoves.add(move);
                }
            }

            game = game.undoMove();
        }

        return new SearchMoveResult(minOrMax ? -betterEvaluation : betterEvaluation, bestMoves.size() - 1, selectMove(game.getChessPosition().getCurrentTurn(), bestMoves), null);
    }

    protected int minMax(Game game, final int currentPly) {

        final boolean minOrMax = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? false : true;
        int betterEvaluation = GameEvaluator.INFINITE_NEGATIVE;

        if (currentPly == 0 || !game.getStatus().isInProgress()) {
            betterEvaluation = minOrMax ? -evaluator.evaluate(game) : evaluator.evaluate(game);
        } else {
            for (Move move : game.getPossibleMoves()) {
                game = game.executeMove(move);

                int currentEvaluation = -minMax(game, currentPly - 1);
                if (currentEvaluation > betterEvaluation) {
                    betterEvaluation = currentEvaluation;
                }

                game = game.undoMove();
            }
        }
        return betterEvaluation;
    }
}
