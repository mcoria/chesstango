package net.chesstango.search.smart;

/**
 * @author Mauricio Coria
 */
public interface SearchByCycleListener extends SearchListener {

    /**
     * Invoked once before searching
     */
    void beforeSearch();


    /**
     * Invoked once after searching is done
     */
    default void afterSearch() {
    }
}
