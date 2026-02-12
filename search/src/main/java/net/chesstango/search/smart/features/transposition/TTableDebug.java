package net.chesstango.search.smart.features.transposition;

import lombok.Setter;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.features.debug.SearchTracker;
import net.chesstango.search.smart.features.debug.model.DebugNode;
import net.chesstango.search.smart.features.debug.model.DebugOperationTT;

import java.util.List;
import java.util.Optional;

/**
 * @author Mauricio Coria
 */
public class TTableDebug implements TTable, Acceptor {

    private final TTable tTable;
    private final DebugOperationTT.TableType tableType;

    @Setter
    private SearchTracker searchTracker;

    public TTableDebug(DebugOperationTT.TableType tableType, TTable tTable) {
        this.tableType = tableType;
        this.tTable = tTable;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public TranspositionEntry read(long hash) {
        TranspositionEntry entry = tTable.read(hash);
        trackReadTranspositionEntry(hash, entry);
        return entry;
    }

    @Override
    public TranspositionEntry write(long hash, TranspositionBound bound, int searchDepth, short move, int value) {
        trackWriteTranspositionEntry(hash, bound, searchDepth, move, value);
        return tTable.write(hash, bound, searchDepth, move, value);
    }

    @Override
    public void clear() {
        tTable.clear();
    }


    void trackReadTranspositionEntry(long hashRequested, TranspositionEntry entry) {
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

    void trackWriteTranspositionEntry(long hash, TranspositionBound transpositionBound, int searchDepth, short move, int value) {
        DebugNode currentNode = searchTracker.getCurrentNode();
        if (currentNode != null) {
            if (searchTracker.isSorting()) {
                throw new RuntimeException("Writing TT while sorting");
            } else {
                TranspositionEntry entryWrite = new TranspositionEntry()
                        .setHash(hash)
                        .setSearchDepth(searchDepth)
                        .setMove(move)
                        .setValue(value)
                        .setTranspositionBound(transpositionBound);

                currentNode.getEntryWrite().add(new DebugOperationTT()
                        .setTableType(tableType)
                        .setEntry(entryWrite));
            }
        }
    }
}
