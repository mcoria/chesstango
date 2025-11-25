package net.chesstango.engine;

import lombok.extern.slf4j.Slf4j;
import net.chesstango.board.Game;
import net.chesstango.search.SearchResultByDepth;

import java.util.concurrent.Future;
import java.util.function.Predicate;

/**
 * @author Mauricio Coria
 */
@Slf4j
class SearchManagerReady implements SearchManagerState {
    private final SearchManager searchManager;
    private final SearchInvoker searchInvoker;
    private final int infiniteDepth;


    SearchManagerReady(SearchManager searchManager,
                       SearchInvoker searchInvoker,
                       int infiniteDepth) {
        this.searchManager = searchManager;
        this.searchInvoker = searchInvoker;
        this.infiniteDepth = infiniteDepth;
    }


    @Override
    public Future<SearchResponse> searchTimeOutImp(Game game, int timeOut, Predicate<SearchResultByDepth> searchPredicate, SearchListener searchListener) {
        log.debug("Searching by time {} ms", timeOut);

        SearchManagerSearchingByTime searchManagerSearchingByTime = searchManager.createSearchingByTimeState(timeOut, searchListener);

        searchManager.setCurrentSearchManagerState(searchManagerSearchingByTime);

        return searchInvoker.searchImp(game, infiniteDepth, searchPredicate, searchManagerSearchingByTime);
    }

    @Override
    public Future<SearchResponse> searchDepthImp(Game game, int depth, Predicate<SearchResultByDepth> searchPredicate, SearchListener searchListener) {
        log.debug("Searching by depth {}", depth);

        SearchManagerSearchingByDepth searchManagerSearchingByDepth = searchManager.createSearchingByDepthState(searchListener);

        searchManager.setCurrentSearchManagerState(searchManagerSearchingByDepth);

        return searchInvoker.searchImp(game, depth, searchPredicate, searchManagerSearchingByDepth);
    }

    @Override
    public void stopSearchingImp() {
        log.warn("No search in progress");
    }
}
