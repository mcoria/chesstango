package net.chesstango.search.smart;

import net.chesstango.board.Game;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;

/**
 * @author Mauricio Coria
 */
public class NoIterativeDeepening implements SearchMove {
    private final SearchSmart searchSmart;

    public NoIterativeDeepening(SearchSmart searchSmartAlgorithm) {
        this.searchSmart = searchSmartAlgorithm;
    }

    @Override
    public SearchMoveResult search(Game game, int depth) {
        searchSmart.beforeSearch(game, depth);

        SearchContext context = new SearchContext(depth);

        searchSmart.beforeSearchByDepth(context);

        SearchMoveResult searchResult = searchSmart.search(context);

        searchSmart.afterSearchByDepth(searchResult);

        searchSmart.afterSearch(searchResult);

        return searchResult;
    }

    @Override
    public void stopSearching() {
        this.searchSmart.stopSearching();
    }

    @Override
    public void reset() {
        this.searchSmart.reset();
    }

}
