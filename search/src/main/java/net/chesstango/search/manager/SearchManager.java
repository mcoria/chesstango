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
    private SearchListener listenerManager = new SearchListener() {
        @Override
        public void searchStarted() {
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
            if (listenerClient != null) {
                listenerClient.searchStarted();
            }
        }

        @Override
        public void searchStopped() {
            if (listenerClient != null) {
                listenerClient.searchStopped();
            }
        }

        @Override
        public void searchFinished(SearchMoveResult searchResult) {
            if (listenerClient != null) {
                listenerClient.searchFinished(searchResult);
            }
        }
    };

    private volatile Future<SearchMoveResult> searchTask;
    private volatile Integer timeOut;

    public void searchInfinite(Game game) {
        this.timeOut = null;
        searchTask = executorService.submit(() -> {
            SearchMoveResult searchResult = searchMove.search(game, Integer.MAX_VALUE);
            return searchResult;
        });
    }

    public void searchUpToDepth(Game game, int depth) {
        this.timeOut = null;
        searchTask = executorService.submit(() -> {
            SearchMoveResult searchResult = searchMove.search(game, depth);
            return searchResult;
        });
    }

    public void searchUpToTime(Game game, int timeOut) {
        this.timeOut = timeOut;
        searchTask = executorService.submit(() -> {
            SearchMoveResult searchResult = searchMove.search(game, Integer.MAX_VALUE);
            return searchResult;
        });
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
        this.searchMove.setSearchListener(listenerManager);
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
}
