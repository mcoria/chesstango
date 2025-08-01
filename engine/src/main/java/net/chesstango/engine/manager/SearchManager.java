package net.chesstango.engine.manager;

import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.engine.SearchListener;
import net.chesstango.engine.timemgmt.FivePercentage;
import net.chesstango.engine.timemgmt.TimeMgmt;
import net.chesstango.search.Search;
import net.chesstango.search.SearchParameter;
import net.chesstango.search.SearchResult;
import net.chesstango.search.SearchResultByDepth;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

/**
 * @author Mauricio Coria
 */
public final class SearchManager {
    private final SearchListener searchListener;
    private final SearchManagerChain searchManagerChain;
    private final SearchManagerByOpenBook searchManagerByOpenBook;
    private final SearchManagerByAlgorithm searchManagerByAlgorithm;
    private final TimeMgmt timeMgmt;

    @Setter
    private int infiniteDepth = 1;

    private volatile Future<?> currentSearchTask;

    private static final AtomicInteger executorCounter = new AtomicInteger(0);
    private static ExecutorService searchExecutor;
    private static ScheduledExecutorService timeOutExecutor;

    public SearchManager(Search search, SearchListener searchListener) {
        this.searchListener = searchListener;

        this.searchManagerByAlgorithm = new SearchManagerByAlgorithm(search);

        this.searchManagerByOpenBook = new SearchManagerByOpenBook();
        this.searchManagerByOpenBook.setNext(searchManagerByAlgorithm);

        this.searchManagerChain = this.searchManagerByOpenBook;
        this.searchManagerChain.setSearchResultByDepthListener(searchListener::searchInfo);

        this.timeMgmt = new FivePercentage();
    }

    public void searchInfinite(Game game) {
        searchImp(game, infiniteDepth, 0);
    }

    public void searchDepth(Game game, int depth) {
        searchImp(game, depth, 0);
    }

    public void searchTime(Game game, int timeOut) {
        searchImp(game, infiniteDepth, timeOut);
    }

    public void searchFast(Game game, int wTime, int bTime, int wInc, int bInc) {
        final int timeOut = timeMgmt.getTimeOut(game, wTime, bTime, wInc, bInc);
        searchImp(game, infiniteDepth, timeOut, searchInfo -> timeMgmt.keepSearching(timeOut, searchInfo));
    }

    public void reset() {
        searchManagerChain.reset();
    }

    public void stopSearching() {
        searchManagerChain.stopSearching();
    }

    public void open() {
        searchManagerChain.open();
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
        searchManagerChain.close();
    }

    public void setPolyglotBook(String path) {
        searchManagerByOpenBook.setSearchParameter(SearchParameter.POLYGLOT_FILE, path);
    }

    private void searchImp(Game game, int depth, int timeOut) {
        searchImp(game, depth, timeOut, searchMoveResult -> true);
    }

    private synchronized void searchImp(Game game, int depth, int timeOut, Predicate<SearchResultByDepth> searchPredicate) {
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

                searchManagerChain.setSearchParameter(SearchParameter.MAX_DEPTH, depth);
                searchManagerChain.setSearchParameter(SearchParameter.SEARCH_PREDICATE, searchPredicate);

                SearchResult searchResult = searchManagerChain.search(game);

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