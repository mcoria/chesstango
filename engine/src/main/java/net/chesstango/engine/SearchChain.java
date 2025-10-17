package net.chesstango.engine;

/**
 * @author Mauricio Coria
 */
interface SearchChain extends AutoCloseable {

    SearchResponse search(SearchContext context);

    void stopSearching();

    void reset();
}
