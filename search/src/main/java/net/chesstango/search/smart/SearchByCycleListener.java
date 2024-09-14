package net.chesstango.search.smart;

import net.chesstango.search.SearchResult;

/**
 * @author Mauricio Coria
 */
public interface SearchByCycleListener extends SearchListener {

    /**
     * Invoked once before searching
     */
    void beforeSearch(SearchByCycleContext context);


    /**
     * Invoked once after searching is done
     */
    default void afterSearch(SearchResult result) {
    }
}
