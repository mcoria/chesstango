package net.chesstango.engine.manager;

import net.chesstango.board.Game;
import net.chesstango.search.Search;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.SearchParameter;
import net.chesstango.search.ProgressListener;

/**
 * @author Mauricio Coria
 */
public final class SearchManagerByAlgorithm implements SearchManagerChain {
    private final Search search;

    public SearchManagerByAlgorithm(Search search) {
        this.search = search;
    }

    @Override
    public void reset() {
        search.reset();
    }

    @Override
    public void setSearchParameter(SearchParameter parameter, Object value) {
        search.setSearchParameter(parameter, value);
    }

    @Override
    public void setProgressListener(ProgressListener progressListener) {
        search.setProgressListener(progressListener);
    }

    @Override
    public void stopSearching() {
        search.stopSearching();
    }

    @Override
    public void open() {
    }

    @Override
    public void close() {
    }

    @Override
    public SearchMoveResult search(Game game) {
        return search.search(game);
    }
}
