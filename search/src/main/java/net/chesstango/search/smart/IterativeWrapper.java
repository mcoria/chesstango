package net.chesstango.search.smart;

import net.chesstango.board.Game;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.SearchParameter;
import net.chesstango.search.smart.statistics.GameStatistics;

/**
 * @author Mauricio Coria
 */
public class IterativeWrapper implements SearchMove {
    private final SearchMove imp;

    public IterativeWrapper(SearchMove imp) {
        this.imp = imp;
    }

    @Override
    public SearchMoveResult search(Game game) {
        return imp.search(new GameStatistics(game));
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
    public void setParameter(SearchParameter parameter, Object value) {
        imp.setParameter(parameter, value);
    }
}
