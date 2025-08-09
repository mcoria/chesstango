package net.chesstango.engine;

import net.chesstango.board.Game;
import net.chesstango.search.SearchResult;
import net.chesstango.search.SearchResultByDepth;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Predicate;

/**
 * @author Mauricio Coria
 */
class SearchInvokerAsync implements SearchInvoker {
    private final ExecutorService searchExecutor;
    private final SearchChain searchChain;

    private volatile Future<SearchResult> currentSearchTask;

    SearchInvokerAsync(SearchChain searchChain, ExecutorService searchExecutor) {
        this.searchChain = searchChain;
        this.searchExecutor = searchExecutor;
    }

    @Override
    public Future<SearchResult> searchImp(Game game, int depth, Predicate<SearchResultByDepth> searchPredicate, SearchListener searchListener) {
        if (currentSearchTask != null && !currentSearchTask.isDone()) {
            throw new IllegalStateException("Another search is running");
        }

        currentSearchTask = searchExecutor.submit(() -> {
            try {
                searchListener.searchStarted();

                SearchContext context = new SearchContext()
                        .setGame(game)
                        .setDepth(depth)
                        .setSearchPredicate(searchPredicate)
                        .setSearchResultByDepthListener(searchListener::searchInfo);

                SearchResult searchResult = searchChain.search(context);

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
