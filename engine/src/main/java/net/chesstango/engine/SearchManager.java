package net.chesstango.engine;

import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.engine.timemgmt.FivePercentage;
import net.chesstango.engine.timemgmt.TimeMgmt;
import net.chesstango.search.SearchParameter;
import net.chesstango.search.SearchResult;
import net.chesstango.search.SearchResultByDepth;
import net.chesstango.search.SearchResultByDepthListener;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

/**
 * @author Mauricio Coria
 */
public final class SearchManager {
    private final SearchChain searchChain;
    private final TimeMgmt timeMgmt;

    @Setter
    private int infiniteDepth = 1;

    private volatile Future<?> currentSearchTask;

    private static final AtomicInteger executorCounter = new AtomicInteger(0);
    private static ExecutorService searchExecutor;
    private static ScheduledExecutorService timeOutExecutor;

    public SearchManager(SearchChain searchChain) {
        this.searchChain = searchChain;

        this.timeMgmt = new FivePercentage();
    }

    public void searchInfinite(Game game, SearchListener searchListener) {
        searchImp(game, infiniteDepth, 0, searchListener);
    }

    public void searchDepth(Game game, int depth, SearchListener searchListener) {
        searchImp(game, depth, 0, searchListener);
    }

    public void searchTime(Game game, int timeOut, SearchListener searchListener) {
        searchImp(game, infiniteDepth, timeOut, searchListener);
    }

    public void searchFast(Game game, int wTime, int bTime, int wInc, int bInc, SearchListener searchListener) {
        final int timeOut = timeMgmt.getTimeOut(game, wTime, bTime, wInc, bInc);
        searchImp(game, infiniteDepth, timeOut, searchInfo -> timeMgmt.keepSearching(timeOut, searchInfo), searchListener);
    }

    public void reset() {
        searchChain.reset();
    }

    public void stopSearching() {
        searchChain.stopSearching();
    }

    public void open() {
        searchChain.open();
        int currentValue = executorCounter.incrementAndGet();
        if (currentValue == 1) {
            initExecutors();
        }
    }

    public void close() {
        int currentValue = executorCounter.decrementAndGet();
        if (currentValue == 0) {
            stopExecutors();
        } else if (currentValue < 0) {
            throw new RuntimeException("Closed too many times");
        }
        searchChain.close();
    }

    private void searchImp(Game game, int depth, int timeOut, SearchListener searchListener) {
        searchImp(game, depth, timeOut, searchMoveResult -> true, searchListener);
    }

    private synchronized void searchImp(Game game, int depth, int timeOut, Predicate<SearchResultByDepth> searchPredicate, SearchListener searchListener) {
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

                searchChain.setSearchParameter(SearchParameter.MAX_DEPTH, depth);
                searchChain.setSearchParameter(SearchParameter.SEARCH_PREDICATE, searchPredicate);
                searchChain.setSearchParameter(SearchParameter.SEARCH_BY_DEPTH_LISTENER, (SearchResultByDepthListener) searchListener::searchInfo);

                SearchResult searchResult = searchChain.search(game);

                if (stopTask != null && !stopTask.isDone()) {
                    stopTask.cancel(false);
                }

                searchListener.searchFinished(searchResult);
            } catch (RuntimeException e) {
                e.printStackTrace(System.err);
                throw new RuntimeException(e);
            }
        });
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