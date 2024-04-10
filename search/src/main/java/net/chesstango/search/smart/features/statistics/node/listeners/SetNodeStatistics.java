package net.chesstango.search.smart.features.statistics.node.listeners;

import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.features.statistics.node.NodeStatistics;

/**
 * @author Mauricio Coria
 */
public class SetNodeStatistics implements SearchByCycleListener {

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
    public void afterSearch(SearchMoveResult result) {
        if (result != null) {
            result.setRegularNodeStatistics(new NodeStatistics(expectedNodesCounters, visitedNodesCounters));
            result.setQuiescenceNodeStatistics(new NodeStatistics(expectedNodesCountersQuiescence, visitedNodesCountersQuiescence));
        }
    }
}
