package net.chesstango.search;

/**
 * @author Mauricio Coria
 */
public interface SearchListener {
    void searchStarted();

    void searchStopped();

    void searchFinished(SearchMoveResult searchResult);
}
