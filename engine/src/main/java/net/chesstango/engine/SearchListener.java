package net.chesstango.engine;

import net.chesstango.search.SearchResultByDepth;

/**
 * @author Mauricio Coria
 */
public interface SearchListener {
    default void searchStarted(){
    }

    default void searchInfo(SearchResultByDepth searchResultByDepth){
    }

    default void searchFinished(SearchResponse searchResponse) {
    }
}
