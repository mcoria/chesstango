package net.chesstango.search.smart.evaluator.visitors;

import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.core.filters.QuiescenceStandingPat;
import net.chesstango.search.smart.evaluator.filters.AlphaBetaEvaluation;
import net.chesstango.search.smart.pv.model.PVCalculatorTriangular;
import net.chesstango.search.smart.quiescence.Quiescence;

/**
 * @author Mauricio Coria
 */
public class LinkEvaluatorVisitor implements Visitor {

    private final Evaluator evaluator;

    public LinkEvaluatorVisitor(Evaluator evaluator) {
        this.evaluator = evaluator;
    }

    @Override
    public void visit(Quiescence quiescence) {
        quiescence.setEvaluator(evaluator);
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
    public void visit(PVCalculatorTriangular setTrianglePV) {
        setTrianglePV.setEvaluator(evaluator);
    }
}
