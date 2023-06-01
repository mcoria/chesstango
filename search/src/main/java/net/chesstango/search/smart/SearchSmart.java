package net.chesstango.search.smart;

import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;

/**
 * @author Mauricio Coria
 */
public interface SearchSmart {

    /**
     * Invoked once before searching
     */
    void beforeSearch(Game game, int maxDepth);

    /**
     * Invoked once after searching is done
     */
    void afterSearch(SearchMoveResult result);

    /**
     * Reset internal buffers
     */
    void reset();


    SearchMoveResult search(SearchContext context);

    void stopSearching();

}
