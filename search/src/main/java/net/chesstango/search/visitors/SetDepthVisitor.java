package net.chesstango.search.visitors;

import net.chesstango.search.Visitor;
import net.chesstango.search.alphabeta.core.filters.AlphaBetaFlowControl;
import net.chesstango.search.alphabeta.debug.filters.DebugFilter;
import net.chesstango.search.alphabeta.debug.listeners.PrintTxtDebugListener;
import net.chesstango.search.alphabeta.quiescence.QuiescenceStandingPat;
import net.chesstango.search.alphabeta.statistics.game.DepthCollector;
import net.chesstango.search.alphabeta.statistics.node.NodeCounters;
import net.chesstango.search.alphabeta.statistics.node.filters.*;
import net.chesstango.search.alphabeta.transposition.filters.TranspositionTable;
import net.chesstango.search.alphabeta.transposition.filters.TranspositionTableQ;
import net.chesstango.search.alphabeta.transposition.filters.TranspositionTableRoot;


/**
 * The search depth for the current iteration cycle.
 * This visitor is responsible for propagating the current search depth to all components
 * that need to be aware of it during a specific search iteration. The depth value represents
 * how many plies (half-moves) deep the search algorithm will explore from the current position.
 * This is typically used in iterative deepening search, where the depth increases with each cycle.
 *
 * @author Mauricio Coria
 * @see SetMaxDepthVisitor
 */
public class SetDepthVisitor implements Visitor {
    private final int depth;

    public SetDepthVisitor(int depth) {
        this.depth = depth;
    }

    @Override
    public void visit(TranspositionTableRoot transpositionTableRoot) {
        transpositionTableRoot.setDepth(depth);
    }

    @Override
    public void visit(AlphaBetaFlowControl alphaBetaFlowControl) {
        alphaBetaFlowControl.setDepth(depth);
    }

    @Override
    public void visit(TranspositionTable transpositionTable) {
        transpositionTable.setDepth(depth);
    }

    @Override
    public void visit(TranspositionTableQ transpositionTableQ) {
        transpositionTableQ.setDepth(depth);
    }

    @Override
    public void visit(PrintTxtDebugListener printTxtDebugListener) {
        printTxtDebugListener.setDepth(depth);
    }

    @Override
    public void visit(NodeCounters nodeCounters) {
        nodeCounters.setDepth(depth);
    }


    @Override
    public void visit(QuiescenceNodeVisited quiescenceNodeVisited) {
        quiescenceNodeVisited.setDepth(depth);
    }

    @Override
    public void visit(LeafNodeStatistics leafNodeStatistics) {
        leafNodeStatistics.setDepth(depth);
    }

    @Override
    public void visit(LoopNodeStatistics loopNodeStatistics) {
        loopNodeStatistics.setDepth(depth);
    }

    @Override
    public void visit(EgtbNodeStatistics egtbNodeStatistics) {
        egtbNodeStatistics.setDepth(depth);
    }

    @Override
    public void visit(TerminalNodeStatistics terminalNodeStatistics) {
        terminalNodeStatistics.setDepth(depth);
    }

    @Override
    public void visit(DepthCollector depthCollector) {
        depthCollector.setDepth(depth);
    }

    @Override
    public void visit(DebugFilter debugFilter) {
        debugFilter.setDepth(depth);
    }
}
