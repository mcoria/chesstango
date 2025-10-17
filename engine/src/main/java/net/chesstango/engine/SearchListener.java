package net.chesstango.engine;

/**
 * @author Mauricio Coria
 */
public interface SearchListener {
    default void searchStarted(){
    }

    default void searchInfo(String searchInfo){
    }

    default void searchFinished(SearchResponse searchResponse) {
    }
}
