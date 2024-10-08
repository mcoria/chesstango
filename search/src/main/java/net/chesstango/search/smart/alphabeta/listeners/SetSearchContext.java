package net.chesstango.search.smart.alphabeta.listeners;

import net.chesstango.search.SearchResultByDepth;
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
    private SearchResultByDepth lastSearchResultByDepth;
    private Instant startInstant;
    private Instant startDepthInstant;

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.lastSearchResultByDepth = null;
        this.startInstant = Instant.now();
    }

    @Override
    public void afterSearch(SearchResult result) {
        result.setTimeSearching(Duration.between(startInstant, Instant.now()).toMillis());
    }

    @Override
    public void beforeSearchByDepth(SearchByDepthContext context) {
        startDepthInstant = Instant.now();
        if (lastSearchResultByDepth != null) {
            context.setLastBestMoveEvaluation(lastSearchResultByDepth.getBestMoveEvaluation());
            context.setLastMoveEvaluations(lastSearchResultByDepth.getMoveEvaluations());
            context.setLastPrincipalVariation(new LinkedList<>(lastSearchResultByDepth.getPrincipalVariation()));
        }
    }

    @Override
    public void afterSearchByDepth(SearchResultByDepth searchResultByDepth) {
        lastSearchResultByDepth = searchResultByDepth;
        searchResultByDepth.setTimeSearching(Duration.between(startInstant, Instant.now()).toMillis());
        searchResultByDepth.setTimeSearchingLastDepth(Duration.between(startDepthInstant, Instant.now()).toMillis());
    }
}
