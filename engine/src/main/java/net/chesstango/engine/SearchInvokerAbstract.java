package net.chesstango.engine;

import lombok.extern.slf4j.Slf4j;
import net.chesstango.board.Game;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.search.PrincipalVariation;
import net.chesstango.search.SearchResultByDepth;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author Mauricio Coria
 */
@Slf4j
abstract class SearchInvokerAbstract implements SearchInvoker {
    private final SearchByChain searchByChain;
    private final SimpleMoveEncoder simpleMoveEncoder = new SimpleMoveEncoder();

    SearchInvokerAbstract(SearchByChain searchByChain) {
        this.searchByChain = searchByChain;
    }

    SearchResponse search(Game game, int depth, Predicate<SearchResultByDepth> searchPredicate, SearchListener searchListener) {
        // Executes and handles exceptions during the search
        try {
            searchListener.searchStarted();

            SearchContext context = new SearchContext()
                    .setGame(game)
                    .setDepth(depth)
                    .setSearchResultByDepthPredicate(searchPredicate)
                    .setSearchResultByDepthConsumer(wrapSearchListener(searchListener));

            SearchResponse searchResult = searchByChain.search(context);

            searchListener.searchFinished(searchResult);

            return searchResult;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    Consumer<SearchResultByDepth> wrapSearchListener(SearchListener searchListener) {
        return searchResultByDepth -> {
            String pv = simpleMoveEncoder
                    .encode(searchResultByDepth
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