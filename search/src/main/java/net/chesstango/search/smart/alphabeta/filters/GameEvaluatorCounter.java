package net.chesstango.search.smart.alphabeta.filters;

import net.chesstango.board.Game;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchListener;
import net.chesstango.search.smart.SearchContext;

/**
 * @author Mauricio Coria
 */
public class GameEvaluatorCounter implements GameEvaluator, SearchListener {
    private GameEvaluator imp;

    private long counter;


    public GameEvaluatorCounter(GameEvaluator instance) {
        this.imp = instance;
    }

    @Override
    public int evaluate(Game game) {
        counter++;
        return imp.evaluate(game);
    }


    public long getCounter() {
        return counter;
    }

    @Override
    public void init(Game game, SearchContext context) {

    }

    @Override
    public void close(SearchMoveResult result) {
        result.setEvaluatedGamesCounter(counter);
    }

}
