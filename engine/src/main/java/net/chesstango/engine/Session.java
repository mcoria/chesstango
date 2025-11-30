package net.chesstango.engine;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.gardel.fen.FEN;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

/**
 * @author Mauricio Coria
 */
public class Session {
    /**
     * Resultado de las busquedas efectuadas durante el juego.
     */
    @Getter
    private final List<SearchResponse> searchResults = new ArrayList<>();

    private final SearchManager searchManager;

    private final SearchListener sessionSearchListener;

    @Setter
    private SearchListener searchListener;


    @Setter
    private FEN fen;

    @Setter
    private List<String> moves;

    Session(SearchManager searchManager) {
        this.searchManager = searchManager;

        this.sessionSearchListener = new SearchListener() {
            @Override
            public void searchStarted() {
                if (searchListener != null) {
                    searchListener.searchStarted();
                }
            }

            @Override
            public void searchInfo(String searchByDepthResult) {
                if (searchListener != null) {
                    searchListener.searchInfo(searchByDepthResult);
                }
            }

            @Override
            public void searchFinished(SearchResponse searchMoveResult) {
                if (searchListener != null) {
                    searchListener.searchFinished(searchMoveResult);
                }
                searchResults.add(searchMoveResult);
            }
        };
    }

    public Future<SearchResponse> goInfinite() {
        return searchManager.searchInfinite(getGame(), sessionSearchListener);
    }

    public Future<SearchResponse> goDepth(int depth) {
        return searchManager.searchDepth(getGame(), depth, sessionSearchListener);
    }

    public Future<SearchResponse> goTime(int timeOut) {
        return searchManager.searchTime(getGame(), timeOut, sessionSearchListener);
    }

    public Future<SearchResponse> goFast(int wTime, int bTime, int wInc, int bInc) {
        return searchManager.searchFast(getGame(), wTime, bTime, wInc, bInc, sessionSearchListener);
    }

    public void stopSearching() {
        searchManager.stopSearching();
    }

    private Game getGame() {
        return Game.from(fen, moves);
    }
}
