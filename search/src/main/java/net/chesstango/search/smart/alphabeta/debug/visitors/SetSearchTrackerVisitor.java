package net.chesstango.search.smart.alphabeta.debug.visitors;

import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.debug.SearchTracker;
import net.chesstango.search.smart.alphabeta.debug.filters.DebugFilter;
import net.chesstango.search.smart.alphabeta.debug.listeners.SetDebugOutput;
import net.chesstango.search.smart.alphabeta.evaluator.EvaluatorCacheDebug;
import net.chesstango.search.smart.alphabeta.evaluator.EvaluatorDebug;
import net.chesstango.search.smart.alphabeta.killermoves.KillerMovesDebug;
import net.chesstango.search.smart.alphabeta.pv.TTPVReaderDebug;
import net.chesstango.search.smart.alphabeta.transposition.TTableDebug;
import net.chesstango.search.smart.sorters.MoveSorterDebug;

/**
 *
 * @author Mauricio Coria
 */
public class SetSearchTrackerVisitor implements Visitor {
    private final SearchTracker searchTracker;

    public SetSearchTrackerVisitor(SearchTracker searchTracker) {
        this.searchTracker = searchTracker;
    }

    @Override
    public void visit(DebugFilter debugFilter) {
        debugFilter.setSearchTracker(searchTracker);
    }

    @Override
    public void visit(MoveSorterDebug moveSorterDebug) {
        moveSorterDebug.setSearchTracker(searchTracker);
    }

    @Override
    public void visit(SetDebugOutput setDebugOutput) {
        setDebugOutput.setSearchTracker(searchTracker);
    }

    @Override
    public void visit(KillerMovesDebug killerMovesDebug) {
        killerMovesDebug.setSearchTracker(searchTracker);
    }

    @Override
    public void visit(TTableDebug tableDebug) {
        tableDebug.setSearchTracker(searchTracker);
    }

    @Override
    public void visit(EvaluatorCacheDebug evaluatorCacheDebug) {
        evaluatorCacheDebug.setSearchTracker(searchTracker);
    }

    @Override
    public void visit(EvaluatorDebug evaluatorDebug) {
        evaluatorDebug.setSearchTracker(searchTracker);
    }

    @Override
    public void visit(TTPVReaderDebug ttpvReaderDebug) {
        ttpvReaderDebug.setSearchTracker(searchTracker);
    }

}
