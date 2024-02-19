package net.chesstango.engine;

import net.chesstango.board.Game;
import net.chesstango.engine.manager.SearchManagerByAlgorithm;
import net.chesstango.engine.manager.SearchManagerByBook;
import net.chesstango.engine.manager.SearchManagerChain;
import net.chesstango.engine.timemgmt.FivePercentage;
import net.chesstango.engine.timemgmt.TimeMgmt;
import net.chesstango.search.SearchByDepthResult;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.SearchParameter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

/**
 * @author Mauricio Coria
 */
public final class SearchManager {
    private final int INFINITE_DEPTH = 100;
    private final SearchListener listenerClient;
    private final SearchManagerChain searchManagerChain;
    private final SearchManagerByBook searchManagerByBook;
    private final SearchManagerByAlgorithm searchManagerByAlgorithm;
    private final TimeMgmt timeMgmt;
    private ScheduledExecutorService executorService;

    public SearchManager(SearchMove searchMove, SearchListener listenerClient) {
        this.listenerClient = listenerClient;

        this.searchManagerByAlgorithm = new SearchManagerByAlgorithm(searchMove);

        this.searchManagerByBook = new SearchManagerByBook();
        this.searchManagerByBook.setNext(searchManagerByAlgorithm);

        this.searchManagerChain = this.searchManagerByBook;
        this.searchManagerChain.setProgressListener(listenerClient::searchInfo);

        this.timeMgmt = new FivePercentage();
    }

    public void searchInfinite(Game game) {
        searchImp(game, INFINITE_DEPTH, 0);
    }

    public void searchDepth(Game game, int depth) {
        searchImp(game, depth, 0);
    }

    public void searchTime(Game game, int timeOut) {
        searchImp(game, INFINITE_DEPTH, timeOut);
    }

    public void searchFast(Game game, int wTime, int bTime, int wInc, int bInc) {
        final int timeOut = timeMgmt.getTimeOut(game, wTime, bTime, wInc, bInc);
        searchImp(game, INFINITE_DEPTH, timeOut, searchInfo -> timeMgmt.keepSearching(timeOut, searchInfo));
    }

    public void reset() {
        searchManagerChain.reset();
    }

    public void stopSearching() {
        searchManagerChain.stopSearching();
    }

    public void open() {
        executorService = Executors.newScheduledThreadPool(2);
        searchManagerChain.open();
    }

    public void close() {
        try {
            executorService.shutdown();
            executorService.awaitTermination(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        searchManagerChain.close();
    }

    public void setPolyglotBook(String path) {
        searchManagerByBook.setSearchParameter(SearchParameter.POLYGLOT_PATH, path);
    }

    private void searchImp(Game game, int depth, int timeOut) {
        searchImp(game, depth, timeOut, searchMoveResult -> true);
    }

    private void searchImp(Game game, int depth, int timeOut, Predicate<SearchByDepthResult> searchPredicate) {
        executorService.execute(() -> {
            try {
                listenerClient.searchStarted();

                ScheduledFuture<?> stopTask = null;
                if (timeOut != 0) {
                    stopTask = executorService.schedule(this::stopSearching, timeOut, TimeUnit.MILLISECONDS);
                }

                searchManagerChain.setSearchParameter(SearchParameter.MAX_DEPTH, depth);
                searchManagerChain.setSearchParameter(SearchParameter.SEARCH_PREDICATE, searchPredicate);
                SearchMoveResult searchResult = searchManagerChain.search(game);

                if (stopTask != null && !stopTask.isDone()) {
                    stopTask.cancel(true);
                }

                listenerClient.searchFinished(searchResult);
            } catch (RuntimeException e) {
                e.printStackTrace(System.err);
            }
        });
    }
}