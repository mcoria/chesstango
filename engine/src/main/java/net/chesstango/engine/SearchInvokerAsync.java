package net.chesstango.engine;

import net.chesstango.board.Game;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.search.PrincipalVariation;
import net.chesstango.search.SearchResultByDepth;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author Mauricio Coria
 */
class SearchInvokerAsync implements SearchInvoker {
    private final ExecutorService searchExecutor;
    private final SearchChain searchChain;
    private final SimpleMoveEncoder simpleMoveEncoder = new SimpleMoveEncoder();

    private volatile Future<SearchResponse> currentSearchTask;

    SearchInvokerAsync(SearchChain searchChain, ExecutorService searchExecutor) {
        this.searchChain = searchChain;
        this.searchExecutor = searchExecutor;
    }

    @Override
    public Future<SearchResponse> searchImp(Game game, int depth, Predicate<SearchResultByDepth> searchPredicate, SearchListener searchListener) {
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
                        .setSearchResultByDepthListener(createSearchListener(searchListener));

                SearchResponse searchResponse = searchChain.search(context);

                searchListener.searchFinished(searchResponse);

                return searchResponse;
            } catch (RuntimeException e) {
                e.printStackTrace(System.err);
                throw new RuntimeException(e);
            }
        });

        return currentSearchTask;
    }

    private Consumer<SearchResultByDepth> createSearchListener(SearchListener searchListener) {
        return searchResultByDepth -> {
            String pv = simpleMoveEncoder
                    .encodeMoves(searchResultByDepth
                            .getPrincipalVariation()
                            .stream()
                            .map(PrincipalVariation::move)
                            .toList()
                    );
            String infoStr = String.format("depth %d seldepth %d pv %s", searchResultByDepth.getDepth(), searchResultByDepth.getDepth(), pv);

            searchListener.searchInfo(infoStr);
        };
    }
}
