package net.chesstango.search.smart;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.*;
import net.chesstango.search.visitors.SetGameVisitor;
import net.chesstango.search.visitors.SetSearchMaxPlyVisitor;


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
        accept(new SetGameVisitor(game));

        searchListenerMediator.triggerBeforeSearch();

        searchListenerMediator.accept(new SetSearchMaxPlyVisitor(maxDepth));

        searchListenerMediator.triggerBeforeSearchByDepth();

        searchAlgorithm.search();

        SearchResultByDepth searchResultByDepth = new SearchResultByDepth(maxDepth);

        searchListenerMediator.triggerAfterSearchByDepth(searchResultByDepth);

        SearchResult searchResult = new SearchResult();

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
