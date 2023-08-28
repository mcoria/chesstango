package net.chesstango.search.smart.statistics;

import net.chesstango.board.Game;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.SearchLifeCycle;

/**
 * @author Mauricio Coria
 */
public class IterativeWrapper implements SearchMove, SearchLifeCycle {
    private final SearchMove imp;
    private GameStatistics gameStatistic;

    public IterativeWrapper(SearchMove imp) {
        this.imp = imp;
    }

    @Override
    public SearchMoveResult search(Game game, int depth) {
        gameStatistic = new GameStatistics(game);
        return imp.search(gameStatistic, depth);
    }

    @Override
    public void stopSearching() {
        imp.stopSearching();
    }

    @Override
    public void reset() {
        gameStatistic.setExecutedMoves(0);
    }

    @Override
    public void beforeSearch(Game game, int maxDepth) {
        gameStatistic.setExecutedMoves(0);
    }

    @Override
    public void beforeSearchByDepth(SearchContext context) {
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {
    }

    @Override
    public void afterSearch(SearchMoveResult result) {
        result.setExecutedMoves(gameStatistic.getExecutedMoves());
    }
}
