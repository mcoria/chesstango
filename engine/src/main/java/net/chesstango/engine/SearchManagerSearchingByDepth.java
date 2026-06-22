package net.chesstango.engine;

import lombok.extern.slf4j.Slf4j;
import net.chesstango.board.Game;
import net.chesstango.search.SearchResultByDepth;

import java.nio.file.Path;
import java.util.concurrent.*;
import java.util.function.Predicate;

/**
 * @author Mauricio Coria
 */
@Slf4j
class SearchManagerSearchingByDepth implements SearchManagerState, SearchListener {
    private final SearchManager searchManager;

    private final Runnable stopFn;

    private final CountDownLatch countDownLatch;

    private final SearchListener searchListener;

    SearchManagerSearchingByDepth(SearchManager searchManager, Runnable stopFn, SearchListener searchListener) {
        this.searchManager = searchManager;
        this.stopFn = stopFn;
        this.searchListener = searchListener;
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

            stopFn.run();
        } catch (InterruptedException e) {
            // Si ocurre la excepcion quiere decir que terminó normalmente y el thread fué interrumpido, por lo tanto no es necesario triggerStopSearching()
            log.warn("Stopping interrupted");
        }
    }

    @Override
    public Session newSessionImp() {
        log.warn("Search is in progress");
        return null;
    }

    @Override
    public void searchStarted() {
        searchListener.searchStarted();
    }

    @Override
    public void searchInfo(String searchInfo) {
        countDownLatch.countDown();
        searchListener.searchInfo(searchInfo);
    }

    @Override
    public void searchFinished(SearchResponse searchResult) {
        searchManager.setCurrentSearchManagerState(searchManager.createReadyState());
        searchListener.searchFinished(searchResult);
    }

    @Override
    public void setPolyglotFile(Path polyglotFile) {
        log.warn("Search is in progress");
    }

    @Override
    public void setSyzygyPath(Path syzygyPath) {
        log.warn("Search is in progress");
    }

    @Override
    public void setHashSize(int hashSizeMB) {
        log.warn("Search is in progress");
    }
}
