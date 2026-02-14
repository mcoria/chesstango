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
 * La profundidad que busca en este ciclo
 *
 * @author Mauricio Coria
 */
public class SetDepthVisitor implements Visitor {
    private final int depth;

    public SetDepthVisitor(int depth) {
        this.depth = depth;
    }

    /**
     * Facades
     */
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
