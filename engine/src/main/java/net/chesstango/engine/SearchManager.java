package net.chesstango.engine;

import lombok.extern.slf4j.Slf4j;
import net.chesstango.board.Game;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author Mauricio Coria
 */
@Slf4j
class SearchManager implements TangoOptions {
    private final int infiniteDepth;
    private final SearchByTree searchByTree;
    private final TangoOptions tangoOptions;
    private final TimeMgmt timeMgmt;
    private final SearchInvoker searchInvoker;
    private final ScheduledExecutorService timeOutExecutor;

    private volatile SearchManagerState currentSearchManagerState;

    SearchManager(int infiniteDepth,
                  SearchByTree searchByTree,
                  TangoOptions tangoOptions,
                  TimeMgmt timeMgmt,
                  SearchInvoker searchInvoker,
                  ScheduledExecutorService timeOutExecutor) {
        this.infiniteDepth = infiniteDepth;
        this.searchByTree = searchByTree;
        this.tangoOptions = tangoOptions;
        this.timeMgmt = timeMgmt;
        this.searchInvoker = searchInvoker;
        this.timeOutExecutor = timeOutExecutor;
        setCurrentSearchManagerState(createReadyState());
    }

    synchronized Future<SearchResponse> searchInfinite(Game game, SearchListener searchListener) {
        return currentSearchManagerState.searchDepthImp(game, infiniteDepth, _ -> true, searchListener);
    }

    synchronized Future<SearchResponse> searchDepth(Game game, int depth, SearchListener searchListener) {
        return currentSearchManagerState.searchDepthImp(game, depth, _ -> true, searchListener);
    }

    synchronized Future<SearchResponse> searchTime(Game game, int timeOut, SearchListener searchListener) {
        return currentSearchManagerState.searchTimeOutImp(game, timeOut, _ -> true, searchListener);
    }

    synchronized Future<SearchResponse> searchFast(Game game, int wTime, int wInc, int bTime, int bInc, SearchListener searchListener) {
        int timeOut = timeMgmt.getTimeOut(game, wTime, bTime, wInc, bInc);
        return currentSearchManagerState.searchTimeOutImp(game, timeMgmt.getTimeOut(game, wTime, bTime, wInc, bInc), searchInfo -> timeMgmt.keepSearching(timeOut, searchInfo), searchListener);
    }

    synchronized void stopSearching() {
        currentSearchManagerState.stopSearchingImp();
    }

    synchronized Session newSession() {
        return currentSearchManagerState.newSessionImp();
    }

    @Override
    public void setPolyglotFile(String polyglotFile) {
        currentSearchManagerState.setPolyglotFile(polyglotFile);
    }

    @Override
    public void setSyzygyPath(String syzygyPath) {
        currentSearchManagerState.setSyzygyPath(syzygyPath);
    }

    @Override
    public void setHashSize(int hashSizeMB) {
        currentSearchManagerState.setHashSize(hashSizeMB);
    }

    synchronized void setCurrentSearchManagerState(SearchManagerState currentSearchManagerState) {
        log.trace("Changing state from {} to {}", this.currentSearchManagerState != null ? this.currentSearchManagerState.getClass().getSimpleName() : "-", currentSearchManagerState.getClass().getSimpleName());
        this.currentSearchManagerState = currentSearchManagerState;
    }

    SearchManagerReady createReadyState() {
        return new SearchManagerReady(this, searchInvoker, searchByTree, tangoOptions, infiniteDepth);
    }

    SearchManagerSearchingByTime createSearchingByTimeState(int timeOut, SearchListener searchListener) {
        return new SearchManagerSearchingByTime(this, searchByTree::stopSearching, timeOutExecutor, searchListener, timeOut);
    }

    SearchManagerSearchingByDepth createSearchingByDepthState(SearchListener searchListener) {
        return new SearchManagerSearchingByDepth(this, searchByTree::stopSearching, searchListener);
    }
}