package net.chesstango.engine;

import net.chesstango.board.Game;
import net.chesstango.search.SearchResult;
import net.chesstango.search.SearchResultByDepth;
import net.chesstango.search.SearchResultByDepthListener;

import java.util.concurrent.*;
import java.util.function.Predicate;

/**
 * @author Mauricio Coria
 */
class SearchInvokerAsync implements SearchInvoker {
    private final ExecutorService searchExecutor;
    private final ScheduledExecutorService timeOutExecutor;
    private final SearchChain searchChain;

    private volatile CountDownLatch countDownLatch;
    private volatile Future<SearchResult> currentSearchTask;

    SearchInvokerAsync(SearchChain searchChain, ExecutorService searchExecutor, ScheduledExecutorService timeOutExecutor) {
        this.searchChain = searchChain;
        this.searchExecutor = searchExecutor;
        this.timeOutExecutor = timeOutExecutor;
    }

    @Override
    public Future<SearchResult> searchImp(Game game, int depth, int timeOut, Predicate<SearchResultByDepth> searchPredicate, SearchListener searchListener) {
        if (currentSearchTask != null && !currentSearchTask.isDone()) {
            throw new IllegalStateException("Another search is running");
        }

        countDownLatch = new CountDownLatch(1);

        currentSearchTask = searchExecutor.submit(() -> {
            try {
                searchListener.searchStarted();

                ScheduledFuture<?> stopTask = null;
                if (timeOut != 0) {
                    stopTask = timeOutExecutor.schedule(this::stopSearchingImp, timeOut, TimeUnit.MILLISECONDS);
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

    @Override
    public void stopSearchingImp() {
        try {
            // Espera que al menos se complete un ciclo
            // Aca se puede dar la interrupcion
            countDownLatch.await();

            searchChain.stopSearching();
        } catch (InterruptedException e) {
            // Si ocurre la excepcion quiere decir que terminó normalmente y el thread fué interrumpido, por lo tanto no es necesario triggerStopSearching()
        }
    }
}
