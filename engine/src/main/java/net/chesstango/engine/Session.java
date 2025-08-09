package net.chesstango.engine;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.search.SearchResult;
import net.chesstango.search.SearchResultByDepth;

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
    private final List<SearchResult> searches = new ArrayList<>();

    private final SearchManager searchManager;

    private final FEN fen;

    private final SearchListener sessionSearchListener;

    @Setter
    private SearchListener searchListener;

    @Setter
    private List<String> moves;

    Session(FEN fen, SearchManager searchManager) {
        this.fen = fen;

        this.searchManager = searchManager;

        this.sessionSearchListener = new SearchListener() {
            @Override
            public void searchStarted() {
                if (searchListener != null) {
                    searchListener.searchStarted();
                }
            }

            @Override
            public void searchInfo(SearchResultByDepth searchByDepthResult) {
                if (searchListener != null) {
                    searchListener.searchInfo(searchByDepthResult);
                }
            }

            @Override
            public void searchFinished(SearchResult searchMoveResult) {
                if (searchListener != null) {
                    searchListener.searchFinished(searchMoveResult);
                }
                searches.add(searchMoveResult);
            }
        };
    }

    public Future<SearchResult> goInfinite() {
        return searchManager.searchInfinite(getGame(), sessionSearchListener);
    }

    public Future<SearchResult> goDepth(int depth) {
        return searchManager.searchDepth(getGame(), depth, sessionSearchListener);
    }

    public Future<SearchResult> goTime(int timeOut) {
        return searchManager.searchTime(getGame(), timeOut, sessionSearchListener);
    }

    public Future<SearchResult> goFast(int wTime, int bTime, int wInc, int bInc) {
        return searchManager.searchFast(getGame(), wTime, bTime, wInc, bInc, sessionSearchListener);
    }

    public void stopSearching() {
        searchManager.stopSearching();
    }

    private Game getGame() {
        return Game.from(fen, moves);
    }
}
