package net.chesstango.engine;

import net.chesstango.board.Game;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.search.SearchResultByDepth;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Predicate;

/**
 * @author Mauricio Coria
 */
interface SearchManagerState extends TangoOptions {
    CompletableFuture<SearchResponse> searchDepthImp(Game game, int depth, Predicate<SearchResultByDepth> searchPredicate, SearchListener searchListener);

    CompletableFuture<SearchResponse> searchTimeOutImp(Game game, int timeOutMs, Predicate<SearchResultByDepth> searchPredicate, SearchListener searchListener);

    void stopSearchingImp();

    Session newSessionImp(FEN fen);
}
