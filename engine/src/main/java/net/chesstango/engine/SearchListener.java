package net.chesstango.engine;

import net.chesstango.search.SearchMoveResult;

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
