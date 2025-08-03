package net.chesstango.engine;

import net.chesstango.search.SearchResult;

/**
 * @author Mauricio Coria
 */
interface SearchChain extends AutoCloseable {

    SearchResult search(SearchContext context);

    void stopSearching();

    void reset();
}
