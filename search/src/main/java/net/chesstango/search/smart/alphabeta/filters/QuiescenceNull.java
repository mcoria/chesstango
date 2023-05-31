package net.chesstango.search.smart.alphabeta.filters;

import net.chesstango.board.Game;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchContext;

/**
 * @author Mauricio Coria
 */
public class QuiescenceNull implements AlphaBetaFilter {

    private GameEvaluator evaluator;

    private Game game;

    @Override
    public void initSearch(Game game, int maxDepth) {
        this.game = game;
    }

    @Override
    public void closeSearch(SearchMoveResult result) {

    }

    @Override
    public void init(SearchContext context) {
    }

    @Override
    public void close(SearchMoveResult result) {
    }

    @Override
    public void stopSearching() {

    }

    @Override
    public void reset() {

    }

    @Override
    public long minimize(final int currentPly, final int alpha, final int beta) {
        return evaluator.evaluate(game);
    }

    @Override
    public long maximize(final int currentPly, final int alpha, final int beta) {
        return evaluator.evaluate(game);
    }

    public void setGameEvaluator(GameEvaluator evaluator) {
        this.evaluator = evaluator;
    }

}
