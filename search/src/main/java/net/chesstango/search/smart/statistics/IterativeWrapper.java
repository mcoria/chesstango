package net.chesstango.search.smart.statistics;

import net.chesstango.board.Game;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;

/**
 * @author Mauricio Coria
 */
public class IterativeWrapper implements SearchMove {
    private final SearchMove imp;

    public IterativeWrapper(SearchMove imp) {
        this.imp = imp;
    }

    @Override
    public SearchMoveResult search(Game game, int depth) {
        return imp.search(new GameStatistics(game), depth);
    }

    @Override
    public void stopSearching() {
        imp.stopSearching();
    }

    @Override
    public void reset() {
        imp.reset();
    }
}
