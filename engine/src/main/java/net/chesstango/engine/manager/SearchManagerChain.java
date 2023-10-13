package net.chesstango.engine.manager;

import net.chesstango.search.SearchMove;

/**
 * @author Mauricio Coria
 */
public sealed interface SearchManagerChain extends SearchMove
        permits SearchManagerByAlgorithm, SearchManagerByBook {

    void open();

    void close();
}
