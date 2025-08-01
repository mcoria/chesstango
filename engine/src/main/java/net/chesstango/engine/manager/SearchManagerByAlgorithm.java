package net.chesstango.engine.manager;

import net.chesstango.board.Game;
import net.chesstango.search.Search;
import net.chesstango.search.SearchResult;
import net.chesstango.search.SearchParameter;
import net.chesstango.search.SearchResultByDepthListener;

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
    public void setSearchResultByDepthListener(SearchResultByDepthListener searchResultByDepthListener) {
        search.setSearchResultByDepthListener(searchResultByDepthListener);
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
    public SearchResult search(Game game) {
        return search.search(game);
    }
}
