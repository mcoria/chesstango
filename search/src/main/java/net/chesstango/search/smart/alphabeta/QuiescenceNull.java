package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Game;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.smart.SearchContext;

/**
 * @author Mauricio Coria
 */
public class QuiescenceNull implements AlphaBetaFilter {

    private GameEvaluator evaluator;

    private Game game;

    @Override
    public void init(Game game, SearchContext context) {
        this.game = game;
    }

    @Override
    public long minimize(final int currentPly, final int alpha, final int beta) {
        return evaluator.evaluate(game);
    }

    @Override
    public long maximize(final int currentPly, final int alpha, final int beta){
        return evaluator.evaluate(game);
    }

    @Override
    public void stopSearching() {
    }

    public void setGameEvaluator(GameEvaluator evaluator) {
        this.evaluator = evaluator;
    }

}
