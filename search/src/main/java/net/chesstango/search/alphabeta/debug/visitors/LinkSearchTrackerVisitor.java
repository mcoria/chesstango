package net.chesstango.search.alphabeta.debug.visitors;

import net.chesstango.search.Visitor;
import net.chesstango.search.alphabeta.debug.DebugNodeTracker;
import net.chesstango.search.alphabeta.debug.filters.DebugFilter;
import net.chesstango.search.alphabeta.evalcache.EvaluatorCacheDebug;
import net.chesstango.search.alphabeta.evaluator.EvaluatorDebug;
import net.chesstango.search.alphabeta.killermoves.KillerMovesDebug;
import net.chesstango.search.alphabeta.transposition.TTableComparatorHeadDebug;
import net.chesstango.search.alphabeta.transposition.TTableComparatorTailDebug;
import net.chesstango.search.alphabeta.transposition.TTableNodeDebug;
import net.chesstango.search.alphabeta.transposition.TTablePVDebug;
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
    public void visit(TTableComparatorHeadDebug tTableComparatorHeadDebug) {
        tTableComparatorHeadDebug.setDebugNodeTracker(debugNodeTracker);
    }

    @Override
    public void visit(TTableComparatorTailDebug tTableComparatorTailDebug) {
        tTableComparatorTailDebug.setDebugNodeTracker(debugNodeTracker);
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
