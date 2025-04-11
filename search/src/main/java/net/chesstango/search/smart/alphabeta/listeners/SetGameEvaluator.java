package net.chesstango.search.smart.alphabeta.listeners;

import lombok.Setter;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;

/**
 * @author Mauricio Coria
 */
@Setter
public class SetGameEvaluator implements SearchByCycleListener {

    private Evaluator evaluator;

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        evaluator.setGame(context.getGame());
    }

}
