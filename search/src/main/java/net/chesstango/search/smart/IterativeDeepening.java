package net.chesstango.search.smart;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.gardel.epd.EPD;
import net.chesstango.search.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.function.Predicate;

import static net.chesstango.search.SearchParameter.*;

/**
 * @author Mauricio Coria
 */
public class IterativeDeepening implements Search {
    private volatile boolean keepProcessing;

    private volatile CountDownLatch countDownLatch;

    @Getter
    private final SearchAlgorithm searchAlgorithm;

    @Getter
    private final SearchListenerMediator searchListenerMediator;

    private final Map<SearchParameter, Object> searchParameters = new HashMap<>();

    private int maxDepth = Integer.MAX_VALUE;

    private SearchResultByDepthListener searchResultByDepthListener;

    private Predicate<SearchResultByDepth> searchPredicateParameter = searchMoveResult -> true;

    public IterativeDeepening(SearchAlgorithm searchAlgorithm, SearchListenerMediator searchListenerMediator) {
        this.searchAlgorithm = searchAlgorithm;
        this.searchListenerMediator = searchListenerMediator;
    }

    @Override
    public SearchResult search(final Game game) {
        keepProcessing = true;
        countDownLatch = new CountDownLatch(1);

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

            countDownLatch.countDown();
            currentSearchDepth++;

        } while (keepProcessing &&
                currentSearchDepth <= maxDepth &&
                searchPredicateParameter.test(searchResultByDepth) &&
                searchResultByDepth.isSearchNextDepth()
        );

        searchListenerMediator.triggerAfterSearch(searchResult);

        return searchResult;
    }


    /**
     * No podemos detener si al menos no se buscó con DEPTH = 1
     */
    @Override
    public void stopSearching() {
        keepProcessing = false;
        try {
            // Espera que al menos se complete un ciclo
            // Aca se puede dar la interrupcion
            countDownLatch.await();

            searchListenerMediator.triggerStopSearching();
        } catch (InterruptedException e) {
            // Si ocurre la excepcion quiere decir que terminó normalmente y el thread fué interrumpido, por lo tanto no es necesario triggerStopSearching()
        }
    }

    @Override
    public void reset() {
        searchListenerMediator.triggerReset();
    }

    @Override
    public void setSearchParameter(SearchParameter parameter, Object value) {
        if (SEARCH_PREDICATE.equals(parameter) && value instanceof Predicate<?> searchPredicateArg) {
            this.searchPredicateParameter = (Predicate<SearchResultByDepth>) searchPredicateArg;
        } else if (MAX_DEPTH.equals(parameter) && value instanceof Integer maxDepthParam) {
            this.maxDepth = maxDepthParam;
        } else if (EPD_PARAMS.equals(parameter) && value instanceof EPD epd) {
            this.searchParameters.put(EPD_PARAMS, epd);
        } else if (SEARCH_BY_DEPTH_LISTENER.equals(parameter) && value instanceof SearchResultByDepthListener searchResultByDepthListenerParam) {
            this.searchResultByDepthListener = searchResultByDepthListenerParam;
        }
    }
}
