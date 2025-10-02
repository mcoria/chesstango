package net.chesstango.engine;

import net.chesstango.search.SearchResult;

/**
 * Podria buscar OnLine
 *
 * @author Mauricio Coria
 */
class SearchByOnline implements SearchChain {

    @Override
    public SearchResult search(SearchContext context) {
        return null;
    }

    @Override
    public void stopSearching() {

    }

    @Override
    public void reset() {

    }

    @Override
    public void close() throws Exception {

    }
}
