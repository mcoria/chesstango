package net.chesstango.search.smart.statistics;

import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;

/**
 * @author Mauricio Coria
 */
public class GameStatisticsByCycleListener implements SearchByCycleListener {
    private GameStatistics gameStatistic;

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        gameStatistic = (GameStatistics) context.getGame();
        gameStatistic.setExecutedMoves(0);
    }

    @Override
    public void afterSearch(SearchMoveResult result) {
        result.setExecutedMoves(gameStatistic.getExecutedMoves());
    }
}
