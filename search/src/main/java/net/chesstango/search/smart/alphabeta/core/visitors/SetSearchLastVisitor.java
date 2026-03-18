package net.chesstango.search.smart.alphabeta.core.visitors;

import net.chesstango.search.RootChildEvaluation;
import net.chesstango.search.PrincipalVariation;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.root.filters.AspirationWindows;
import net.chesstango.search.smart.alphabeta.root.filters.StopProcessingCatch;
import net.chesstango.search.smart.alphabeta.pv.comparators.PrincipalVariationComparator;
import net.chesstango.search.smart.sorters.RootMoveSorter;

import java.util.List;

/**
 *
 * @author Mauricio Coria
 */
public class SetSearchLastVisitor implements Visitor {
    private final RootChildEvaluation lastRootChildEvaluation;

    private final List<RootChildEvaluation> lastRootChildEvaluations;

    private final List<PrincipalVariation> lastPrincipalVariations;

    public SetSearchLastVisitor(RootChildEvaluation lastRootChildEvaluation,
                                List<RootChildEvaluation> lastRootChildEvaluations,
                                List<PrincipalVariation> lastPrincipalVariations) {
        this.lastRootChildEvaluation = lastRootChildEvaluation;
        this.lastRootChildEvaluations = lastRootChildEvaluations;
        this.lastPrincipalVariations = lastPrincipalVariations;
    }

    @Override
    public void visit(AspirationWindows aspirationWindows) {
        aspirationWindows.setLastBestValue(lastRootChildEvaluation.evaluation());
    }

    @Override
    public void visit(StopProcessingCatch stopProcessingCatch) {
        stopProcessingCatch.setLastRootChildEvaluation(lastRootChildEvaluation);
    }

    @Override
    public void visit(RootMoveSorter rootMoveSorter) {
        rootMoveSorter.setLastRootChildEvaluations(lastRootChildEvaluations);
        rootMoveSorter.setLastRootChildEvaluation(lastRootChildEvaluation);
    }

    @Override
    public void visit(PrincipalVariationComparator principalVariationComparator) {
        principalVariationComparator.setLastPrincipalVariations(lastPrincipalVariations);
    }
}
