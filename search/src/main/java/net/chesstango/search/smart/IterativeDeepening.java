package net.chesstango.search.smart;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.Search;
import net.chesstango.search.SearchResult;
import net.chesstango.search.SearchResultByDepth;
import net.chesstango.search.Visitor;
import net.chesstango.search.visitors.CollectSearchResultByDepthVisitor;
import net.chesstango.search.visitors.CollectSearchResultVisitor;
import net.chesstango.search.visitors.SetGameVisitor;
import net.chesstango.search.visitors.SetSearchMaxPlyVisitor;

import java.util.function.Consumer;
import java.util.function.Predicate;


/**
 * @author Mauricio Coria
 */
public class IterativeDeepening implements Search {
    private volatile boolean keepProcessing;

    @Getter
    private final SearchAlgorithm searchAlgorithm;

    @Getter
    private final SearchListenerMediator searchListenerMediator;

    @Setter
    private int maxDepth = Integer.MAX_VALUE / 2;

    @Setter
    private Consumer<SearchResultByDepth> searchResultByDepthListener;

    @Setter
    private Predicate<SearchResultByDepth> searchPredicateParameter = searchMoveResult -> true;

    public IterativeDeepening(SearchAlgorithm searchAlgorithm, SearchListenerMediator searchListenerMediator) {
        this.searchAlgorithm = searchAlgorithm;
        this.searchListenerMediator = searchListenerMediator;
    }

    @Override
    public SearchResult startSearch(final Game game) {
        keepProcessing = true;

        accept(new SetGameVisitor(game));

        searchListenerMediator.triggerBeforeSearch();

        int currentSearchDepth = 1;
        SearchResult searchResult = new SearchResult();
        SearchResultByDepth searchResultByDepth = null;
        do {
            searchListenerMediator.accept(new SetSearchMaxPlyVisitor(currentSearchDepth));

            searchListenerMediator.triggerBeforeSearchByDepth();

            searchAlgorithm.search();

            searchResultByDepth = new SearchResultByDepth(currentSearchDepth);

            searchListenerMediator.accept(new CollectSearchResultByDepthVisitor(searchResultByDepth));

            searchListenerMediator.triggerAfterSearchByDepth(searchResultByDepth);

            searchResult.addSearchResultByDepth(searchResultByDepth);

            if (searchResultByDepthListener != null) {
                searchResultByDepthListener.accept(searchResultByDepth);
            }

            currentSearchDepth++;

        } while (keepProcessing &&
                currentSearchDepth <= maxDepth &&
                searchResultByDepth.isContinueDeepening() &&
                searchPredicateParameter.test(searchResultByDepth)
        );

        searchListenerMediator.accept(new CollectSearchResultVisitor(searchResult));

        searchListenerMediator.triggerAfterSearch(searchResult);

        return searchResult;
    }


    /**
     * No podemos detener si al menos no se buscó con DEPTH = 1
     * TODO: LA LOGICA DEBERIA MOVERSE A SEARCH MANAGER
     *
     */
    @Override
    public void stopSearch() {
        keepProcessing = false;
        searchListenerMediator.triggerStopSearching();
    }

    @Override
    public void reset() {
        searchListenerMediator.triggerReset();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
