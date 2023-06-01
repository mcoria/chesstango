package net.chesstango.search.smart.alphabeta.filters;

import net.chesstango.board.Game;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.SearchLifeCycle;

/**
 * @author Mauricio Coria
 */
public class GameEvaluatorCounter implements GameEvaluator, SearchLifeCycle {
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

    @Override
    public void beforeSearch(Game game, int maxDepth) {
        counter = 0;
    }

    @Override
    public void afterSearch(SearchMoveResult result) {
        if(result!=null) {
            result.setEvaluatedGamesCounter(counter);
        }
    }

    @Override
    public void init(SearchContext context) {
    }

    @Override
    public void close(SearchMoveResult result) {
        if(result!=null) {
            result.setEvaluatedGamesCounter(counter);
        }
    }

    @Override
    public void stopSearching() {
    }

    @Override
    public void reset() {
    }

}
