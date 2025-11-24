package net.chesstango.engine;

import lombok.extern.slf4j.Slf4j;
import net.chesstango.board.Game;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author Mauricio Coria
 */
@Slf4j
class SearchManager implements AutoCloseable {
    private final int infiniteDepth;
    private final SearchChain searchChain;
    private final TimeMgmt timeMgmt;
    private final SearchInvoker searchInvoker;
    private final ScheduledExecutorService timeOutExecutor;

    private volatile SearchManagerState currentSearchManagerState;

    SearchManager(int infiniteDepth,
                  SearchChain searchChain,
                  TimeMgmt timeMgmt,
                  SearchInvoker searchInvoker,
                  ScheduledExecutorService timeOutExecutor) {
        this.infiniteDepth = infiniteDepth;
        this.searchChain = searchChain;
        this.timeMgmt = timeMgmt;
        this.searchInvoker = searchInvoker;
        this.timeOutExecutor = timeOutExecutor;
        setSearchManagerReady();
    }

    synchronized Future<SearchResponse> searchInfinite(Game game, SearchListener searchListener) {
        return currentSearchManagerState.searchDepthImp(game, infiniteDepth, searchMoveResult -> true, searchListener);
    }

    synchronized Future<SearchResponse> searchDepth(Game game, int depth, SearchListener searchListener) {
        return currentSearchManagerState.searchDepthImp(game, depth, searchMoveResult -> true, searchListener);
    }

    synchronized Future<SearchResponse> searchTime(Game game, int timeOut, SearchListener searchListener) {
        return currentSearchManagerState.searchTimeOutImp(game, timeOut, searchMoveResult -> true, searchListener);
    }

    synchronized Future<SearchResponse> searchFast(Game game, int wTime, int bTime, int wInc, int bInc, SearchListener searchListener) {
        int timeOut = timeMgmt.getTimeOut(game, wTime, bTime, wInc, bInc);
        return currentSearchManagerState.searchTimeOutImp(game, timeMgmt.getTimeOut(game, wTime, bTime, wInc, bInc), searchInfo -> timeMgmt.keepSearching(timeOut, searchInfo), searchListener);
    }

    synchronized void stopSearching() {
        currentSearchManagerState.stopSearchingImp();
    }

    synchronized void reset() {
        searchChain.reset();
    }

    @Override
    public synchronized void close() throws Exception {
        searchChain.close();
    }

    void setSearchManagerReady() {
        this.currentSearchManagerState = new SearchManagerReady(this, searchInvoker, infiniteDepth);
    }

    SearchManagerSearchingByTime setSearchManagerSearchingByTime(int timeOut, SearchListener searchListener) {
        SearchManagerSearchingByTime searchManagerSearchingByTime = new SearchManagerSearchingByTime(this, searchChain, timeOutExecutor, searchListener, timeOut);
        this.currentSearchManagerState = searchManagerSearchingByTime;
        return searchManagerSearchingByTime;
    }

    SearchManagerSearchingByDepth setSearchManagerSearchingByDepth(SearchListener searchListener) {
        SearchManagerSearchingByDepth searchManagerSearchingByDepth = new SearchManagerSearchingByDepth(this, searchChain, searchListener);
        this.currentSearchManagerState = searchManagerSearchingByDepth;
        return searchManagerSearchingByDepth;
    }
}