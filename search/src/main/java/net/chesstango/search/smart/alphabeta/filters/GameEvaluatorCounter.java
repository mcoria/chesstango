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


    public long getCounter() {
        return counter;
    }

    @Override
    public void initSearch(Game game, int maxDepth) {

    }

    @Override
    public void closeSearch(SearchMoveResult result) {

    }

    @Override
    public void init(SearchContext context) {
        counter = 0;
    }

    @Override
    public void close(SearchMoveResult result) {
        if(result!=null) {
            result.setEvaluatedGamesCounter(counter);
        }
    }

    @Override
    public void reset() {

    }

}
