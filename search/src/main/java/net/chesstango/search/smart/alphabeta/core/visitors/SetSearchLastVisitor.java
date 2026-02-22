package net.chesstango.search.smart.alphabeta.core.visitors;

import net.chesstango.search.MoveEvaluation;
import net.chesstango.search.PrincipalVariation;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.core.filters.once.AspirationWindows;
import net.chesstango.search.smart.alphabeta.core.filters.once.StopProcessingCatch;
import net.chesstango.search.smart.alphabeta.pv.comparators.PrincipalVariationComparator;
import net.chesstango.search.smart.sorters.RootMoveSorter;

import java.util.List;

/**
 *
 * @author Mauricio Coria
 */
public class SetSearchLastVisitor implements Visitor {
    private final MoveEvaluation lastBestMoveEvaluation;

    private final List<MoveEvaluation> lastMoveEvaluations;

    private final List<PrincipalVariation> lastPrincipalVariation;

    public SetSearchLastVisitor(MoveEvaluation lastBestMoveEvaluation,
                                List<MoveEvaluation> lastMoveEvaluations,
                                List<PrincipalVariation> lastPrincipalVariation) {
        this.lastBestMoveEvaluation = lastBestMoveEvaluation;
        this.lastMoveEvaluations = lastMoveEvaluations;
        this.lastPrincipalVariation = lastPrincipalVariation;
    }

    @Override
    public void visit(AspirationWindows aspirationWindows) {
        aspirationWindows.setLastBestValue(lastBestMoveEvaluation.evaluation());
    }

    @Override
    public void visit(StopProcessingCatch stopProcessingCatch) {
        stopProcessingCatch.setLastBestMoveEvaluation(lastBestMoveEvaluation);
    }

    @Override
    public void visit(RootMoveSorter rootMoveSorter) {
        rootMoveSorter.setLastMoveEvaluations(lastMoveEvaluations);
        rootMoveSorter.setLastBestMove(lastBestMoveEvaluation.move());
    }

    @Override
    public void visit(PrincipalVariationComparator principalVariationComparator) {
        principalVariationComparator.setLastPrincipalVariation(lastPrincipalVariation);
    }
}
