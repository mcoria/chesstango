package net.chesstango.engine;

import lombok.extern.slf4j.Slf4j;
import net.chesstango.board.Game;
import net.chesstango.search.SearchResultByDepth;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Predicate;

/**
 * @author Mauricio Coria
 */
@Slf4j
class SearchInvokerAsync extends SearchInvokerAbstract {
    private final ExecutorService searchExecutor;

    SearchInvokerAsync(SearchByChain searchByChain, ExecutorService searchExecutor) {
        super(searchByChain);
        this.searchExecutor = searchExecutor;
    }

    @Override
    public Future<SearchResponse> searchImp(Game game, int depth, Predicate<SearchResultByDepth> searchPredicate, SearchListener searchListener) {
        return searchExecutor.submit(() -> {
            // Submits search task; handles exceptions; returns search response
            return search(game, depth, searchPredicate, searchListener);
        });
    }
}