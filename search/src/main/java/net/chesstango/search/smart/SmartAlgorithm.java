package net.chesstango.search.smart;

import net.chesstango.search.SearchMoveResult;

/**
 * @author Mauricio Coria
 */
public interface SmartAlgorithm {

    /**
     * Invoked once per search depth
     */
    SearchMoveResult search(SearchContext context);

}
