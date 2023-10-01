package net.chesstango.search.smart.alphabeta.filters;

import net.chesstango.board.Game;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.StopSearchingException;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.transposition.TranspositionEntry;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaFlowControl implements AlphaBetaFilter {
    private volatile boolean keepProcessing;

    private GameEvaluator evaluator;

    private AlphaBetaFilter next;

    private AlphaBetaFilter quiescence;

    private int maxPly;
    private Game game;

    @Override
    public void beforeSearch(Game game, int maxDepth) {
        this.game = game;
        this.keepProcessing = true;
    }

    @Override
    public void beforeSearchByDepth(SearchContext context) {
        this.maxPly = context.getMaxPly();
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {
    }

    @Override
    public void afterSearch(SearchMoveResult result) {
    }

    @Override
    public void stopSearching() {
        this.keepProcessing = false;
    }

    @Override
    public void reset() {
    }

    @Override
    public long maximize(int currentPly, int alpha, int beta) {
        if (!keepProcessing) {
            throw new StopSearchingException();
        }
        if (!game.getStatus().isInProgress()) {
            return TranspositionEntry.encode(evaluator.evaluate(game));
        }
        if (currentPly == maxPly) {
            return quiescence.maximize(currentPly, alpha, beta);
        }

        return next.maximize(currentPly, alpha, beta);
    }

    @Override
    public long minimize(int currentPly, int alpha, int beta) {
        if (!keepProcessing) {
            throw new StopSearchingException();
        }
        if (!game.getStatus().isInProgress()) {
            return TranspositionEntry.encode(evaluator.evaluate(game));
        }
        if (currentPly == maxPly) {
            return quiescence.minimize(currentPly, alpha, beta);
        }

        return next.minimize(currentPly, alpha, beta);
    }

    public void setQuiescence(AlphaBetaFilter quiescence) {
        this.quiescence = quiescence;
    }

    public void setGameEvaluator(GameEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    public void setNext(AlphaBetaFilter next) {
        this.next = next;
    }
}
