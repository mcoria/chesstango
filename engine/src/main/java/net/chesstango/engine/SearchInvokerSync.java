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
    private final SearchByChain searchByChain;
    private final SimpleMoveEncoder simpleMoveEncoder = new SimpleMoveEncoder();

    SearchInvokerSync(SearchByChain searchByChain) {
        this.searchByChain = searchByChain;
    }

    @Override
    public Future<SearchResponse> searchImp(Game game, int depth, Predicate<SearchResultByDepth> searchPredicate, SearchListener searchListener) {
        try {
            searchListener.searchStarted();

            SearchContext context = new SearchContext()
                    .setGame(game)
                    .setDepth(depth)
                    .setSearchResultByDepthPredicate(searchPredicate)
                    .setSearchResultByDepthConsumer(wrappSearchListener(searchListener));

            SearchResponse searchResult = searchByChain.search(context);

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