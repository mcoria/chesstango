package net.chesstango.engine;

import lombok.extern.slf4j.Slf4j;
import net.chesstango.board.Game;
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

    public SearchManager(int infiniteDepth,
                         SearchChain searchChain,
                         TimeMgmt timeMgmt,
                         SearchInvoker searchInvoker,
                         ScheduledExecutorService timeOutExecutor) {
        this.infiniteDepth = infiniteDepth;
        this.searchChain = searchChain;
        this.timeMgmt = timeMgmt;
        this.searchInvoker = searchInvoker;
        this.timeOutExecutor = timeOutExecutor;
    }

    public synchronized Future<SearchResponse> searchInfinite(Game game, SearchListener searchListener) {
        return searchDepthImp(game, infiniteDepth, searchMoveResult -> true, searchListener);
    }

    public synchronized Future<SearchResponse> searchDepth(Game game, int depth, SearchListener searchListener) {
        return searchDepthImp(game, depth, searchMoveResult -> true, searchListener);
    }

    public synchronized Future<SearchResponse> searchTime(Game game, int timeOut, SearchListener searchListener) {
        return searchTimeOutImp(game, timeOut, searchMoveResult -> true, searchListener);
    }

    public synchronized Future<SearchResponse> searchFast(Game game, int wTime, int bTime, int wInc, int bInc, SearchListener searchListener) {
        int timeOut = timeMgmt.getTimeOut(game, wTime, bTime, wInc, bInc);
        return searchTimeOutImp(game, timeMgmt.getTimeOut(game, wTime, bTime, wInc, bInc), searchInfo -> timeMgmt.keepSearching(timeOut, searchInfo), searchListener);
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

    private Future<SearchResponse> searchTimeOutImp(Game game, int timeOut, Predicate<SearchResultByDepth> searchPredicate, SearchListener searchListener) {
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
            public void searchFinished(SearchResponse searchResult) {
                // Esta linea garantiza que se cancele stopTask inmediatamente termina la búsqueda
                stopTask.cancel(false);
                searchListener.searchFinished(searchResult);
            }
        };

        log.debug("Searching by time {} ms", timeOut);
        return searchInvoker.searchImp(game, infiniteDepth, searchPredicate, searchListenerDecorator);
    }

    private Future<SearchResponse> searchDepthImp(Game game, int depth, Predicate<SearchResultByDepth> searchPredicate, SearchListener searchListener) {
        countDownLatch = new CountDownLatch(1);

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
            public void searchFinished(SearchResponse searchResult) {
                searchListener.searchFinished(searchResult);
            }
        };

        log.debug("Searching by depth {}", depth);
        return searchInvoker.searchImp(game, depth, searchPredicate, searchListenerDecorator);
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