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
 *
 * @author Mauricio Coria
 */
public class SetSearchMaxPlyVisitor implements Visitor {
    private final int maxPly;

    public SetSearchMaxPlyVisitor(int maxPly) {
        this.maxPly = maxPly;
    }

    /**
     * Facades
     */
    @Override
    public void visit(TranspositionTableRoot transpositionTableRoot) {
        transpositionTableRoot.setMaxPly(maxPly);
    }

    @Override
    public void visit(TTPVReader ttpvReader) {
        ttpvReader.setMaxPly(maxPly);
    }

    @Override
    public void visit(AlphaBetaFlowControl alphaBetaFlowControl) {
        alphaBetaFlowControl.setMaxPly(maxPly);
    }

    @Override
    public void visit(TranspositionTable transpositionTable) {
        transpositionTable.setMaxPly(maxPly);
    }

    @Override
    public void visit(TranspositionTableQ transpositionTableQ) {
        transpositionTableQ.setMaxPly(maxPly);
    }

    @Override
    public void visit(QuiescenceStatisticsExpected quiescenceStatisticsExpected) {
        quiescenceStatisticsExpected.setMaxPly(maxPly);
    }

    @Override
    public void visit(QuiescenceStatisticsVisited quiescenceStatisticsVisited) {
        quiescenceStatisticsVisited.setMaxPly(maxPly);
    }

    @Override
    public void visit(SetDebugOutput setDebugOutput) {
        setDebugOutput.setMaxPly(maxPly);
    }

}
