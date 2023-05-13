package net.chesstango.search.manager;

import net.chesstango.search.SearchMoveResult;

/**
 * @author Mauricio Coria
 */
public interface SearchListener {
    void searchStarted();

    void searchStopped();

    void searchFinished(SearchMoveResult result);
}
