package net.chesstango.engine;

import net.chesstango.search.Search;
import net.chesstango.search.SearchParameter;
import net.chesstango.search.SearchResult;

/**
 * @author Mauricio Coria
 */
class SearchByAlgorithm implements SearchChain {
    private final Search search;

    public SearchByAlgorithm(Search search) {
        this.search = search;
    }

    @Override
    public void reset() {
        search.reset();
    }

    @Override
    public void stopSearching() {
        search.stopSearch();
    }

    @Override
    public void close() {
    }


    /**
     * TODO: deberia subsribirse al listener para manipular el latcher que habilita el stop
     *
     * @param context
     * @return
     */
    @Override
    public SearchResult search(SearchContext context) {
        search.setSearchParameter(SearchParameter.MAX_DEPTH, context.getDepth());
        search.setSearchParameter(SearchParameter.SEARCH_PREDICATE, context.getSearchPredicate());
        search.setSearchParameter(SearchParameter.SEARCH_BY_DEPTH_LISTENER, context.getSearchResultByDepthListener());
        return search.startSearch(context.getGame());
    }
}
