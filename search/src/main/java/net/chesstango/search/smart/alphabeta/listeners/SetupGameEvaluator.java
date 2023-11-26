package net.chesstango.search.smart.alphabeta.listeners;

import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.SearchLifeCycle;

/**
 * @author Mauricio Coria
 */
public class SetupGameEvaluator implements SearchLifeCycle {

    @Setter
    private GameEvaluator gameEvaluator;

    @Override
    public void beforeSearch(Game game) {
        gameEvaluator.setGame(game);
    }

    @Override
    public void beforeSearchByDepth(SearchContext context) {

    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {

    }

    @Override
    public void afterSearch(SearchMoveResult result) {

    }

    @Override
    public void stopSearching() {

    }

    @Override
    public void reset() {

    }
}
