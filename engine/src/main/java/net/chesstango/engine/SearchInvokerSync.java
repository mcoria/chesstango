package net.chesstango.engine;

import lombok.extern.slf4j.Slf4j;
import net.chesstango.board.Game;
import net.chesstango.search.SearchResultByDepth;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Predicate;

/**
 * @author Mauricio Coria
 */
@Slf4j
class SearchInvokerSync extends SearchInvokerAbstract {
    SearchInvokerSync(SearchByChain searchByChain) {
        super(searchByChain);
    }

    @Override
    public Future<SearchResponse> searchImp(Game game, int depth, Predicate<SearchResultByDepth> searchPredicate, SearchListener searchListener) {

        SearchResponse searchResponse = search(game, depth, searchPredicate, searchListener);

        return CompletableFuture.completedFuture(searchResponse);
    }

}