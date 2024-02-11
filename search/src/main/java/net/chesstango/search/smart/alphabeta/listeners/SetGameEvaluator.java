package net.chesstango.search.smart.alphabeta.listeners;

import lombok.Setter;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;

/**
 * @author Mauricio Coria
 */
public class SetGameEvaluator implements SearchByCycleListener {

    @Setter
    private GameEvaluator gameEvaluator;

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        gameEvaluator.setGame(context.getGame());
    }

}
