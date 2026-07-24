package net.chesstango.search.smart.alphabeta.root.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.Acceptor;
import net.chesstango.search.RootMoveEvaluation;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchAlgorithm;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.root.RootMoveEvaluationCollection;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaFacade implements SearchAlgorithm, Acceptor {

    @Setter
    @Getter
    private AlphaBetaFilter next;


    @Setter
    private RootMoveEvaluationCollection rootMoveEvaluationCollection;


    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void search() {
        int bestValue = next.alphaBeta(0, Evaluator.INFINITE_NEGATIVE, Evaluator.INFINITE_POSITIVE);

        RootMoveEvaluation bestRootMoveEvaluation = rootMoveEvaluationCollection.getBestRootMoveEvaluation();

        if (bestValue != bestRootMoveEvaluation.evaluation()) {
            throw new RuntimeException("Best value is not the same as the best root move evaluation");
        }
    }

}
