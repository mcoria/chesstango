package net.chesstango.search.smart.transposition;

import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.statistics.GameStatistics;

import java.io.PrintStream;

/**
 * @author Mauricio Coria
 */
public class TTableDebug implements TTable, SearchByCycleListener {

    private final TTable tTable;
    private PrintStream debugOut;

    public TTableDebug(TTable tTable){
        this.tTable = tTable;
    }

    @Override
    public TranspositionEntry read(long hash) {
        return tTable.read(hash);
    }

    @Override
    public TranspositionEntry write(long hash, int searchDepth, long movesAndValue, TranspositionBound bound) {
        debugOut.print(" WriteTT");
        return tTable.write(hash, searchDepth, movesAndValue, bound);
    }

    @Override
    public void clear() {
        tTable.clear();
    }

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.debugOut = context.getDebugOut();
    }

    @Override
    public void afterSearch() {
    }
}
