package net.chesstango.engine;

import net.chesstango.board.Game;
import net.chesstango.search.SearchResult;
import net.chesstango.search.SearchResultByDepth;

import java.util.concurrent.Future;
import java.util.function.Predicate;

/**
 * @author Mauricio Coria
 */
interface SearchInvoker {
    Future<SearchResult> searchImp(Game game, int depth, int timeOut, Predicate<SearchResultByDepth> searchPredicate, SearchListener searchListener);

    void stopSearchingImp();
}
