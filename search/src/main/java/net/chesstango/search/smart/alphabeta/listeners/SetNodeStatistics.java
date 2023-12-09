package net.chesstango.search.smart.alphabeta.listeners;

import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByDepthListener;
import net.chesstango.search.smart.SearchByDepthContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.statistics.NodeStatistics;

/**
 * @author Mauricio Coria
 */
public class SetNodeStatistics implements SearchByCycleListener, SearchByDepthListener {

    private int[] visitedNodesCounters;
    private int[] expectedNodesCounters;
    private int[] visitedNodesCountersQuiescence;
    private int[] expectedNodesCountersQuiescence;

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.visitedNodesCounters = new int[30];
        this.expectedNodesCounters = new int[30];
        this.visitedNodesCountersQuiescence = new int[30];
        this.expectedNodesCountersQuiescence = new int[30];

        context.setVisitedNodesCounters(visitedNodesCounters);
        context.setExpectedNodesCounters(expectedNodesCounters);

        context.setVisitedNodesCountersQuiescence(visitedNodesCountersQuiescence);
        context.setExpectedNodesCountersQuiescence(expectedNodesCountersQuiescence);
    }

    @Override
    public void afterSearch() {
        this.visitedNodesCounters = null;
        this.expectedNodesCounters =  null;
        this.visitedNodesCountersQuiescence = null;
        this.expectedNodesCountersQuiescence = null;
    }

    @Override
    public void beforeSearchByDepth(SearchByDepthContext context) {
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {
        if (result != null) {
            result.setRegularNodeStatistics(new NodeStatistics(expectedNodesCounters, visitedNodesCounters));
            result.setQuiescenceNodeStatistics(new NodeStatistics(expectedNodesCountersQuiescence, visitedNodesCountersQuiescence));
        }
    }
}
