package net.chesstango.search.smart.alphabeta.listeners;

import net.chesstango.search.SearchByDepthResult;
import net.chesstango.search.SearchResult;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.SearchByDepthContext;
import net.chesstango.search.smart.SearchByDepthListener;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;

/**
 * @author Mauricio Coria
 */
public class SetSearchContext implements SearchByCycleListener, SearchByDepthListener {
    private SearchByDepthResult lastSearchByDepthResult;
    private Instant startInstant;
    private Instant startDepthInstant;

    /**
     * Cuantas veces se busca por profundidad
     */
    private int searchByDepthCounter;

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.lastSearchByDepthResult = null;
        this.startInstant = Instant.now();
        this.searchByDepthCounter = 0;
    }

    @Override
    public void afterSearch(SearchResult result) {
        result.setTimeSearching(Duration.between(startInstant, Instant.now()).toMillis());
        result.setSearchByDepthCounter(searchByDepthCounter);
    }

    @Override
    public void beforeSearchByDepth(SearchByDepthContext context) {
        startDepthInstant = Instant.now();
        searchByDepthCounter++;
        if (lastSearchByDepthResult != null) {
            context.setLastBestMoveEvaluation(lastSearchByDepthResult.getBestMoveEvaluation());
            context.setLastMoveEvaluations(lastSearchByDepthResult.getMoveEvaluations());
            context.setLastPrincipalVariation(new LinkedList<>(lastSearchByDepthResult.getPrincipalVariation()));
        }
    }

    @Override
    public void afterSearchByDepth(SearchByDepthResult searchByDepthResult) {
        lastSearchByDepthResult = searchByDepthResult;
        searchByDepthResult.setTimeSearching(Duration.between(startInstant, Instant.now()).toMillis());
        searchByDepthResult.setTimeSearchingLastDepth(Duration.between(startDepthInstant, Instant.now()).toMillis());
    }
}
