package net.chesstango.search.smart.transposition;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.debug.DebugNodeTracker;
import net.chesstango.search.smart.debug.model.DebugNode;

import java.util.List;
import java.util.Optional;

/**
 * @author Mauricio Coria
 */
public class TTableNodeDebug implements TTable, Acceptor {

    @Setter
    @Getter
    private TTable tTable;

    @Setter
    private DebugNodeTracker debugNodeTracker;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean load(long hash, TranspositionEntry entry) {
        boolean load = tTable.load(hash, entry);
        if (load) {
            trackReadTranspositionEntry(hash, entry);
        }
        return load;
    }

    @Override
    public void save(TranspositionEntry entry) {
        tTable.save(entry);
        trackWriteTranspositionEntry(entry);
    }


    void trackReadTranspositionEntry(long hashRequested, TranspositionEntry entry) {
        DebugNode currentNode = debugNodeTracker.getCurrentNode();

        List<TranspositionEntry> readList = currentNode.getNodeReads();

        Optional<TranspositionEntry> previousReadOpt = readList
                .stream()
                .filter(entryRead -> entryRead.getHash() == hashRequested)
                .findFirst();

        if (previousReadOpt.isEmpty()) {

            TranspositionEntry entryRead = entry.clone();

            readList.add(entryRead);
        }
    }

    void trackWriteTranspositionEntry(TranspositionEntry entry) {
        DebugNode currentNode = debugNodeTracker.getCurrentNode();

        TranspositionEntry entryWrite = entry.clone();

        List<TranspositionEntry> writeList = currentNode.getNodeWrites();

        writeList.add(entryWrite);

    }
}
