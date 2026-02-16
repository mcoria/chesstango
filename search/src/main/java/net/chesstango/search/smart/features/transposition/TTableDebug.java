package net.chesstango.search.smart.features.transposition;

import lombok.Getter;
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

    private final DebugOperationTT.TableType tableType;

    @Setter
    @Getter
    private TTable tTable;

    @Setter
    private SearchTracker searchTracker;

    public TTableDebug(DebugOperationTT.TableType tableType) {
        this.tableType = tableType;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean load(long hash, TranspositionEntry entry) {
        boolean load = tTable.load(hash, entry);
        trackReadTranspositionEntry(hash, load ? entry : null);
        return load;
    }

    @Override
    public InsertResult save(TranspositionEntry entry) {
        InsertResult insertResult = tTable.save(entry);
        trackWriteTranspositionEntry(entry, insertResult);
        return insertResult;
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

                TranspositionEntry entryRead = entry.clone();

                List<DebugOperationTT> readList = currentNode.getCurrentEntryRead();

                Optional<DebugOperationTT> previousReadOpt = readList
                        .stream()
                        .filter(debugOperation -> debugOperation.getTableType().equals(tableType) && debugOperation.getEntry().getHash() == hashRequested)
                        .findFirst();

                if (previousReadOpt.isEmpty()) {
                    readList.add(new DebugOperationTT()
                            .setTableType(tableType)
                            .setEntry(entryRead));
                }
            }
        }
    }

    void trackWriteTranspositionEntry(TranspositionEntry entry, InsertResult insertResult) {
        DebugNode currentNode = searchTracker.getCurrentNode();
        if (currentNode != null) {
            // Si intenta grabar mientras esta ordenando lanza NULLPOINTER
            TranspositionEntry entryWrite = entry.clone();

            List<DebugOperationTT> writeList = currentNode.getCurrentEntryWrite();

            writeList.add(new DebugOperationTT()
                    .setTableType(tableType)
                    .setEntry(entryWrite));

        }
    }
}
