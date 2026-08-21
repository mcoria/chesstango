package net.chesstango.search.smart.debug.visitors;

import net.chesstango.search.Visitor;
import net.chesstango.search.smart.debug.DebugNodeTracker;
import net.chesstango.search.smart.debug.filters.DebugFilter;
import net.chesstango.search.smart.evaluator.EvaluatorCacheDebug;
import net.chesstango.search.smart.evaluator.EvaluatorDebug;
import net.chesstango.search.smart.killermoves.KillerMovesDebug;
import net.chesstango.search.smart.transposition.TTableComparatorDebug;
import net.chesstango.search.smart.transposition.TTableNodeDebug;
import net.chesstango.search.smart.transposition.TTablePVDebug;
import net.chesstango.search.sorters.MoveSorterDebug;

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
    public void visit(TTableNodeDebug tTableNodeDebug) {
        tTableNodeDebug.setDebugNodeTracker(debugNodeTracker);
    }

    @Override
    public void visit(TTableComparatorDebug tTableComparatorDebug) {
        tTableComparatorDebug.setDebugNodeTracker(debugNodeTracker);
    }

    @Override
    public void visit(TTablePVDebug tTablePVDebug) {
        tTablePVDebug.setDebugNodeTracker(debugNodeTracker);
    }

    @Override
    public void visit(EvaluatorCacheDebug evaluatorCacheDebug) {
        evaluatorCacheDebug.setDebugNodeTracker(debugNodeTracker);
    }

    @Override
    public void visit(EvaluatorDebug evaluatorDebug) {
        evaluatorDebug.setDebugNodeTracker(debugNodeTracker);
    }

}
