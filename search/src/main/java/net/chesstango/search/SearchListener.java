package net.chesstango.search;

/**
 * @author Mauricio Coria
 */
public interface SearchListener {
    default void searchStarted(){
    }

    default void searchInfo(SearchMoveResult info){
    }

    default void searchFinished(SearchMoveResult searchResult) {
    }
}
