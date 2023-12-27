package net.chesstango.search.smart.transposition;

import lombok.Getter;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.SearchTracker;

/**
 * @author Mauricio Coria
 */
public class TTableDebug implements TTable, SearchByCycleListener {

    @Getter
    private final TTable tTable;
    private SearchTracker searchTracker;

    public TTableDebug(TTable tTable){
        this.tTable = tTable;
    }

    @Override
    public TranspositionEntry read(long hash) {
        TranspositionEntry entry = tTable.read(hash);
        searchTracker.trackReadTranspositionEntry(entry);
        return entry;
    }

    @Override
    public TranspositionEntry write(long hash, int searchDepth, long movesAndValue, TranspositionBound bound) {
        searchTracker.trackWriteTranspositionEntry(hash, searchDepth, movesAndValue, bound);
        return tTable.write(hash, searchDepth, movesAndValue, bound);
    }

    @Override
    public void clear() {
        tTable.clear();
    }

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.searchTracker = context.getSearchTracker();
    }

    @Override
    public void afterSearch() {
    }
}
