package net.chesstango.engine.manager;

import net.chesstango.board.Game;
import net.chesstango.search.SearchListener;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.SearchParameter;

/**
 * @author Mauricio Coria
 */
public final class SearchManagerByAlgorithm implements SearchManagerChain {
    private final SearchListener listenerClient;
    private final SearchMove searchMove;

    public SearchManagerByAlgorithm(SearchMove searchMove, SearchListener listenerClient) {
        this.listenerClient = listenerClient;
        this.searchMove = searchMove;
    }

    @Override
    public void reset() {
        searchMove.reset();
    }

    @Override
    public void setParameter(SearchParameter parameter, Object value) {
        searchMove.setParameter(parameter, value);
    }

    @Override
    public void stopSearching() {
        searchMove.stopSearching();
    }

    @Override
    public void open() {
    }

    @Override
    public void close() {
    }

    @Override
    public SearchMoveResult search(Game game) {
        return searchMove.search(game);
    }
}
