package net.chesstango.search.smart;

import net.chesstango.search.SearchMoveResult;

/**
 * @author Mauricio Coria
 */
public class StopSearchingException extends RuntimeException {
    private SearchMoveResult searchMoveResult;

    public SearchMoveResult getSearchMoveResult() {
        return searchMoveResult;
    }

    public void setSearchMoveResult(SearchMoveResult searchMoveResult) {
        this.searchMoveResult = searchMoveResult;
    }
}
