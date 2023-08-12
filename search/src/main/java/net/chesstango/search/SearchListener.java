package net.chesstango.search;

/**
 * @author Mauricio Coria
 */
public interface SearchListener {
    default void searchStarted() {
    }

    default void searchInfo(SearchInfo info) {
    }

    default void searchStopped() {
    }

    default void searchFinished(SearchMoveResult searchResult) {
    }
}
