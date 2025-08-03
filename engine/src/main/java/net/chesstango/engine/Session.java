package net.chesstango.engine;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.search.SearchResult;
import net.chesstango.search.SearchResultByDepth;

import java.util.ArrayList;
import java.util.List;

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

    public void goInfinite() {
        searchManager.searchInfinite(getGame(), sessionSearchListener);
    }

    public void goDepth(int depth) {
        searchManager.searchDepth(getGame(), depth, sessionSearchListener);
    }

    public void goTime(int timeOut) {
        searchManager.searchTime(getGame(), timeOut, sessionSearchListener);
    }

    public void goFast(int wTime, int bTime, int wInc, int bInc) {
        searchManager.searchFast(getGame(), wTime, bTime, wInc, bInc, sessionSearchListener);
    }

    public void stopSearching() {
        searchManager.stopSearching();
    }

    Game getGame() {
        return Game.from(fen, moves);
    }
}
