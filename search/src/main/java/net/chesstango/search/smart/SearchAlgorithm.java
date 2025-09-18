package net.chesstango.search.smart;

import net.chesstango.search.Acceptor;

/**
 * @author Mauricio Coria
 */
public interface SearchAlgorithm extends SearchByCycleListener, SearchByDepthListener, Acceptor {

    /**
     * Invoked once per search depth
     */
    void search();

}
