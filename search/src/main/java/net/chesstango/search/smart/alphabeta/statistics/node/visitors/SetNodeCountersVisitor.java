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

    public SetNodeCountersVisitor(long[] visitedNodesCounters, long[] expectedNodesCounters) {
        this.visitedNodesCounters = visitedNodesCounters;
        this.expectedNodesCounters = expectedNodesCounters;
    }

    @Override
    public void visit(AlphaBetaInteriorNodeStatistics alphaBetaNodeStatistics) {
        alphaBetaNodeStatistics.setVisitedNodesCounters(visitedNodesCounters);
        alphaBetaNodeStatistics.setExpectedNodesCounters(expectedNodesCounters);
    }

    @Override
    public void visit(AlphaBetaTerminalNodeStatistics alphaBetaTerminalNodeStatistics) {
        alphaBetaTerminalNodeStatistics.setVisitedNodesCounters(visitedNodesCounters);
    }

}
