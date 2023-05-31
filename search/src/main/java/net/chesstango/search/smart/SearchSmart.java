package net.chesstango.search.smart;

import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;

/**
 * @author Mauricio Coria
 */
public interface SearchSmart {

    /**
     * Invoked once per search session before searching
     */
    void initSearch(Game game, int maxDepth);

    /**
     * Invoked once per search session after searching is done
     */
    void closeSearch(SearchMoveResult result);

    /**
     * Reset internal buffers
     */
    void reset();


    SearchMoveResult search(SearchContext context);

    void stopSearching();

}
