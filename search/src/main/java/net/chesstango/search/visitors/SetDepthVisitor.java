package net.chesstango.search.visitors;

import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFlowControl;
import net.chesstango.search.smart.features.debug.listeners.SetDebugOutput;
import net.chesstango.search.smart.features.pv.TTPVReader;
import net.chesstango.search.smart.features.statistics.node.filters.QuiescenceStatisticsExpected;
import net.chesstango.search.smart.features.statistics.node.filters.QuiescenceStatisticsVisited;
import net.chesstango.search.smart.features.transposition.filters.TranspositionTable;
import net.chesstango.search.smart.features.transposition.filters.TranspositionTableQ;
import net.chesstango.search.smart.features.transposition.filters.TranspositionTableRoot;


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
    public void visit(TTPVReader ttpvReader) {
        ttpvReader.setDepth(depth);
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
    public void visit(QuiescenceStatisticsExpected quiescenceStatisticsExpected) {
        quiescenceStatisticsExpected.setDepth(depth);
    }

    @Override
    public void visit(QuiescenceStatisticsVisited quiescenceStatisticsVisited) {
        quiescenceStatisticsVisited.setDepth(depth);
    }

    @Override
    public void visit(SetDebugOutput setDebugOutput) {
        setDebugOutput.setDepth(depth);
    }

}
