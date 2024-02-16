package net.chesstango.search.smart.alphabeta.debug;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;

/**
 * @author Mauricio Coria
 */

public class TrapEvaluation implements GameEvaluator, SearchByCycleListener {

    @Getter
    @Setter
    private SearchTracker searchTracker;

    @Getter
    @Setter
    private GameEvaluator gameEvaluator;

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.searchTracker = context.getSearchTracker();
    }


    @Override
    public int evaluate() {
        int evaluation = gameEvaluator.evaluate();
        searchTracker.trackEvaluation(evaluation);
        return evaluation;
    }

    @Override
    public void setGame(Game game) {
        throw new RuntimeException("Do not invoke this method");
    }
}
