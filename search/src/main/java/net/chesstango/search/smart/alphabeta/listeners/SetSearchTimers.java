package net.chesstango.search.smart.alphabeta.listeners;

import net.chesstango.search.SearchResult;
import net.chesstango.search.SearchResultByDepth;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.SearchByDepthContext;
import net.chesstango.search.smart.SearchByDepthListener;

import java.time.Duration;
import java.time.Instant;

/**
 * @author Mauricio Coria
 */
public class SetSearchTimers implements SearchByCycleListener, SearchByDepthListener {
    private Instant startInstant;
    private Instant startDepthInstant;

    @Override
    public void beforeSearch() {
        this.startInstant = Instant.now();
    }

    @Override
    public void afterSearch(SearchResult result) {
        result.setTimeSearching(Duration.between(startInstant, Instant.now()).toMillis());
    }

    @Override
    public void beforeSearchByDepth(SearchByDepthContext context) {
        startDepthInstant = Instant.now();
    }

    @Override
    public void afterSearchByDepth(SearchResultByDepth searchResultByDepth) {
        searchResultByDepth.setTimeSearching(Duration.between(startInstant, Instant.now()).toMillis());
        searchResultByDepth.setTimeSearchingLastDepth(Duration.between(startDepthInstant, Instant.now()).toMillis());
    }
}
