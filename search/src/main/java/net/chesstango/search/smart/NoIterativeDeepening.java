package net.chesstango.search.smart;

import lombok.Getter;
import net.chesstango.board.Game;
import net.chesstango.gardel.epd.EPD;
import net.chesstango.search.*;

import java.util.HashMap;
import java.util.Map;

import static net.chesstango.search.SearchParameter.EPD_PARAMS;
import static net.chesstango.search.SearchParameter.MAX_DEPTH;

/**
 * @author Mauricio Coria
 */
public class NoIterativeDeepening implements Search {

    @Getter
    private final SearchAlgorithm searchAlgorithm;

    @Getter
    private final SearchListenerMediator searchListenerMediator;

    private final Map<SearchParameter, Object> searchParameters = new HashMap<>();

    private int maxDepth = Integer.MAX_VALUE;

    public NoIterativeDeepening(SearchAlgorithm searchAlgorithm, SearchListenerMediator searchListenerMediator) {
        this.searchAlgorithm = searchAlgorithm;
        this.searchListenerMediator = searchListenerMediator;
    }

    @Override
    public SearchResult search(Game game) {
        SearchResult searchResult = new SearchResult();

        SearchByCycleContext searchByCycleContext = new SearchByCycleContext(game);

        searchByCycleContext.setSearchParameters(searchParameters);

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
    public void stopSearching() {
        this.searchListenerMediator.triggerStopSearching();
    }

    @Override
    public void reset() {
        this.searchListenerMediator.triggerReset();
    }

    @Override
    public void setSearchParameter(SearchParameter parameter, Object value) {
        if (MAX_DEPTH.equals(parameter) && value instanceof Integer maxDepthParam) {
            maxDepth = maxDepthParam;
            searchParameters.put(MAX_DEPTH, maxDepthParam);
        }

        if (EPD_PARAMS.equals(parameter) && value instanceof EPD epd) {
            searchParameters.put(EPD_PARAMS, epd);
        }
    }

    @Override
    public void setProgressListener(ProgressListener progressListener) {

    }

}
