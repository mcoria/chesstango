package net.chesstango.search.smart.features.evaluator.visitors;

import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.features.evaluator.filters.AlphaBetaEvaluation;
import net.chesstango.search.smart.alphabeta.filters.Quiescence;
import net.chesstango.search.smart.features.evaluator.listeners.SetGameToEvaluator;
import net.chesstango.search.smart.features.evaluator.EvaluatorDebug;
import net.chesstango.search.smart.features.pv.TTPVReader;
import net.chesstango.search.smart.features.pv.listeners.SetTrianglePV;

/**
 * @author Mauricio Coria
 */
public class SetEvaluatorVisitor implements Visitor {

    private final Evaluator evaluator;

    public SetEvaluatorVisitor(Evaluator evaluator) {
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
    public void visit(TTPVReader ttPVReader) {
        ttPVReader.setEvaluator(evaluator);
    }

    @Override
    public void visit(SetTrianglePV setTrianglePV) {
        setTrianglePV.setEvaluator(evaluator);
    }

    @Override
    public void visit(SetGameToEvaluator setGameToEvaluator) {
        setGameToEvaluator.setEvaluator(evaluator);
    }
}
