package net.chesstango.engine;

import net.chesstango.search.DefaultSearch;
import net.chesstango.search.Search;
import net.chesstango.search.SearchParameter;
import net.chesstango.search.SearchResult;

/**
 * @author Mauricio Coria
 */
class SearchByAlgorithm implements SearchChain {
    private final Search search;

    public SearchByAlgorithm() {
        this(new DefaultSearch());
    }

    public SearchByAlgorithm(Search search) {
        this.search = search;
    }

    @Override
    public void reset() {
        search.reset();
    }

    @Override
    public void stopSearching() {
        search.stopSearching();
    }

    @Override
    public void close() {
    }

    @Override
    public SearchResult search(SearchContext context) {
        search.setSearchParameter(SearchParameter.MAX_DEPTH, context.getSearchPredicate());
        search.setSearchParameter(SearchParameter.SEARCH_PREDICATE, context.getSearchPredicate());
        return search.search(context.getGame());
    }
}
