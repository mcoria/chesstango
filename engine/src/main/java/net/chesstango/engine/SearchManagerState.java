package net.chesstango.engine;

import net.chesstango.board.Game;
import net.chesstango.search.SearchResultByDepth;

import java.util.concurrent.Future;
import java.util.function.Predicate;

/**
 * @author Mauricio Coria
 */
interface SearchManagerState {
    Future<SearchResponse> searchDepthImp(Game game, int depth, Predicate<SearchResultByDepth> searchPredicate, SearchListener searchListener);

    Future<SearchResponse> searchTimeOutImp(Game game, int timeOut, Predicate<SearchResultByDepth> searchPredicate, SearchListener searchListener);

    void stopSearchingImp();
}
