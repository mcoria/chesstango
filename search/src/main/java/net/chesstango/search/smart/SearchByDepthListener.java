package net.chesstango.search.smart;

import net.chesstango.search.SearchResultByDepth;

/**
 * @author Mauricio Coria
 */
public interface SearchByDepthListener extends SearchListener {

    /**
     * Invoked once per search depth
     * If Iterating Deeping is disabled then once,
     * If Iterating Deeping is enabled then multiple times from depth 1 to maxDepth
     */
    void beforeSearchByDepth(SearchByDepthContext context);

    /**
     * Invoked once per search depth
     * If Iterating Deeping is disabled then once,
     * If Iterating Deeping is enabled then multiple times from depth 1 to maxDepth
     * If result == null, it means search was stopped before completion
     */
    default void afterSearchByDepth(SearchResultByDepth result) {
    }
}
