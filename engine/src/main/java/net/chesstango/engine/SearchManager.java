package net.chesstango.engine;

import net.chesstango.board.Game;
import net.chesstango.engine.manager.SearchManagerByAlgorithm;
import net.chesstango.engine.manager.SearchManagerByBook;
import net.chesstango.engine.manager.SearchManagerChain;
import net.chesstango.engine.timemgmt.ProportionalMoves;
import net.chesstango.engine.timemgmt.TimeMgmt;
import net.chesstango.search.DefaultSearchMove;
import net.chesstango.search.SearchListener;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.IterativeDeepening;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author Mauricio Coria
 */
public final class SearchManager {
    private final SearchListener listenerClient;
    private final SearchManagerChain searchManagerChain;
    private final SearchManagerByBook searchManagerByBook;

    private final TimeMgmt timeMgmt;

    private ScheduledExecutorService executorService;

    public SearchManager(SearchMove searchMove, SearchListener listenerClient) {
        this.listenerClient = listenerClient;

        SearchManagerByAlgorithm searchManagerByAlgorithm = new SearchManagerByAlgorithm(searchMove, listenerClient);
        this.searchManagerByBook = new SearchManagerByBook(searchManagerByAlgorithm);
        this.searchManagerChain = this.searchManagerByBook;

        this.timeMgmt = new ProportionalMoves();

        if (searchMove instanceof DefaultSearchMove searchMoveDefault) {
            SearchMove searchImp = searchMoveDefault.getImplementation();

            if (searchImp instanceof IterativeDeepening iterativeDeepening) {
                iterativeDeepening.setSearchStatusListener(listenerClient::searchInfo);
            }
        }
    }

    public void searchInfinite(Game game) {
        searchDepth(game, Integer.MAX_VALUE);
    }

    public void searchDepth(Game game, int depth) {
        searchImp(game, depth, 0);
    }

    public void searchTime(Game game, int timeOut) {
        searchImp(game, Integer.MAX_VALUE, timeOut);
    }

    public void searchFast(Game game, int wTime, int bTime, int wInc, int bInc) {
        int timeOut = timeMgmt.getSearchTime(game, wTime, bTime, wInc, bInc);
        searchTime(game, timeOut);
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
        searchManagerByBook.setPolyglotBook(path);
    }

    private void searchImp(Game game, int depth, int timeOut) {
        executorService.execute(() -> {
            try {
                listenerClient.searchStarted();

                ScheduledFuture<?> stopTask = null;
                if (timeOut != 0) {
                    stopTask = executorService.schedule(this::stopSearching, timeOut, TimeUnit.MILLISECONDS);
                }

                SearchMoveResult searchResult = searchManagerChain.searchImp(game, depth);

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