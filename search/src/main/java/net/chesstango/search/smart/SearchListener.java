package net.chesstango.search.smart;

import net.chesstango.search.SearchMoveResult;

/**
 * @author Mauricio Coria
 */
public interface SearchListener {
    void init(final SearchContext context);

    void close(SearchMoveResult result);

}
