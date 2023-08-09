package net.chesstango.search;

/**
 * @author Mauricio Coria
 */
public interface SearchListener {
    void searchStarted();

    void searchInfo(SearchInfo info);

    void searchStopped();

    void searchFinished(SearchMoveResult searchResult);
}
