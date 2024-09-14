package net.chesstango.search.smart;

/**
 * @author Mauricio Coria
 */
public interface SearchAlgorithm extends SearchByCycleListener, SearchByDepthListener {

    /**
     * Invoked once per search depth
     */
    void search();

}
