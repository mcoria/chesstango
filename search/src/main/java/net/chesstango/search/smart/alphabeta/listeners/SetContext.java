package net.chesstango.search.smart.alphabeta.listeners;

import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.SearchByDepthContext;
import net.chesstango.search.smart.SearchByDepthListener;

/**
 * @author Mauricio Coria
 */
public class SetContext implements SearchByCycleListener, SearchByDepthListener {
    private SearchMoveResult lastMoveResult;

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.lastMoveResult = null;
    }

    @Override
    public void afterSearch() {

    }

    @Override
    public void beforeSearchByDepth(SearchByDepthContext context) {
        if (lastMoveResult != null) {
            context.setLastBestMove(lastMoveResult.getBestMove());
            context.setLastBestEvaluation(lastMoveResult.getEvaluation());
            context.setLastMoveEvaluations(lastMoveResult.getMoveEvaluations());
        }
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {
        this.lastMoveResult = result;
    }
}
