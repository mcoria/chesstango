package net.chesstango.search.smart.alphabeta.listeners;

import net.chesstango.search.Acceptor;
import net.chesstango.search.SearchResult;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.SearchByDepthListener;

import java.time.Duration;
import java.time.Instant;

/**
 * @author Mauricio Coria
 */
public class SetSearchTimers implements Acceptor, SearchByCycleListener, SearchByDepthListener {
    private Instant startInstant;
    private Instant startDepthInstant;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch() {
        this.startInstant = Instant.now();
    }

    @Override
    public void afterSearch(SearchResult result) {
        result.setTimeSearching(Duration.between(startInstant, Instant.now()).toMillis());
    }

    @Override
    public void beforeSearchByDepth() {
        startDepthInstant = Instant.now();
    }

    public long getTimeSearching() {
        return Duration.between(startInstant, Instant.now()).toMillis();
    }

    public long getTimeSearchingLastDepth() {
        return Duration.between(startDepthInstant, Instant.now()).toMillis();
    }
}
