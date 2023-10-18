package net.chesstango.search.smart.alphabeta.listeners;

import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.SearchLifeCycle;

/**
 * @author Mauricio Coria
 */
public class SetPVStorage implements SearchLifeCycle {
    private final short[][] trianglePV;

    public SetPVStorage() {
        trianglePV = new short[30][30];
    }

    @Override
    public void beforeSearch(Game game) {
    }

    /**
     * NO LO RESETTEA EN CADA SEARCH POR WINDOWS
     */
    @Override
    public void beforeSearchByDepth(SearchContext context) {
        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 30; j++) {
                trianglePV[i][j] = 0;
            }
        }
        context.setTrianglePV(trianglePV);
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {

    }

    @Override
    public void afterSearch(SearchMoveResult result) {

    }

    @Override
    public void stopSearching() {

    }

    @Override
    public void reset() {

    }
}
