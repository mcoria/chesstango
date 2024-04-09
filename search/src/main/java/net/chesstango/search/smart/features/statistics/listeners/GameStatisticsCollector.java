package net.chesstango.search.smart.features.statistics.listeners;

import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.SearchByDepthContext;
import net.chesstango.search.smart.SearchByDepthListener;
import net.chesstango.search.smart.features.statistics.GameStatisticsWrapper;

/**
 * @author Mauricio Coria
 */
public class GameStatisticsCollector implements SearchByCycleListener, SearchByDepthListener {
    private GameStatisticsWrapper gameStatistic;

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        gameStatistic = (GameStatisticsWrapper) context.getGame();
        gameStatistic.setExecutedMoves(0);
    }

    @Override
    public void afterSearch(SearchMoveResult searchMoveResult) {
        searchMoveResult.setExecutedMoves(gameStatistic.getExecutedMoves());
    }

    @Override
    public void beforeSearchByDepth(SearchByDepthContext context) {

    }
}
