package net.chesstango.search.smart.alphabeta.statistics.node.visitors;

import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.statistics.node.NodeCounters;
import net.chesstango.search.smart.alphabeta.statistics.node.filters.*;

/**
 *
 * @author Mauricio Coria
 */
public class SetNodeCountersVisitor implements Visitor {

    private final NodeCounters nodeCounters;

    public SetNodeCountersVisitor(NodeCounters nodeCounters) {
        this.nodeCounters = nodeCounters;
    }

    @Override
    public void visit(AlphaBetaRootNodeStatistics alphaBetaRootNodeStatistics) {
        alphaBetaRootNodeStatistics.setNodeCounters(nodeCounters);
    }

    @Override
    public void visit(AlphaBetaInteriorNodeStatistics alphaBetaInteriorNodeStatistics) {
        alphaBetaInteriorNodeStatistics.setNodeCounters(nodeCounters);
    }

    @Override
    public void visit(AlphaBetaQuiescenceNodeStatistics alphaBetaQuiescenceNodeStatistics) {
        alphaBetaQuiescenceNodeStatistics.setNodeCounters(nodeCounters);
    }


    @Override
    public void visit(AlphaBetaLeafNodeStatistics alphaBetaLeafNodeStatistics) {
        alphaBetaLeafNodeStatistics.setNodeCounters(nodeCounters);
    }

    @Override
    public void visit(AlphaBetaTerminalNodeStatistics alphaBetaTerminalNodeStatistics) {
        alphaBetaTerminalNodeStatistics.setNodeCounters(nodeCounters);
    }

}
