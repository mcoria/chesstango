package net.chesstango.search.smart.alphabeta.debug;

import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.transposition.TTable;
import net.chesstango.search.smart.transposition.TranspositionBound;
import net.chesstango.search.smart.transposition.TranspositionEntry;

/**
 * @author Mauricio Coria
 */
public class DebugTT implements TTable, SearchByCycleListener {

    private final TTable tTable;
    private final String tableName;
    private SearchTracker searchTracker;

    public DebugTT(String tableName, TTable tTable) {
        this.tableName = tableName;
        this.tTable = tTable;
    }

    @Override
    public TranspositionEntry read(long hash) {
        TranspositionEntry entry = tTable.read(hash);
        searchTracker.trackReadTranspositionEntry(tableName, hash, entry);
        return entry;
    }

    @Override
    public TranspositionEntry write(long hash, int searchDepth, long movesAndValue, TranspositionBound bound) {
        searchTracker.trackWriteTranspositionEntry(tableName, hash, searchDepth, movesAndValue, bound);
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
