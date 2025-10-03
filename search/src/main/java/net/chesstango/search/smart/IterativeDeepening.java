package net.chesstango.search.smart;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.gardel.epd.EPD;
import net.chesstango.search.*;
import net.chesstango.search.Acceptor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static net.chesstango.search.SearchParameter.*;

/**
 * @author Mauricio Coria
 */
public class IterativeDeepening implements Search {
    private volatile boolean keepProcessing;

    @Getter
    private final SearchAlgorithm searchAlgorithm;

    @Getter
    private final SearchListenerMediator searchListenerMediator;

    private final Map<SearchParameter, Object> searchParameters = new HashMap<>();

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

        SearchByCycleContext searchByCycleContext = new SearchByCycleContext(game);
        searchByCycleContext.setSearchParameters(searchParameters);

        searchListenerMediator.triggerBeforeSearch(searchByCycleContext);

        int currentSearchDepth = 1;
        SearchResult searchResult = new SearchResult();
        SearchResultByDepth searchResultByDepth = null;
        do {
            SearchByDepthContext context = new SearchByDepthContext(currentSearchDepth);

            searchListenerMediator.triggerBeforeSearchByDepth(context);

            searchAlgorithm.search();

            searchResultByDepth = new SearchResultByDepth(currentSearchDepth);

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
    public void setSearchParameter(SearchParameter parameter, Object value) {
        if (EPD_PARAMS.equals(parameter) && value instanceof EPD epd) {
            this.searchParameters.put(EPD_PARAMS, epd);
        }
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
