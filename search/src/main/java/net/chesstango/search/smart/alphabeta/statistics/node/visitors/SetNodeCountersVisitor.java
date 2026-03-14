package net.chesstango.search.smart.alphabeta.statistics.node.visitors;

import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.statistics.node.filters.*;

/**
 *
 * @author Mauricio Coria
 */
public class SetNodeCountersVisitor implements Visitor {

    private final long[] visitedNodesCounters;
    private final long[] expectedNodesCounters;
    private final long[] visitedNodesCountersQuiescence;
    private final long[] expectedNodesCountersQuiescence;

    public SetNodeCountersVisitor(long[] visitedNodesCounters, long[] expectedNodesCounters, long[] visitedNodesCountersQuiescence, long[] expectedNodesCountersQuiescence) {
        this.visitedNodesCounters = visitedNodesCounters;
        this.expectedNodesCounters = expectedNodesCounters;
        this.visitedNodesCountersQuiescence = visitedNodesCountersQuiescence;
        this.expectedNodesCountersQuiescence = expectedNodesCountersQuiescence;
    }

    public void visit(AlphaBetaInteriorNodeStatistics alphaBetaNodeStatistics) {
        alphaBetaNodeStatistics.setVisitedNodesCounters(visitedNodesCounters);
        alphaBetaNodeStatistics.setExpectedNodesCounters(expectedNodesCounters);
        alphaBetaNodeStatistics.setVisitedNodesCountersQuiescence(visitedNodesCountersQuiescence);
        alphaBetaNodeStatistics.setExpectedNodesCountersQuiescence(expectedNodesCountersQuiescence);
    }

}
