package net.chesstango.engine;

import net.chesstango.board.Game;
import net.chesstango.search.SearchResultByDepth;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
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
    public Future<SearchResponse> searchImp(Game game, int depth, Predicate<SearchResultByDepth> searchPredicate, SearchListener searchListener) {
        try {
            searchListener.searchStarted();

            SearchContext context = new SearchContext()
                    .setGame(game)
                    .setDepth(depth)
                    .setSearchPredicate(searchPredicate)
                    .setSearchResultByDepthListener(searchListener::searchInfo);

            SearchResponse searchResult = searchChain.search(context);

            searchListener.searchFinished(searchResult);

            return CompletableFuture.completedFuture(searchResult);
        } catch (RuntimeException e) {
            e.printStackTrace(System.err);
            throw new RuntimeException(e);
        }
    }
}
