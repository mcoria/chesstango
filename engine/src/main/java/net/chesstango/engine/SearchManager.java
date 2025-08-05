package net.chesstango.engine;

import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.engine.timemgmt.TimeMgmt;
import net.chesstango.search.SearchResult;
import net.chesstango.search.SearchResultByDepth;

import java.util.concurrent.*;
import java.util.function.Predicate;

/**
 * @author Mauricio Coria
 */
class SearchManager implements AutoCloseable {
    private final int infiniteDepth;
    private final SearchChain searchChain;
    private final TimeMgmt timeMgmt;
    private final ExecutorService searchExecutor;
    private final ScheduledExecutorService timeOutExecutor;

    private volatile Future<SearchResult> currentSearchTask;


    public SearchManager(int infiniteDepth, SearchChain searchChain, TimeMgmt timeMgmt, ExecutorService searchExecutor, ScheduledExecutorService timeOutExecutor) {
        this.infiniteDepth = infiniteDepth;
        this.searchChain = searchChain;
        this.timeMgmt = timeMgmt;
        this.searchExecutor = searchExecutor;
        this.timeOutExecutor = timeOutExecutor;
    }

    public Future<SearchResult> searchInfinite(Game game, SearchListener searchListener) {
        return searchImp(game, infiniteDepth, 0, searchListener);
    }

    public Future<SearchResult> searchDepth(Game game, int depth, SearchListener searchListener) {
        return searchImp(game, depth, 0, searchListener);
    }

    public Future<SearchResult> searchTime(Game game, int timeOut, SearchListener searchListener) {
        return searchImp(game, infiniteDepth, timeOut, searchListener);
    }

    public Future<SearchResult> searchFast(Game game, int wTime, int bTime, int wInc, int bInc, SearchListener searchListener) {
        int timeOut = timeMgmt.getTimeOut(game, wTime, bTime, wInc, bInc);
        return searchImp(game, infiniteDepth, timeOut, searchInfo -> timeMgmt.keepSearching(timeOut, searchInfo), searchListener);
    }

    public void reset() {
        searchChain.reset();
    }

    public void stopSearching() {
        searchChain.stopSearching();
    }

    @Override
    public void close() throws Exception {
        searchChain.close();
    }

    private Future<SearchResult> searchImp(Game game, int depth, int timeOut, SearchListener searchListener) {
        return searchImp(game, depth, timeOut, searchMoveResult -> true, searchListener);
    }

    private synchronized Future<SearchResult> searchImp(Game game, int depth, int timeOut, Predicate<SearchResultByDepth> searchPredicate, SearchListener searchListener) {
        if (currentSearchTask != null && !currentSearchTask.isDone()) {
            try {
                currentSearchTask.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace(System.err);
                throw new RuntimeException(e);
            }
        }

        currentSearchTask = searchExecutor.submit(() -> {
            try {
                searchListener.searchStarted();

                ScheduledFuture<?> stopTask = null;
                if (timeOut != 0) {
                    stopTask = timeOutExecutor.schedule(this::stopSearching, timeOut, TimeUnit.MILLISECONDS);
                }

                SearchContext context = new SearchContext()
                        .setGame(game)
                        .setDepth(depth)
                        .setSearchPredicate(searchPredicate)
                        .setSearchResultByDepthListener(searchListener::searchInfo);

                SearchResult searchResult = searchChain.search(context);

                if (stopTask != null && !stopTask.isDone()) {
                    stopTask.cancel(false);
                }

                searchListener.searchFinished(searchResult);

                return searchResult;
            } catch (RuntimeException e) {
                e.printStackTrace(System.err);
                throw new RuntimeException(e);
            }
        });

        return currentSearchTask;
    }
}