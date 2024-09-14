package net.chesstango.engine;

import net.chesstango.search.SearchResult;
import net.chesstango.search.SearchByDepthResult;

/**
 * @author Mauricio Coria
 */
public interface SearchListener {
    default void searchStarted(){
    }

    default void searchInfo(SearchByDepthResult searchByDepthResult){
    }

    default void searchFinished(SearchResult searchResult) {
    }
}
