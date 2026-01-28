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
    private final SearchByChain searchByChain;
    private final TimeMgmt timeMgmt;
    private final SearchInvoker searchInvoker;
    private final ScheduledExecutorService timeOutExecutor;

    private volatile SearchManagerState currentSearchManagerState;

    SearchManager(int infiniteDepth,
                  SearchByChain searchByChain,
                  TimeMgmt timeMgmt,
                  SearchInvoker searchInvoker,
                  ScheduledExecutorService timeOutExecutor) {
        this.infiniteDepth = infiniteDepth;
        this.searchByChain = searchByChain;
        this.timeMgmt = timeMgmt;
        this.searchInvoker = searchInvoker;
        this.timeOutExecutor = timeOutExecutor;
        setCurrentSearchManagerState(createReadyState());
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
        searchByChain.reset();
    }

    synchronized void setCurrentSearchManagerState(SearchManagerState currentSearchManagerState) {
        log.trace("Changing state from {} to {}", this.currentSearchManagerState.getClass().getSimpleName(), currentSearchManagerState.getClass().getSimpleName());
        this.currentSearchManagerState = currentSearchManagerState;
    }

    @Override
    public synchronized void close() throws Exception {
        searchByChain.close();
    }

    SearchManagerReady createReadyState() {
        return new SearchManagerReady(this, searchInvoker, infiniteDepth);
    }

    SearchManagerSearchingByTime createSearchingByTimeState(int timeOut, SearchListener searchListener) {
        return new SearchManagerSearchingByTime(this, searchByChain, timeOutExecutor, searchListener, timeOut);
    }

    SearchManagerSearchingByDepth createSearchingByDepthState(SearchListener searchListener) {
        return new SearchManagerSearchingByDepth(this, searchByChain, searchListener);
    }
}