package net.chesstango.engine;

import net.chesstango.board.Game;
import net.chesstango.engine.timemgmt.TimeMgmt;
import net.chesstango.search.SearchResult;
import net.chesstango.search.SearchResultByDepth;

import java.util.concurrent.Future;
import java.util.function.Predicate;

/**
 * @author Mauricio Coria
 */
class SearchManager implements AutoCloseable {
    private final int infiniteDepth;
    private final SearchChain searchChain;
    private final TimeMgmt timeMgmt;
    private final SearchInvoker searchInvoker;


    public SearchManager(int infiniteDepth, SearchChain searchChain, TimeMgmt timeMgmt, SearchInvoker searchInvoker) {
        this.infiniteDepth = infiniteDepth;
        this.searchChain = searchChain;
        this.timeMgmt = timeMgmt;
        this.searchInvoker = searchInvoker;
    }

    public Future<SearchResult> searchInfinite(Game game, SearchListener searchListener) {
        return searchImp(game, infiniteDepth, 0, searchMoveResult -> true, searchListener);
    }

    public Future<SearchResult> searchDepth(Game game, int depth, SearchListener searchListener) {
        return searchImp(game, depth, 0, searchMoveResult -> true, searchListener);
    }

    public Future<SearchResult> searchTime(Game game, int timeOut, SearchListener searchListener) {
        return searchImp(game, infiniteDepth, timeOut, searchMoveResult -> true, searchListener);
    }

    public Future<SearchResult> searchFast(Game game, int wTime, int bTime, int wInc, int bInc, SearchListener searchListener) {
        int timeOut = timeMgmt.getTimeOut(game, wTime, bTime, wInc, bInc);
        return searchImp(game, infiniteDepth, timeOut, searchInfo -> timeMgmt.keepSearching(timeOut, searchInfo), searchListener);
    }

    public synchronized void stopSearching() {
        searchInvoker.stopSearchingImp();
    }

    public synchronized void reset() {
        searchChain.reset();
    }

    @Override
    public synchronized void close() throws Exception {
        searchChain.close();
    }

    private synchronized Future<SearchResult> searchImp(Game game, int depth, int timeOut, Predicate<SearchResultByDepth> searchPredicate, SearchListener searchListener) {
        return searchInvoker.searchImp(game, depth, timeOut, searchPredicate, searchListener);
    }

}