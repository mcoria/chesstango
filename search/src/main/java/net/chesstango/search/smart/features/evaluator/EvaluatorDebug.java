package net.chesstango.search.smart.features.evaluator;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.features.debug.SearchTracker;
import net.chesstango.search.smart.features.debug.model.DebugNode;

/**
 * @author Mauricio Coria
 */

public class EvaluatorDebug implements Evaluator, SearchByCycleListener {

    @Getter
    @Setter
    private SearchTracker searchTracker;

    @Getter
    @Setter
    private Evaluator evaluator;

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.searchTracker = context.getSearchTracker();
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
