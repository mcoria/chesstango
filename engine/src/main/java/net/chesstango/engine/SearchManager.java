package net.chesstango.engine;

import net.chesstango.board.Game;
import net.chesstango.engine.timemgmt.TimeMgmt;
import net.chesstango.search.SearchResult;
import net.chesstango.search.SearchResultByDepth;
import net.chesstango.search.SearchResultByDepthListener;

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

    private volatile CountDownLatch countDownLatch;
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

    public synchronized void stopSearching() {
        try {
            // Espera que al menos se complete un ciclo
            // Aca se puede dar la interrupcion
            countDownLatch.await();

            searchChain.stopSearching();
        } catch (InterruptedException e) {
            // Si ocurre la excepcion quiere decir que terminó normalmente y el thread fué interrumpido, por lo tanto no es necesario triggerStopSearching()
        }
    }

    public synchronized void reset() {
        searchChain.reset();
    }

    @Override
    public synchronized void close() throws Exception {
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

        countDownLatch = new CountDownLatch(1);

        currentSearchTask = searchExecutor.submit(() -> {
            try {
                searchListener.searchStarted();

                ScheduledFuture<?> stopTask = null;
                if (timeOut != 0) {
                    stopTask = timeOutExecutor.schedule(this::stopSearching, timeOut, TimeUnit.MILLISECONDS);
                }

                SearchResultByDepthListener searchResultByDepthListener = searchInfo -> {
                    countDownLatch.countDown();
                    searchListener.searchInfo(searchInfo);
                };

                SearchContext context = new SearchContext()
                        .setGame(game)
                        .setDepth(depth)
                        .setSearchPredicate(searchPredicate)
                        .setSearchResultByDepthListener(searchResultByDepthListener);

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