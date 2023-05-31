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
        searchSmart.initSearch(game, depth);

        SearchContext context = new SearchContext(depth);

        SearchMoveResult result = searchSmart.search(context);

        searchSmart.closeSearch(result);

        return result;
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
