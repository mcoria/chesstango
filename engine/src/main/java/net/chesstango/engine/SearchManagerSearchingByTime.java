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
class SearchManagerSearchingByTime implements SearchManagerState, SearchListener {
    private final SearchManager searchManager;

    private final SearchByChain searchByChain;

    private final ScheduledExecutorService timeOutExecutor;

    private final CountDownLatch countDownLatch;

    private final SearchListener searchListener;

    private final int timeOut;

    private ScheduledFuture<?> stopTask;


    SearchManagerSearchingByTime(SearchManager searchManager,
                                 SearchByChain searchByChain,
                                 ScheduledExecutorService timeOutExecutor,
                                 SearchListener searchListener,
                                 int timeOut) {
        this.searchManager = searchManager;
        this.searchByChain = searchByChain;
        this.timeOutExecutor = timeOutExecutor;
        this.searchListener = searchListener;
        this.timeOut = timeOut;
        this.countDownLatch = new CountDownLatch(1);
    }


    @Override
    public Future<SearchResponse> searchTimeOutImp(Game game, int timeOut, Predicate<SearchResultByDepth> searchPredicate, SearchListener searchListener) {
        log.warn("Search is in progress");
        return null;
    }

    @Override
    public Future<SearchResponse> searchDepthImp(Game game, int depth, Predicate<SearchResultByDepth> searchPredicate, SearchListener searchListener) {
        log.warn("Search is in progress");
        return null;
    }

    @Override
    public void stopSearchingImp() {
        log.debug("Stopping search");
        try {
            // Espera que al menos se complete un ciclo
            // Aca se puede dar la interrupcion
            countDownLatch.await();

            searchByChain.stopSearching();
        } catch (InterruptedException e) {
            // Si ocurre la excepcion quiere decir que terminó normalmente y el thread fué interrumpido, por lo tanto no es necesario triggerStopSearching()
            log.warn("Stopping interrupted");
        }
    }

    @Override
    public void searchStarted() {
        stopTask = timeOutExecutor.schedule(this::stopSearchingImp, timeOut, TimeUnit.MILLISECONDS);
        searchListener.searchStarted();
    }

    @Override
    public void searchInfo(String searchInfo) {
        countDownLatch.countDown();
        searchListener.searchInfo(searchInfo);
    }

    @Override
    public void searchFinished(SearchResponse searchResult) {
        // Esta linea garantiza que se cancele stopTask inmediatamente termina la búsqueda
        if (!stopTask.isDone()) {
            stopTask.cancel(false);
        }
        searchManager.setCurrentSearchManagerState(searchManager.createReadyState());
        searchListener.searchFinished(searchResult);
    }
}
