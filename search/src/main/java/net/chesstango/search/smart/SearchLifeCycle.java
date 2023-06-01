package net.chesstango.search.smart;

import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;

/**
 * @author Mauricio Coria
 */
public interface SearchLifeCycle {

    /**
     * Invoked once before searching
     */
    void beforeSearch(Game game, int maxDepth);

    /**
     * Invoked once after searching is done
     */
    void afterSearch(SearchMoveResult result);

    /**
     * Invoked multiple times per search depth.
     * If Iterating Deeping is disabled then once,
     * If Iterating Deeping is enabled then multiple times from depth 1 to maxDepth
     */
    void init(SearchContext context);

    /**
     * Invoked multiple times per search depth.
     * If Iterating Deeping is disabled then once,
     * If Iterating Deeping is enabled then multiple times from depth 1 to maxDepth
     */
    void close(SearchMoveResult result);


    void stopSearching();


    void reset();
}
