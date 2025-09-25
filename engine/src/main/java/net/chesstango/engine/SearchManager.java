package net.chesstango.engine;

import lombok.extern.slf4j.Slf4j;
import net.chesstango.board.Game;
import net.chesstango.engine.timemgmt.TimeMgmt;
import net.chesstango.search.SearchResult;
import net.chesstango.search.SearchResultByDepth;

import java.util.concurrent.*;
import java.util.function.Predicate;

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

    private volatile CountDownLatch countDownLatch;

    public SearchManager(int infiniteDepth, SearchChain searchChain, TimeMgmt timeMgmt, SearchInvoker searchInvoker, ScheduledExecutorService timeOutExecutor) {
        this.infiniteDepth = infiniteDepth;
        this.searchChain = searchChain;
        this.timeMgmt = timeMgmt;
        this.searchInvoker = searchInvoker;
        this.timeOutExecutor = timeOutExecutor;
    }

    public Future<SearchResult> searchInfinite(Game game, SearchListener searchListener) {
        return searchDepthImp(game, infiniteDepth, searchMoveResult -> true, searchListener);
    }

    public Future<SearchResult> searchDepth(Game game, int depth, SearchListener searchListener) {
        return searchDepthImp(game, depth, searchMoveResult -> true, searchListener);
    }

    public Future<SearchResult> searchTime(Game game, int timeOut, SearchListener searchListener) {
        return searchTimeOutImp(game, timeOut, searchMoveResult -> true, searchListener);
    }

    public Future<SearchResult> searchFast(Game game, int wTime, int bTime, int wInc, int bInc, SearchListener searchListener) {
        int timeOut = timeMgmt.getTimeOut(game, wTime, bTime, wInc, bInc);
        return searchTimeOutImp(game, timeOut, searchInfo -> timeMgmt.keepSearching(timeOut, searchInfo), searchListener);
    }

    public synchronized void stopSearching() {
        stopSearchingImp();
    }

    public synchronized void reset() {
        searchChain.reset();
    }

    @Override
    public synchronized void close() throws Exception {
        searchChain.close();
    }

    private synchronized Future<SearchResult> searchTimeOutImp(Game game, int timeOut, Predicate<SearchResultByDepth> searchPredicate, SearchListener searchListener) {
        countDownLatch = new CountDownLatch(1);

        ScheduledFuture<?> stopTask = timeOutExecutor.schedule(this::stopSearchingImp, timeOut, TimeUnit.MILLISECONDS);

        SearchListener searchListenerDecorator = new SearchListener() {
            @Override
            public void searchStarted() {
                searchListener.searchStarted();
            }

            @Override
            public void searchInfo(SearchResultByDepth searchResultByDepth) {
                countDownLatch.countDown();
                searchListener.searchInfo(searchResultByDepth);
            }

            @Override
            public void searchFinished(SearchResult searchResult) {
                // Esta lnea garantiza que se cancele stopTask inmediatamente termina la búsqueda
                stopTask.cancel(false);
                searchListener.searchFinished(searchResult);
            }
        };

        log.debug("Searching by time {} ms", timeOut);
        return searchInvoker.searchImp(game, infiniteDepth, searchPredicate, searchListenerDecorator);
    }

    private synchronized Future<SearchResult> searchDepthImp(Game game, int depth, Predicate<SearchResultByDepth> searchPredicate, SearchListener searchListener) {
        log.debug("Searching by depth {}", depth);
        return searchInvoker.searchImp(game, depth, searchPredicate, searchListener);
    }

    private void stopSearchingImp() {
        log.debug("Stopping search");
        try {
            // Espera que al menos se complete un ciclo
            // Aca se puede dar la interrupcion
            countDownLatch.await();

            searchChain.stopSearching();
        } catch (InterruptedException e) {
            // Si ocurre la excepcion quiere decir que terminó normalmente y el thread fué interrumpido, por lo tanto no es necesario triggerStopSearching()
            log.error("Stopping interrupted");
        }
    }
}