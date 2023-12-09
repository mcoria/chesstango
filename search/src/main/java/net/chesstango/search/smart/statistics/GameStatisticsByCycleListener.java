package net.chesstango.search.smart.statistics;

import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.SearchByDepthContext;
import net.chesstango.search.smart.SearchByDepthListener;

/**
 * @author Mauricio Coria
 */
public class GameStatisticsByCycleListener implements SearchByCycleListener, SearchByDepthListener {
    private GameStatistics gameStatistic;

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        gameStatistic = (GameStatistics) context.getGame();
        gameStatistic.setExecutedMoves(0);
    }

    @Override
    public void afterSearch() {
    }

    @Override
    public void beforeSearchByDepth(SearchByDepthContext context) {

    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {
        result.setExecutedMoves(gameStatistic.getExecutedMoves());
    }
}
