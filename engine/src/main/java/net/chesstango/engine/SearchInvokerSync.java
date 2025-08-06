package net.chesstango.engine;

import net.chesstango.board.Game;
import net.chesstango.search.SearchResult;
import net.chesstango.search.SearchResultByDepth;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

/**
 * @author Mauricio Coria
 */
class SearchInvokerSync implements SearchInvoker {
    private final SearchChain searchChain;

    SearchInvokerSync(SearchChain searchChain) {
        this.searchChain = searchChain;
    }

    @Override
    public Future<SearchResult> searchImp(Game game, int depth, int timeOut, Predicate<SearchResultByDepth> searchPredicate, SearchListener searchListener) {
        try {
            searchListener.searchStarted();

            SearchContext context = new SearchContext()
                    .setGame(game)
                    .setDepth(depth)
                    .setSearchPredicate(searchPredicate)
                    .setSearchResultByDepthListener(searchListener::searchInfo);

            SearchResult searchResult = searchChain.search(context);

            searchListener.searchFinished(searchResult);

            return wrapResult(searchResult);
        } catch (RuntimeException e) {
            e.printStackTrace(System.err);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stopSearchingImp() {
        searchChain.stopSearching();
    }

    
    private Future<SearchResult> wrapResult(SearchResult searchResult) {
        return new ImmediateFuture(searchResult);
    }

    private static class ImmediateFuture implements Future<SearchResult> {
        private final SearchResult result;

        public ImmediateFuture(SearchResult result) {
            this.result = result;
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            return false;
        }

        @Override
        public boolean isCancelled() {
            return false;
        }

        @Override
        public boolean isDone() {
            return true;
        }

        @Override
        public SearchResult get() {
            return result;
        }

        @Override
        public SearchResult get(long timeout, TimeUnit unit) {
            return result;
        }
    }
}
