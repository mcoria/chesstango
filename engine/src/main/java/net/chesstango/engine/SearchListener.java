package net.chesstango.engine;

/**
 *
 * Engine Listener ????
 *
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
