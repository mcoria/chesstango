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
    private final SearchByDepth searchByDepth;

    @Getter
    private final SearchListenerMediator searchListenerMediator;

    @Setter
    private int maxDepth = Integer.MAX_VALUE;

    public NoIterativeDeepening(SearchByDepth searchByDepth, SearchListenerMediator searchListenerMediator) {
        this.searchByDepth = searchByDepth;
        this.searchListenerMediator = searchListenerMediator;
    }

    @Override
    public SearchResult startSearch(Game game) {
        if (game.getStatus().isFinalStatus()) {
            throw new RuntimeException("Game is already finished");
        }

        accept(new SetGameVisitor(game));

        searchListenerMediator.triggerBeforeSearch();

        SearchResultByDepth searchResultByDepth  = searchByDepth.search(maxDepth);

        searchListenerMediator.triggerAfterSearch();

        SearchResult searchResult = new SearchResult();

        searchResult.addSearchResultByDepth(searchResultByDepth);

        searchListenerMediator.accept(new CollectSearchResultVisitor(searchResult));

        searchListenerMediator.accept(new DistributeSearchResultVisitor(searchResult));

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
