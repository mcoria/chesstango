package net.chesstango.search;

import net.chesstango.board.Game;
import net.chesstango.evaluation.DefaultGameEvaluator;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.smartminmax.IterativeDeeping;

/**
 * @author Mauricio Coria
 */
public class DefaultSearchMove implements SearchMove {

    private final SearchMove imp;

    public DefaultSearchMove() {
        this.imp = new IterativeDeeping();
        this.setGameEvaluator(new DefaultGameEvaluator());
    }

    @Override
    public SearchMoveResult searchBestMove(Game game) {
        return imp.searchBestMove(game);
    }

    @Override
    public SearchMoveResult searchBestMove(Game game, int depth) {
        return imp.searchBestMove(game, depth);
    }

    @Override
    public void stopSearching() {
        imp.stopSearching();
    }

    @Override
    public void setGameEvaluator(GameEvaluator evaluator) {
        this.imp.setGameEvaluator(evaluator);
    }
}
