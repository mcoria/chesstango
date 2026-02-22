package net.chesstango.search.smart.features.statistics.node.visitors;

import net.chesstango.search.Visitor;
import net.chesstango.search.smart.features.statistics.node.filters.AlphaBetaStatisticsExpected;
import net.chesstango.search.smart.features.statistics.node.filters.AlphaBetaStatisticsVisited;
import net.chesstango.search.smart.features.statistics.node.filters.QuiescenceStatisticsExpected;
import net.chesstango.search.smart.features.statistics.node.filters.QuiescenceStatisticsVisited;

/**
 *
 * @author Mauricio Coria
 */
public class SetStaticCountersVisitor implements Visitor {

    private final long[] visitedNodesCounters;
    private final long[] expectedNodesCounters;
    private final long[] visitedNodesCountersQuiescence;
    private final long[] expectedNodesCountersQuiescence;

    public SetStaticCountersVisitor(long[] visitedNodesCounters, long[] expectedNodesCounters, long[] visitedNodesCountersQuiescence, long[] expectedNodesCountersQuiescence) {
        this.visitedNodesCounters = visitedNodesCounters;
        this.expectedNodesCounters = expectedNodesCounters;
        this.visitedNodesCountersQuiescence = visitedNodesCountersQuiescence;
        this.expectedNodesCountersQuiescence = expectedNodesCountersQuiescence;
    }


    @Override
    public void visit(AlphaBetaStatisticsExpected alphaBetaStatisticsExpected) {
        alphaBetaStatisticsExpected.setExpectedNodesCounters(expectedNodesCounters);
    }

    @Override
    public void visit(AlphaBetaStatisticsVisited alphaBetaStatisticsVisited) {
        alphaBetaStatisticsVisited.setVisitedNodesCounters(visitedNodesCounters);
    }

    @Override
    public void visit(QuiescenceStatisticsExpected quiescenceStatisticsExpected) {
        quiescenceStatisticsExpected.setExpectedNodesCounters(expectedNodesCountersQuiescence);
    }

    @Override
    public void visit(QuiescenceStatisticsVisited quiescenceStatisticsVisited) {
        quiescenceStatisticsVisited.setVisitedNodesCounters(visitedNodesCountersQuiescence);
    }


}
