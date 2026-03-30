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
    private final SearchByTree searchByTree;
    private final SearchByAggregator searchByAggregator;
    private final int infiniteDepth;

    SearchManagerReady(SearchManager searchManager,
                       SearchInvoker searchInvoker,
                       SearchByTree searchByTree,
                       SearchByAggregator searchByAggregator,
                       int infiniteDepth) {
        this.searchManager = searchManager;
        this.searchInvoker = searchInvoker;
        this.searchByTree = searchByTree;
        this.searchByAggregator = searchByAggregator;
        this.infiniteDepth = infiniteDepth;
    }

    @Override
    public Future<SearchResponse> searchDepthImp(Game game,
                                                 int depth,
                                                 Predicate<SearchResultByDepth> searchResultByDepthPredicate,
                                                 SearchListener searchListener) {

        log.debug("Searching by depth {}", depth);

        SearchManagerSearchingByDepth searchManagerSearchingByDepth = searchManager.createSearchingByDepthState(searchListener);

        searchManager.setCurrentSearchManagerState(searchManagerSearchingByDepth);

        return searchInvoker.searchImp(game, depth, searchResultByDepthPredicate, searchManagerSearchingByDepth);
    }

    @Override
    public Future<SearchResponse> searchTimeOutImp(Game game,
                                                   int timeOut,
                                                   Predicate<SearchResultByDepth> searchResultByDepthPredicate,
                                                   SearchListener searchListener) {

        log.debug("Searching by time {} ms", timeOut);

        SearchManagerSearchingByTime searchManagerSearchingByTime = searchManager.createSearchingByTimeState(timeOut, searchListener);

        searchManager.setCurrentSearchManagerState(searchManagerSearchingByTime);

        return searchInvoker.searchImp(game, infiniteDepth, searchResultByDepthPredicate, searchManagerSearchingByTime);
    }

    @Override
    public void stopSearchingImp() {
        log.warn("No search in progress");
    }

    @Override
    public Session newSessionImp() {
        searchByTree.reset();
        return new Session(searchManager);
    }

    @Override
    public void setPolyglotFile(String polyglotFile) {
        searchByAggregator.withPolyglotFile(polyglotFile);
    }

    @Override
    public void setSyzygyPath(String syzygyPath) {
        searchByAggregator.withSyzygyPath(syzygyPath);
    }
}
