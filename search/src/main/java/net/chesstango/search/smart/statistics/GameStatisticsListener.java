package net.chesstango.search.smart.statistics;

import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.SearchLifeCycle;

/**
 * @author Mauricio Coria
 */
public class GameStatisticsListener implements SearchLifeCycle {
    private GameStatistics gameStatistic;

    @Override
    public void beforeSearch(Game game) {
        gameStatistic = (GameStatistics) game;
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

    @Override
    public void stopSearching() {
    }

    @Override
    public void reset() {
    }
}
