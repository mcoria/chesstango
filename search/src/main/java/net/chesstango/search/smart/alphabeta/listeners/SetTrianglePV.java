package net.chesstango.search.smart.alphabeta.listeners;

import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchByDepthContext;
import net.chesstango.search.smart.SearchByDepthListener;

/**
 * @author Mauricio Coria
 */
public class SetTrianglePV implements SearchByDepthListener {

    private final short[][] trianglePV;

    public SetTrianglePV() {
        trianglePV = new short[40][40];
    }

    @Override
    public void beforeSearchByDepth(SearchByDepthContext context) {
        for (int i = 0; i < 40; i++) {
            for (int j = 0; j < 40; j++) {
                trianglePV[i][j] = 0;
            }
        }
        context.setTrianglePV(trianglePV);
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {
    }

}
