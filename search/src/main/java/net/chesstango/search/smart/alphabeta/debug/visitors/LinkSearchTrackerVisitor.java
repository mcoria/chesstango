package net.chesstango.search.smart.alphabeta.debug.visitors;

import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.debug.DebugNodeTracker;
import net.chesstango.search.smart.alphabeta.debug.filters.DebugFilter;
import net.chesstango.search.smart.alphabeta.evaluator.EvaluatorCacheDebug;
import net.chesstango.search.smart.alphabeta.evaluator.EvaluatorDebug;
import net.chesstango.search.smart.alphabeta.killermoves.KillerMovesDebug;
import net.chesstango.search.smart.alphabeta.pv.model.PVCalculatorDebug;
import net.chesstango.search.smart.alphabeta.transposition.TTableDebug;
import net.chesstango.search.smart.sorters.MoveSorterDebug;

/**
 *
 * @author Mauricio Coria
 */
public class LinkSearchTrackerVisitor implements Visitor {
    private final DebugNodeTracker debugNodeTracker;

    public LinkSearchTrackerVisitor(DebugNodeTracker debugNodeTracker) {
        this.debugNodeTracker = debugNodeTracker;
    }

    @Override
    public void visit(DebugFilter debugFilter) {
        debugFilter.setDebugNodeTracker(debugNodeTracker);
    }

    @Override
    public void visit(MoveSorterDebug moveSorterDebug) {
        moveSorterDebug.setDebugNodeTracker(debugNodeTracker);
    }

    @Override
    public void visit(KillerMovesDebug killerMovesDebug) {
        killerMovesDebug.setDebugNodeTracker(debugNodeTracker);
    }

    @Override
    public void visit(TTableDebug tableDebug) {
        tableDebug.setDebugNodeTracker(debugNodeTracker);
    }

    @Override
    public void visit(EvaluatorCacheDebug evaluatorCacheDebug) {
        evaluatorCacheDebug.setDebugNodeTracker(debugNodeTracker);
    }

    @Override
    public void visit(EvaluatorDebug evaluatorDebug) {
        evaluatorDebug.setDebugNodeTracker(debugNodeTracker);
    }

    @Override
    public void visit(PVCalculatorDebug pvCalculatorDebug) {
        pvCalculatorDebug.setDebugNodeTracker(debugNodeTracker);
    }

}
