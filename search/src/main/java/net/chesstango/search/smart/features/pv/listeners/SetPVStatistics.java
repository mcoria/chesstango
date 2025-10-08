package net.chesstango.search.smart.features.pv.listeners;

import net.chesstango.search.SearchResultByDepth;
import net.chesstango.search.SearchResult;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.SearchByDepthContext;
import net.chesstango.search.smart.SearchByDepthListener;

/**
 * @author Mauricio Coria
 */
public class SetPVStatistics implements SearchByCycleListener, SearchByDepthListener {

    private int pvCompleteCounter;

    @Override
    public void beforeSearch() {
        this.pvCompleteCounter = 0;
    }

    @Override
    public void afterSearch(SearchResult result) {
        //result.setSearchByDepthPvCompleteCounter(pvCompleteCounter);
    }

    @Override
    public void beforeSearchByDepth(SearchByDepthContext context) {
    }

    @Override
    public void afterSearchByDepth(SearchResultByDepth result) {
        if (result.isPvComplete()) {
            pvCompleteCounter++;
        }
    }
}
