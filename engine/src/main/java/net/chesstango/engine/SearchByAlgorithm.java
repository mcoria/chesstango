package net.chesstango.engine;

import net.chesstango.board.Game;
import net.chesstango.search.*;

/**
 * @author Mauricio Coria
 */
public final class SearchByAlgorithm implements SearchChain {
    private final Search search;


    public SearchByAlgorithm() {
        this(new DefaultSearch());
    }

    public SearchByAlgorithm(Search search) {
        this.search = search;
    }

    @Override
    public void reset() {
        search.reset();
    }

    @Override
    public void setSearchParameter(SearchParameter parameter, Object value) {
        search.setSearchParameter(parameter, value);
    }

    @Override
    public void stopSearching() {
        search.stopSearching();
    }

    @Override
    public void open() {
    }

    @Override
    public void close() {
    }

    @Override
    public SearchResult search(Game game) {
        return search.search(game);
    }
}
