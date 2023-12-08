package net.chesstango.search.smart;

import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;

/**
 * @author Mauricio Coria
 */
public interface SearchSmart {

    /**
     * Invoked once per search depth
     */
    SearchMoveResult search(SearchContext context);


    /**
     * Invoked once per search depth
     * If Iterating Deeping is disabled then once,
     * If Iterating Deeping is enabled then multiple times from depth 1 to maxDepth
     */
    void beforeSearchByDepth(SearchContext context);

    /**
     * Invoked once per search depth
     * If Iterating Deeping is disabled then once,
     * If Iterating Deeping is enabled then multiple times from depth 1 to maxDepth
     * If result == null, it means search was stopped before completion
     */
    void afterSearchByDepth(SearchMoveResult result);

    /**
     * Stop searching. This method may be called while another thread is searching
     */
    void stopSearching();

    /**
     * Reset internal buffers
     */
    void reset();
}
