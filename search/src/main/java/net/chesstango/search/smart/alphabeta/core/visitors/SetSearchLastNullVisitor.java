package net.chesstango.search.smart.alphabeta.core.visitors;

import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.core.filters.once.AspirationWindows;
import net.chesstango.search.smart.alphabeta.core.filters.once.StopProcessingCatch;
import net.chesstango.search.smart.alphabeta.pv.comparators.PrincipalVariationComparator;
import net.chesstango.search.smart.sorters.RootMoveSorter;

/**
 *
 * @author Mauricio Coria
 */
public class SetSearchLastNullVisitor implements Visitor {

    @Override
    public void visit(AspirationWindows aspirationWindows) {
        aspirationWindows.setLastBestValue(null);
    }

    @Override
    public void visit(StopProcessingCatch stopProcessingCatch) {
        stopProcessingCatch.setLastBestMoveEvaluation(null);
    }

    @Override
    public void visit(RootMoveSorter rootMoveSorter) {
        rootMoveSorter.setLastMoveEvaluations(null);
        rootMoveSorter.setLastBestMove(null);
    }

    @Override
    public void visit(PrincipalVariationComparator principalVariationComparator) {
        principalVariationComparator.setLastPrincipalVariation(null);
    }
}
