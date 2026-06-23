package net.chesstango.engine;

import lombok.extern.slf4j.Slf4j;
import net.chesstango.board.Game;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.search.SearchResultByDepth;

import java.nio.file.Path;
import java.util.concurrent.Future;
import java.util.function.Predicate;

/**
 * @author Mauricio Coria
 */
@Slf4j
class SearchManagerReady implements SearchManagerState {
    private final SearchManager searchManager;
    private final SearchInvoker searchInvoker;
    private final SearchByTree searchByTree;
    private final TangoOptions tangoOptions;
    private final int infiniteDepth;

    SearchManagerReady(SearchManager searchManager,
                       SearchInvoker searchInvoker,
                       SearchByTree searchByTree,
                       TangoOptions tangoOptions,
                       int infiniteDepth) {
        this.searchManager = searchManager;
        this.searchInvoker = searchInvoker;
        this.searchByTree = searchByTree;
        this.tangoOptions = tangoOptions;
        this.infiniteDepth = infiniteDepth;
    }

    @Override
    public Future<SearchResponse> searchDepthImp(Game game,
                                                 int depth,
                                                 Predicate<SearchResultByDepth> searchResultByDepthPredicate,
                                                 SearchListener searchListener) {

        log.debug("Searching by depth {}", depth);

        SearchManagerSearchingByDepth searchManagerSearchingByDepth = searchManager.createSearchingByDepthState(searchListener);

        searchManager.setCurrentSearchManagerState(searchManagerSearchingByDepth);

        return searchInvoker.searchImp(game, depth, searchResultByDepthPredicate, searchManagerSearchingByDepth);
    }

    @Override
    public Future<SearchResponse> searchTimeOutImp(Game game,
                                                   int timeOut,
                                                   Predicate<SearchResultByDepth> searchResultByDepthPredicate,
                                                   SearchListener searchListener) {

        log.debug("Searching by time {} ms", timeOut);

        SearchManagerSearchingByTime searchManagerSearchingByTime = searchManager.createSearchingByTimeState(timeOut, searchListener);

        searchManager.setCurrentSearchManagerState(searchManagerSearchingByTime);

        return searchInvoker.searchImp(game, infiniteDepth, searchResultByDepthPredicate, searchManagerSearchingByTime);
    }

    @Override
    public void stopSearchingImp() {
        log.warn("No search in progress");
    }

    @Override
    public Session newSessionImp(FEN fen) {
        searchByTree.reset();
        return new Session(searchManager, fen);
    }

    @Override
    public void setPolyglotFile(Path polyglotFile) {
        tangoOptions.setPolyglotFile(polyglotFile);
    }

    @Override
    public void setSyzygyPath(Path syzygyPath) {
        tangoOptions.setSyzygyPath(syzygyPath);
    }

    @Override
    public void setHashSize(int hashSizeMB) {
        tangoOptions.setHashSize(hashSizeMB);
    }
}
