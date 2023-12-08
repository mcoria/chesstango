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
     * Stop searching. This method may be called while another thread is searching
     */
    void stopSearching();

    /**
     * Reset internal buffers
     */
    void reset();
}
