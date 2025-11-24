package net.chesstango.engine;

import net.chesstango.board.Game;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.search.PrincipalVariation;
import net.chesstango.search.SearchResultByDepth;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author Mauricio Coria
 */
class SearchInvokerSync implements SearchInvoker {
    private final SearchChain searchChain;
    private final SimpleMoveEncoder simpleMoveEncoder = new SimpleMoveEncoder();

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
                    .setSearchResultByDepthListener(wrappSearchListener(searchListener));

            SearchResponse searchResult = searchChain.search(context);

            searchListener.searchFinished(searchResult);

            return CompletableFuture.completedFuture(searchResult);
        } catch (RuntimeException e) {
            e.printStackTrace(System.err);
            throw new RuntimeException(e);
        }
    }

    private Consumer<SearchResultByDepth> wrappSearchListener(SearchListener searchListener) {
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


