package net.chesstango.search;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.visitors.*;


/**
 * @author Mauricio Coria
 */
public class NoIterativeDeepening implements Search {

    @Getter
    private final SearchByDepth searchByDepth;

    @Getter
    private final ListenerMediator listenerMediator;

    @Setter
    private int maxDepth = Integer.MAX_VALUE;

    public NoIterativeDeepening(SearchByDepth searchByDepth, ListenerMediator listenerMediator) {
        this.searchByDepth = searchByDepth;
        this.listenerMediator = listenerMediator;
    }

    @Override
    public SearchResult startSearch(Game game) {
        if (game.getStatus().isFinalStatus()) {
            throw new RuntimeException("Game is already finished");
        }

        accept(new SetGameVisitor(game));

        listenerMediator.triggerBeforeSearch();

        SearchResultByDepth searchResultByDepth  = searchByDepth.search(maxDepth);

        listenerMediator.triggerAfterSearch();

        SearchResult searchResult = new SearchResult();

        searchResult.addSearchResultByDepth(searchResultByDepth);

        listenerMediator.accept(new CollectSearchResultVisitor(searchResult));

        listenerMediator.accept(new DistributeSearchResultVisitor(searchResult));

        return searchResult;
    }

    @Override
    public void stopSearch() {
        this.listenerMediator.triggerStopSearching();
    }

    @Override
    public void reset() {
        this.listenerMediator.triggerReset();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
