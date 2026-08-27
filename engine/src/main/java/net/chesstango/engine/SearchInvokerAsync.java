package net.chesstango.engine;

import lombok.extern.slf4j.Slf4j;
import net.chesstango.board.Game;
import net.chesstango.search.SearchResultByDepth;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
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
    public CompletableFuture<SearchResponse> searchImp(Game game, int depth, Predicate<SearchResultByDepth> searchPredicate, SearchListener searchListener) {
        return CompletableFuture.supplyAsync(() -> search(game, depth, searchPredicate, searchListener), searchExecutor);
    }
}