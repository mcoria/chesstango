package net.chesstango.search;

import lombok.Getter;
import net.chesstango.board.Game;

import java.util.function.Function;

/**
 * @author Mauricio Coria
 */
public class SearchMoveGameWrapper implements SearchMove {

    @Getter
    private final SearchMove imp;

    private final Function<Game, Game> fnGameWrapper;

    public SearchMoveGameWrapper(SearchMove imp, Function<Game, Game> fnGameWrapper) {
        this.imp = imp;
        this.fnGameWrapper = fnGameWrapper;
    }

    @Override
    public SearchMoveResult search(Game game) {
        return imp.search(fnGameWrapper.apply(game));
    }

    @Override
    public void stopSearching() {
        imp.stopSearching();
    }

    @Override
    public void reset() {
        imp.reset();
    }

    @Override
    public void setSearchParameter(SearchParameter parameter, Object value) {
        imp.setSearchParameter(parameter, value);
    }

    @Override
    public void setProgressListener(ProgressListener progressListener) {
        imp.setProgressListener(progressListener);
    }


}
