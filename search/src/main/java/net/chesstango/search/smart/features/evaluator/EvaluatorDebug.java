package net.chesstango.search.smart.features.evaluator;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.features.debug.SearchTracker;
import net.chesstango.search.smart.features.debug.model.DebugNode;

/**
 * @author Mauricio Coria
 */

@Setter
@Getter
public class EvaluatorDebug implements Evaluator, Acceptor {

    private SearchTracker searchTracker;

    private Evaluator evaluator;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int evaluate() {
        int evaluation = evaluator.evaluate();
        trackEvaluation(evaluation);
        return evaluation;
    }

    public void trackEvaluation(int evaluation) {
        DebugNode currentNode = searchTracker.getCurrentNode();
        if (currentNode != null) {
            currentNode.setStandingPat(evaluation);
        }
    }

    @Override
    public void setGame(Game game) {
        throw new RuntimeException("Do not invoke this method");
    }
}
