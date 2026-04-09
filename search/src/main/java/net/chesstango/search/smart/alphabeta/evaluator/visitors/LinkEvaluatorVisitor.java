package net.chesstango.search.smart.alphabeta.evaluator.visitors;

import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.evaluator.filters.AlphaBetaEvaluation;
import net.chesstango.search.smart.alphabeta.quiescence.Quiescence;
import net.chesstango.search.smart.alphabeta.evaluator.EvaluatorDebug;
import net.chesstango.search.smart.alphabeta.pv.TranspositionPVReader;
import net.chesstango.search.smart.alphabeta.pv.TrianglePVReader;

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
    public void visit(EvaluatorDebug evaluatorDebug) {
        evaluatorDebug.setEvaluator(evaluator);
    }

    @Override
    public void visit(AlphaBetaEvaluation alphaBetaEvaluation) {
        alphaBetaEvaluation.setEvaluator(evaluator);
    }

    @Override
    public void visit(TranspositionPVReader ttPVReader) {
        ttPVReader.setEvaluator(evaluator);
    }

    @Override
    public void visit(TrianglePVReader setTrianglePV) {
        setTrianglePV.setEvaluator(evaluator);
    }
}
