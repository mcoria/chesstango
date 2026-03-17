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
    void beforeSearchByDepth();

    /**
     * Invoked once per search depth
     * If Iterating Deeping is disabled then once,
     * If Iterating Deeping is enabled then multiple times from depth 1 to maxDepth
     */
    default void afterSearchByDepth() {
    }

    /**
     * Invoked once per search depth
     * If Iterating Deeping is disabled then once,
     * If Iterating Deeping is enabled then multiple times from depth 1 to maxDepth
     */
    default void searchByDepthCompleted(SearchResultByDepth searchResultByDepth){

    }
}
