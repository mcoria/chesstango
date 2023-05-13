package net.chesstango.search.manager;


import net.chesstango.board.Game;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Mauricio Coria
 */
public class SearchManager {

    private ExecutorService executorService;
    private SearchMove searchMove;
    private SearchListener listener;

    public void searchInfinite(Game game) {
        executorService.submit(() -> {
            listener.searchStarted();
            SearchMoveResult searchResult = searchMove.search(game, Integer.MAX_VALUE);
            listener.searchFinished(searchResult);
        });
    }

    public void searchUpToDepth(Game game, int depth) {
        executorService.submit(() -> {
            listener.searchStarted();
            SearchMoveResult searchResult = searchMove.search(game, depth);
            listener.searchFinished(searchResult);
        });
    }

    public void searchUpToTime(Game game, int timeOut) {
        executorService.submit(() -> {
            listener.searchStarted();
            SearchMoveResult searchResult = searchMove.search(game, Integer.MAX_VALUE);
            listener.searchFinished(searchResult);
        });
        executorService.submit(() -> {

        });
    }

    public void reset() {
        searchMove.reset();
    }

    public void stopSearching() {
        searchMove.stopSearching();
    }

    public SearchManager setSearchListener(SearchListener listener){
        this.listener = listener;
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
}
