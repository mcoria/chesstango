package net.chesstango.engine;

import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.engine.timemgmt.FivePercentage;
import net.chesstango.engine.timemgmt.TimeMgmt;
import net.chesstango.search.SearchResult;
import net.chesstango.search.SearchResultByDepth;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

/**
 * @author Mauricio Coria
 */
class SearchManager implements AutoCloseable {
    private final SearchChain searchChain;
    private final TimeMgmt timeMgmt;

    @Setter
    private int infiniteDepth;

    private volatile Future<SearchResult> currentSearchTask;

    private static final AtomicInteger executorCounter = new AtomicInteger(0);

    private static ExecutorService searchExecutor;
    private static ScheduledExecutorService timeOutExecutor;

    public SearchManager(SearchChain searchChain) {
        this.searchChain = searchChain;

        this.timeMgmt = new FivePercentage();
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
        final int timeOut = timeMgmt.getTimeOut(game, wTime, bTime, wInc, bInc);
        return searchImp(game, infiniteDepth, timeOut, searchInfo -> timeMgmt.keepSearching(timeOut, searchInfo), searchListener);
    }

    public void reset() {
        searchChain.reset();
    }

    public void stopSearching() {
        searchChain.stopSearching();
    }

    public void init() {
        int currentValue = executorCounter.incrementAndGet();
        if (currentValue == 1) {
            initExecutors();
        }
    }

    @Override
    public void close() throws Exception {
        int currentValue = executorCounter.decrementAndGet();
        if (currentValue == 0) {
            stopExecutors();
        } else if (currentValue < 0) {
            throw new RuntimeException("Closed too many times");
        }
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


    private synchronized static void initExecutors() {
        timeOutExecutor = Executors.newSingleThreadScheduledExecutor(new SearchManagerThreadFactory("timeout"));
        searchExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new SearchManagerThreadFactory("search"));
    }


    private synchronized static void stopExecutors() {
        searchExecutor.shutdownNow();
        timeOutExecutor.shutdownNow();
    }

    public static class SearchManagerThreadFactory implements ThreadFactory {
        private final AtomicInteger threadCounter = new AtomicInteger(1);
        private String threadNamePrefix = "";

        public SearchManagerThreadFactory(String threadNamePrefix) {
            this.threadNamePrefix = threadNamePrefix;
        }

        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, String.format("%s-%d", threadNamePrefix, threadCounter.getAndIncrement()));
        }
    }
}