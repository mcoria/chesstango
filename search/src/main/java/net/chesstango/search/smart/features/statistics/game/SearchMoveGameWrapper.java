package net.chesstango.search.smart.features.statistics.game;

import lombok.Getter;
import net.chesstango.board.Game;
import net.chesstango.search.ProgressListener;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.SearchParameter;

/**
 * @author Mauricio Coria
 */
public class SearchMoveGameWrapper implements SearchMove {

    @Getter
    private final SearchMove imp;

    public SearchMoveGameWrapper(SearchMove imp) {
        this.imp = imp;
    }

    @Override
    public SearchMoveResult search(Game game) {
        GameStatisticsWrapper gameStatisticsWrapper = new GameStatisticsWrapper(game);
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


}
