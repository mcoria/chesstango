package net.chesstango.search.manager;


import net.chesstango.board.Game;
import net.chesstango.search.SearchListener;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author Mauricio Coria
 */
public class SearchManager {
    private ExecutorService executorService;
    private SearchMove searchMove;
    private SearchListener listenerClient;

    private volatile Future<?> searchTask;

    public void searchInfinite(Game game) {
        searchImp(game, Integer.MAX_VALUE, null);
    }

    public void searchUpToDepth(Game game, int depth) {
        searchImp(game, depth, null);
    }

    public void searchUpToTime(Game game, int timeOut) {
        searchImp(game, Integer.MAX_VALUE, timeOut);
    }

    public void reset() {
        searchMove.reset();
    }

    public void stopSearching() {
        if (!searchTask.isDone()) {
            searchMove.stopSearching();
        }
    }

    public SearchManager setSearchListener(SearchListener listener) {
        this.listenerClient = listener;
        return this;
    }

    public SearchManager setSearchMove(SearchMove searchMove) {
        this.searchMove = searchMove;
        return this;
    }

    public void open() {
        executorService = Executors.newFixedThreadPool(2);
    }

    public void close() {
        executorService.shutdown();
        try {
            executorService.awaitTermination(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    private void searchImp(Game game, int depth, Integer timeOut) {
        searchTask = executorService.submit(() -> {

            if (listenerClient != null) {
                listenerClient.searchStarted();
            }

            if (timeOut != null) {
                executorService.submit(() -> {
                    try {
                        Thread.sleep(timeOut);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    stopSearching();
                });
            }

            SearchMoveResult searchResult = searchMove.search(game, depth);

            if (listenerClient != null) {
                listenerClient.searchFinished(searchResult);
            }
        });
    }
}
