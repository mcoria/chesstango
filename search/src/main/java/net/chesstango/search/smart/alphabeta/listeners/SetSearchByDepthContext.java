package net.chesstango.search.smart.alphabeta.listeners;

import net.chesstango.search.SearchByDepthResult;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.SearchByDepthContext;
import net.chesstango.search.smart.SearchByDepthListener;

import java.time.Duration;
import java.time.Instant;

/**
 * @author Mauricio Coria
 */
public class SetSearchByDepthContext implements SearchByCycleListener, SearchByDepthListener {
    private SearchByDepthResult lastSearchByDepthResult;
    private Instant startInstant;
    private Instant startDepthInstant;

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.lastSearchByDepthResult = null;
        this.startInstant = Instant.now();
    }


    @Override
    public void beforeSearchByDepth(SearchByDepthContext context) {
        startDepthInstant = Instant.now();
        if (lastSearchByDepthResult != null) {
            context.setLastBestMoveEvaluation(lastSearchByDepthResult.getBestMoveEvaluation());
            context.setLastMoveEvaluations(lastSearchByDepthResult.getMoveEvaluations());
        }
    }

    @Override
    public void afterSearchByDepth(SearchByDepthResult searchByDepthResult) {
        this.lastSearchByDepthResult = searchByDepthResult;
        searchByDepthResult.setTimeSearching(Duration.between(startInstant, Instant.now()).toMillis());
        searchByDepthResult.setTimeSearchingLastDepth(Duration.between(startDepthInstant, Instant.now()).toMillis());
    }
}
