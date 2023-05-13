package net.chesstango.search.smart;

import net.chesstango.search.SearchMoveResult;

/**
 * @author Mauricio Coria
 */
public interface SearchLifeCycle {
    void init(SearchContext context);

    void close(SearchMoveResult result);

}
