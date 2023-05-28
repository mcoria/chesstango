package net.chesstango.search.manager;


import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.*;
import net.chesstango.search.smart.IterativeDeepening;
import net.chesstango.search.smart.SearchStatusListener;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Mauricio Coria
 */
public class SearchManager {
    private ScheduledExecutorService executorService;
    private final SearchMove searchMove;
    private final SearchListener listenerClient;

    private final SearchStatusListener searchStatusListener = new SearchStatusListener() {
        @Override
        public void info(int depth, int selDepth, List<Move> pv) {
            listenerClient.searchInfo(depth, selDepth, pv);
        }
    };

    public SearchManager(SearchMove searchMove, SearchListener listenerClient) {
        this.searchMove = searchMove;
        this.listenerClient = listenerClient;

        if (searchMove instanceof DefaultSearchMove) {
            DefaultSearchMove searchMoveDefault = (DefaultSearchMove) searchMove;
            SearchMove searchImp = searchMoveDefault.getImplementation();

            if (searchImp instanceof IterativeDeepening) {
                ((IterativeDeepening) searchImp).setSearchStatusListener(searchStatusListener);
            }
        }
    }

    public void searchInfinite(Game game) {
        searchImp(game, Integer.MAX_VALUE, null);
    }

    public void searchUpToDepth(Game game, int depth) {
        searchImp(game, depth, null);
    }

    public void searchUpToTime(Game game, int timeOut) {
        searchImp(game, Integer.MAX_VALUE, timeOut);
    }

    public void reset() {
        searchMove.reset();
    }

    public void stopSearching() {
        searchMove.stopSearching();
    }

    public void open() {
        executorService = Executors.newScheduledThreadPool(2);
    }

    public void close() {
        executorService.shutdown();
        try {
            executorService.awaitTermination(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    private void searchImp(Game game, int depth, Integer timeOut) {
        executorService.execute(() -> {
            try {
                listenerClient.searchStarted();

                if (timeOut != null) {
                    executorService.schedule(this::stopSearching, timeOut, TimeUnit.MILLISECONDS);
                }

                SearchMoveResult searchResult = null;
                try {
                    searchResult = searchMove.search(game, depth);
                } catch (StopSearchingException spe) {
                    searchResult = spe.getSearchMoveResult();
                    listenerClient.searchStopped();
                }

                listenerClient.searchFinished(searchResult);
            } catch (RuntimeException exception) {
                exception.printStackTrace(System.err);
            }
        });
    }
}
