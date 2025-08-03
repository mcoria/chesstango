package net.chesstango.search;

import net.chesstango.board.Game;

/**
 * @author Mauricio Coria
 */
public interface Search {

    /**
     * Search up to depth
     */
    SearchResult search(Game game);

    /**
     * Stop searching. This method may be called while another thread is searching
     */
    void stopSearching();

    /**
     * Reset internal counters and buffers (for instance TT)
     */
    void reset();

    /**
     * Set search parameters
     */
    void setSearchParameter(SearchParameter parameter, Object value);

}
