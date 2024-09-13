package net.chesstango.search.smart.features.statistics.game;

import lombok.Getter;
import net.chesstango.board.Game;
import net.chesstango.search.ProgressListener;
import net.chesstango.search.Search;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.SearchParameter;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;

/**
 * @author Mauricio Coria
 */
public class SearchGameWrapper implements Search, SearchByCycleListener {

    @Getter
    private final Search imp;

    @Getter
    private GameStatisticsWrapper gameStatisticsWrapper;

    public SearchGameWrapper(Search imp) {
        this.imp = imp;
    }

    @Override
    public SearchMoveResult search(Game game) {
        gameStatisticsWrapper = new GameStatisticsWrapper(game);
        return imp.search(gameStatisticsWrapper);
    }

    @Override
    public void stopSearching() {
        imp.stopSearching();
    }

    @Override
    public void reset() {
        imp.reset();
    }

    @Override
    public void setSearchParameter(SearchParameter parameter, Object value) {
        imp.setSearchParameter(parameter, value);
    }

    @Override
    public void setProgressListener(ProgressListener progressListener) {
        imp.setProgressListener(progressListener);
    }

    @Override
    public void beforeSearch(SearchByCycleContext context) {
    }

    @Override
    public void afterSearch(SearchMoveResult result) {
        result.setExecutedMoves(gameStatisticsWrapper.getExecutedMoves());
    }

}
