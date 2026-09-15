package net.chesstango.search.alphabeta.evaluator.visitors;

import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.Visitor;
import net.chesstango.search.alphabeta.quiescence.QuiescenceStandingPat;
import net.chesstango.search.alphabeta.evaluator.filters.AlphaBetaEvaluation;
import net.chesstango.search.alphabeta.pv.model.PVCalculator;

/**
 * @author Mauricio Coria
 */
public class LinkEvaluatorVisitor implements Visitor {

    private final Evaluator evaluator;

    public LinkEvaluatorVisitor(Evaluator evaluator) {
        this.evaluator = evaluator;
    }

    @Override
    public void visit(QuiescenceStandingPat quiescenceStandingPat) {
        quiescenceStandingPat.setEvaluator(evaluator);
    }

    @Override
    public void visit(AlphaBetaEvaluation alphaBetaEvaluation) {
        alphaBetaEvaluation.setEvaluator(evaluator);
    }

    @Override
    public void visit(PVCalculator setTrianglePV) {
        setTrianglePV.setEvaluator(evaluator);
    }
}
