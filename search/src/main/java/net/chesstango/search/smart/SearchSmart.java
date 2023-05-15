package net.chesstango.search.smart;

import net.chesstango.search.SearchListener;
import net.chesstango.search.SearchMoveResult;

/**
 * @author Mauricio Coria
 */
public interface SearchSmart {

    SearchMoveResult search(SearchContext context);

    void stopSearching();

    void setSearchListener(SearchListener searchListener);
}
