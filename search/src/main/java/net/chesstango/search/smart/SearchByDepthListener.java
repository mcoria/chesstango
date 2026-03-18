package net.chesstango.search.smart;

/**
 * @author Mauricio Coria
 */
public interface SearchByDepthListener extends SearchListener {

    /**
     * Invoked once per search depth
     * If Iterating Deeping is disabled then once,
     * If Iterating Deeping is enabled then multiple times from depth 1 to exploredDepth
     */
    void beforeSearchByDepth();

    /**
     * Invoked once per search depth
     * If Iterating Deeping is disabled then once,
     * If Iterating Deeping is enabled then multiple times from depth 1 to exploredDepth
     */
    default void afterSearchByDepth() {
    }
}
