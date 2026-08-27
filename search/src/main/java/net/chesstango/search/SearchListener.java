package net.chesstango.search;

/**
 * @author Mauricio Coria
 */
public interface SearchListener extends Listener {

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
