package net.chesstango.search.smart;

import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;

/**
 * @author Mauricio Coria
 */
public interface SearchLifeCycle {
    /**
     * Invoked once per search session before searching
     */
    void initSearch(Game game, int maxDepth);

    /**
     * Invoked once per search session after searching is done
     */
    void closeSearch(SearchMoveResult result);

    /**
     * Invoked multiple times per search session
     */
    void init(SearchContext context);

    /**
     * Invoked multiple times per search session
     */
    void close(SearchMoveResult result);


    void reset();
}
