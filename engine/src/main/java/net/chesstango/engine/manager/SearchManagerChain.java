package net.chesstango.engine.manager;

import net.chesstango.search.Search;

/**
 * @author Mauricio Coria
 */
public sealed interface SearchManagerChain extends Search
        permits SearchManagerByAlgorithm, SearchManagerByOpenBook, SearchManagerByTablebase {

    void open();

    void close();
}
