package net.chesstango.search.smart.alphabeta.listeners;

import net.chesstango.search.SearchByDepthResult;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.SearchByDepthContext;
import net.chesstango.search.smart.SearchByDepthListener;

/**
 * @author Mauricio Coria
 */
public class SetPVStatistics implements SearchByCycleListener, SearchByDepthListener {

    private int pvTruncatedCounter;

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.pvTruncatedCounter = 0;
    }

    @Override
    public void afterSearch(SearchMoveResult searchMoveResult) {
        searchMoveResult.setSearchByDepthPvCompleteCounter(pvTruncatedCounter);
    }

    @Override
    public void beforeSearchByDepth(SearchByDepthContext context) {
    }

    @Override
    public void afterSearchByDepth(SearchByDepthResult result) {
        if (result.isPvComplete()) {
            pvTruncatedCounter++;
        }
    }
}
