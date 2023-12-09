package net.chesstango.search.smart;

import net.chesstango.search.SearchMoveResult;

/**
 * @author Mauricio Coria
 */
public interface SearchByCycleListener extends SmartListener {

    /**
     * Invoked once before searching
     */
    void beforeSearch(SearchByCycleContext context);


    /**
     * Invoked once after searching is done
     */
    void afterSearch(SearchMoveResult result);
}
