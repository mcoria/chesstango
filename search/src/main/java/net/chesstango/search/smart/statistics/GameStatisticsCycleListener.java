package net.chesstango.search.smart.statistics;

import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchCycleListener;

/**
 * @author Mauricio Coria
 */
public class GameStatisticsCycleListener implements SearchCycleListener {
    private GameStatistics gameStatistic;

    @Override
    public void beforeSearch(Game game) {
        gameStatistic = (GameStatistics) game;
        gameStatistic.setExecutedMoves(0);
    }

    @Override
    public void afterSearch(SearchMoveResult result) {
        result.setExecutedMoves(gameStatistic.getExecutedMoves());
    }
}
