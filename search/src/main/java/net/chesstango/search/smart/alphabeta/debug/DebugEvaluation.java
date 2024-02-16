package net.chesstango.search.smart.alphabeta.debug;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.evaluation.GameEvaluatorCacheRead;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;

/**
 * @author Mauricio Coria
 */

public class DebugEvaluation implements GameEvaluatorCacheRead, SearchByCycleListener {

    @Getter
    @Setter
    private SearchTracker searchTracker;

    @Getter
    @Setter
    private GameEvaluatorCacheRead gameEvaluatorCacheRead;

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.searchTracker = context.getSearchTracker();
    }

    @Override
    public Integer readFromCache(long hash) {
        Integer evaluation = gameEvaluatorCacheRead.readFromCache(hash);
        if (evaluation != null) {
            searchTracker.trackReadFromCache(hash, evaluation);
        }
        return evaluation;
    }
}
