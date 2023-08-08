package net.chesstango.search.smart;

import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;

/**
 * @author Mauricio Coria
 */
public interface SearchSmart extends SearchLifeCycle {

    /**
     * Invoked once per search depth
     */
    SearchMoveResult search(SearchContext context);
}
