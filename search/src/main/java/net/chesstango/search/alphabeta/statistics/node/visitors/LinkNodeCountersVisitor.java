package net.chesstango.search.alphabeta.statistics.node.visitors;

import net.chesstango.search.Visitor;
import net.chesstango.search.alphabeta.statistics.node.NodeCounters;
import net.chesstango.search.alphabeta.statistics.node.filters.*;

/**
 *
 * @author Mauricio Coria
 */
public class LinkNodeCountersVisitor implements Visitor {

    private final NodeCounters nodeCounters;

    public LinkNodeCountersVisitor(NodeCounters nodeCounters) {
        this.nodeCounters = nodeCounters;
    }

    @Override
    public void visit(RootNodeStatistics rootNodeStatistics) {
        rootNodeStatistics.setNodeCounters(nodeCounters);
    }

    @Override
    public void visit(InteriorNodeVisited alphaBetaInteriorNodeStatistics) {
        alphaBetaInteriorNodeStatistics.setNodeCounters(nodeCounters);
    }

    @Override
    public void visit(InteriorNodeExpected interiorNodeExpected) {
        interiorNodeExpected.setNodeCounters(nodeCounters);
    }

    @Override
    public void visit(QuiescenceNodeVisited alphaBetaQuiescenceNodeStatistics) {
        alphaBetaQuiescenceNodeStatistics.setNodeCounters(nodeCounters);
    }

    @Override
    public void visit(QuiescenceNodeExpected quiescenceNodeExpected) {
        quiescenceNodeExpected.setNodeCounters(nodeCounters);
    }


    @Override
    public void visit(LeafNodeStatistics leafNodeStatistics) {
        leafNodeStatistics.setNodeCounters(nodeCounters);
    }

    @Override
    public void visit(TerminalNodeStatistics terminalNodeStatistics) {
        terminalNodeStatistics.setNodeCounters(nodeCounters);
    }

    @Override
    public void visit(LoopNodeStatistics loopNodeStatistics) {
        loopNodeStatistics.setNodeCounters(nodeCounters);
    }

    @Override
    public void visit(EgtbNodeStatistics egtbNodeStatistics) {
        egtbNodeStatistics.setNodeCounters(nodeCounters);
    }

}
