package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Game;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.smart.SearchContext;

/**
 * @author Mauricio Coria
 */
public class QuiescenceNull implements AlphaBetaFilter {

    private GameEvaluator evaluator;

    private SearchContext context;

    @Override
    public void init(Game game, SearchContext context) {
        this.context = context;
    }
    @Override
    public int minimize(Game game, final int currentPly, final int alpha, final int beta) {
        return evaluator.evaluate(game);
    }

    @Override
    public int maximize(Game game, final int currentPly, final int alpha, final int beta){
        return evaluator.evaluate(game);
    }

    @Override
    public void stopSearching() {
    }

    public void setGameEvaluator(GameEvaluator evaluator) {
        this.evaluator = evaluator;
    }

}
