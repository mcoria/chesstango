package net.chesstango.engine;

import net.chesstango.search.Search;

/**
 * @author Mauricio Coria
 */
public sealed interface SearchChain extends Search
        permits SearchByAlgorithm, SearchByOpenBook, SearchByTablebase {

    void open();

    void close();
}
