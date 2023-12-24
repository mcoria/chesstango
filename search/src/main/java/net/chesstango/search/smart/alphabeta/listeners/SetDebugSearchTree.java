package net.chesstango.search.smart.alphabeta.listeners;

import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.SearchByDepthContext;
import net.chesstango.search.smart.SearchByDepthListener;

/**
 * @author Mauricio Coria
 */
public class SetDebugSearchTree implements SearchByCycleListener, SearchByDepthListener {


    @Override
    public void beforeSearch(SearchByCycleContext context) {
        System.out.printf("Search started\n");
    }

    @Override
    public void afterSearch() {
        System.out.printf("Search completed\n");
    }

    @Override
    public void beforeSearchByDepth(SearchByDepthContext context) {
        System.out.printf("Search by depth %d started", context.getMaxPly());
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {
        System.out.printf("\nSearch by depth completed\n\n");
    }
}
