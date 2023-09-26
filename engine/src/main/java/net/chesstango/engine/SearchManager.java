package net.chesstango.engine;

import net.chesstango.board.Game;
import net.chesstango.engine.manager.SearchManagerChain;
import net.chesstango.engine.manager.SearchManagerChainBuilder;
import net.chesstango.search.DefaultSearchMove;
import net.chesstango.search.SearchListener;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.IterativeDeepening;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Mauricio Coria
 */
public class SearchManager {
    private final SearchListener listenerClient;
    private final SearchManagerChain searchManagerChain;

    private ScheduledExecutorService executorService;

    public SearchManager(SearchMove searchMove, SearchListener listenerClient) {
        this.listenerClient = listenerClient;
        this.searchManagerChain = new SearchManagerChainBuilder().withSearchByBookEnabled(false).withSearchMove(searchMove).withSearchListener(listenerClient).build();

        if (searchMove instanceof DefaultSearchMove searchMoveDefault) {
            SearchMove searchImp = searchMoveDefault.getImplementation();

            if (searchImp instanceof IterativeDeepening) {
                ((IterativeDeepening) searchImp).setSearchStatusListener(listenerClient::searchInfo);
            }
        }
    }

    public void searchInfinite(Game game) {
        searchImp(game, Integer.MAX_VALUE, 0);
    }

    public void searchDepth(Game game, int depth) {
        searchImp(game, depth, 0);
    }

    public void searchTime(Game game, int timeOut) {
        searchImp(game, Integer.MAX_VALUE, timeOut);
    }

    public void searchFast(Game game, int wTime, int bTime, int wInc, int bInc) {
        int timeOut = 2000;
        searchImp(game, Integer.MAX_VALUE, timeOut);
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

    private void searchImp(Game game, int depth, int timeOut) {
        executorService.execute(() -> {
            try {
                listenerClient.searchStarted();

                if (timeOut != 0) {
                    executorService.schedule(this::stopSearching, timeOut, TimeUnit.MILLISECONDS);
                }

                SearchMoveResult searchResult = searchManagerChain.searchImp(game, depth);

                listenerClient.searchFinished(searchResult);
            } catch (RuntimeException e) {
                e.printStackTrace(System.err);
            }
        });
    }


}
