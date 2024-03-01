package net.chesstango.search.smart.alphabeta.debug;

import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.alphabeta.debug.model.DebugNode;
import net.chesstango.search.smart.alphabeta.debug.model.DebugOperationTT;
import net.chesstango.search.smart.transposition.TTable;
import net.chesstango.search.smart.transposition.TranspositionBound;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import java.util.List;
import java.util.Optional;

/**
 * @author Mauricio Coria
 */
public class TrapTranspositionAccess implements TTable, SearchByCycleListener {

    private final TTable tTable;
    private final DebugOperationTT.TableType tableType;
    private SearchTracker searchTracker;

    public TrapTranspositionAccess(DebugOperationTT.TableType tableType, TTable tTable) {
        this.tableType = tableType;
        this.tTable = tTable;
    }

    @Override
    public TranspositionEntry read(long hash) {
        TranspositionEntry entry = tTable.read(hash);
        trackReadTranspositionEntry(hash, entry);
        return entry;
    }

    @Override
    public TranspositionEntry write(long hash, int searchDepth, long movesAndValue, TranspositionBound bound) {
        trackWriteTranspositionEntry(hash, searchDepth, movesAndValue, bound);
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

    public void trackReadTranspositionEntry(long hashRequested, TranspositionEntry entry) {
        DebugNode currentNode = searchTracker.getCurrentNode();
        if (currentNode != null) {
            if (entry != null) {
                assert hashRequested == entry.hash;
                TranspositionEntry entryCloned = entry.clone();

                List<DebugOperationTT> readList = searchTracker.isSorting() ? currentNode.getSorterReads() : currentNode.getEntryRead();

                Optional<DebugOperationTT> previousReadOpt = readList
                        .stream()
                        .filter(debugOperation -> debugOperation.getTableType().equals(tableType) && debugOperation.getEntry().getHash() == hashRequested)
                        .findFirst();

                if (previousReadOpt.isEmpty()) {
                    readList.add(new DebugOperationTT()
                            .setTableType(tableType)
                            .setEntry(entryCloned));
                }
            }
        }
    }

    public void trackWriteTranspositionEntry(long hash, int searchDepth, long movesAndValue, TranspositionBound transpositionBound) {
        DebugNode currentNode = searchTracker.getCurrentNode();
        if (currentNode != null) {
            if (searchTracker.isSorting()) {
                throw new RuntimeException("Writing TT while sorting");
            } else {
                TranspositionEntry entryWrite = new TranspositionEntry()
                        .setHash(hash)
                        .setSearchDepth(searchDepth)
                        .setMovesAndValue(movesAndValue)
                        .setTranspositionBound(transpositionBound);

                currentNode.getEntryWrite().add(new DebugOperationTT()
                        .setTableType(tableType)
                        .setEntry(entryWrite));
            }
        }
    }
}
