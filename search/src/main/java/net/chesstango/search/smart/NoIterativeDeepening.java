package net.chesstango.search.smart;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.*;


/**
 * @author Mauricio Coria
 */
public class NoIterativeDeepening implements Search {

    @Getter
    private final SearchAlgorithm searchAlgorithm;

    @Getter
    private final SearchListenerMediator searchListenerMediator;

    @Setter
    private int maxDepth = Integer.MAX_VALUE;

    public NoIterativeDeepening(SearchAlgorithm searchAlgorithm, SearchListenerMediator searchListenerMediator) {
        this.searchAlgorithm = searchAlgorithm;
        this.searchListenerMediator = searchListenerMediator;
    }

    @Override
    public SearchResult startSearch(Game game) {
        SearchResult searchResult = new SearchResult();

        SearchByCycleContext searchByCycleContext = new SearchByCycleContext(game);

        searchListenerMediator.triggerBeforeSearch(searchByCycleContext);

        SearchByDepthContext context = new SearchByDepthContext(maxDepth);

        searchListenerMediator.triggerBeforeSearchByDepth(context);

        searchAlgorithm.search();

        SearchResultByDepth searchResultByDepth = new SearchResultByDepth(maxDepth);

        searchListenerMediator.triggerAfterSearchByDepth(searchResultByDepth);

        searchResult.addSearchResultByDepth(searchResultByDepth);

        searchListenerMediator.triggerAfterSearch(searchResult);

        return searchResult;
    }

    @Override
    public void stopSearch() {
        this.searchListenerMediator.triggerStopSearching();
    }

    @Override
    public void reset() {
        this.searchListenerMediator.triggerReset();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
