package net.chesstango.search.smart;

import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.SearchParameter;

import static net.chesstango.search.SearchParameter.MAX_DEPTH;

/**
 * @author Mauricio Coria
 */
public class NoIterativeDeepening implements SearchMove {
    private final SearchSmart searchSmart;

    @Setter
    private SmartListenerMediator smartListenerMediator;

    private int maxDepth = Integer.MAX_VALUE;

    public NoIterativeDeepening(SearchSmart searchSmartAlgorithm) {
        this.searchSmart = searchSmartAlgorithm;
    }

    @Override
    public SearchMoveResult search(Game game) {
        smartListenerMediator.triggerBeforeSearch(game);

        SearchContext context = new SearchContext(maxDepth);

        searchSmart.beforeSearchByDepth(context);

        SearchMoveResult searchResult = searchSmart.search(context);

        searchSmart.afterSearchByDepth(searchResult);

        smartListenerMediator.triggerAfterSearch(searchResult);

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

    @Override
    public void setParameter(SearchParameter parameter, Object value) {
        if (MAX_DEPTH.equals(parameter) && value instanceof Integer maxDepthParam) {
            maxDepth = maxDepthParam;
        }
    }

}
