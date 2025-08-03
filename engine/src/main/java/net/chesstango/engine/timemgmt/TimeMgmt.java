package net.chesstango.engine.timemgmt;

import net.chesstango.board.Game;
import net.chesstango.search.SearchResultByDepth;

/**
 * Interface for managing time control during chess engine searches.
 * Implementations handle time allocation and decisions about continuing or stopping searches
 * based on remaining time and search progress.
 *
 * @author Mauricio Coria
 */
public interface TimeMgmt {
    /**
     * Calculates the time allocation for the current position search.
     *
     * @param game  Current game state
     * @param wTime Remaining time for white player in milliseconds
     * @param bTime Remaining time for black player in milliseconds
     * @param wInc  Time increment for white player after each move in milliseconds
     * @param bInc  Time increment for black player after each move in milliseconds
     * @return Allocated time for the search in milliseconds
     */
    int getTimeOut(Game game, int wTime, int bTime, int wInc, int bInc);

    /**
     * Determines whether to continue the search based on elapsed time and search progress.
     *
     * @param timeOut             Maximum allocated time for the search in milliseconds
     * @param searchResultByDepth Current search results including evaluation and best moves
     * @return true if the search should continue, false if it should stop
     */
    boolean keepSearching(int timeOut, SearchResultByDepth searchResultByDepth);
}
