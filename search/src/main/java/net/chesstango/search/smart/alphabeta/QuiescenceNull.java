package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Game;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.smart.SearchContext;

/**
 * @author Mauricio Coria
 */
public class QuiescenceNull implements AlphaBetaSearch {

    private GameEvaluator evaluator;

    @Override
    public int minimize(Game game, final int currentPly, final int alpha, final int beta, final SearchContext context) {
        return evaluator.evaluate(game);
    }

    @Override
    public int maximize(Game game, final int currentPly, final int alpha, final int beta, final SearchContext context){
        return evaluator.evaluate(game);
    }

    @Override
    public void stopSearching() {
    }

    public void setGameEvaluator(GameEvaluator evaluator) {
        this.evaluator = evaluator;
    }

}
