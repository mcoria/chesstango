package net.chesstango.search.smart;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.Search;
import net.chesstango.search.SearchResult;
import net.chesstango.search.SearchResultByDepth;
import net.chesstango.search.Visitor;
import net.chesstango.search.visitors.*;


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
        if (game.getStatus().isFinalStatus()) {
            throw new RuntimeException("Game is already finished");
        }

        accept(new SetGameVisitor(game));

        searchListenerMediator.triggerBeforeSearch();

        searchListenerMediator.accept(new SetDepthVisitor(maxDepth));

        searchListenerMediator.triggerBeforeSearchByDepth();

        searchAlgorithm.search();

        searchListenerMediator.triggerAfterSearchByDepth();

        SearchResultByDepth searchResultByDepth = new SearchResultByDepth(maxDepth);

        searchListenerMediator.accept(new CollectSearchResultByDepthVisitor(searchResultByDepth));

        searchListenerMediator.accept(new DistributeSearchResultByDepthVisitor(searchResultByDepth));

        SearchResult searchResult = new SearchResult();

        searchResult.addSearchResultByDepth(searchResultByDepth);

        searchListenerMediator.accept(new CollectSearchResultVisitor(searchResult));

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
