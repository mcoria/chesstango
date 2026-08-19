package net.chesstango.search.smart.alphabeta.transposition;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.debug.DebugNodeTracker;
import net.chesstango.search.smart.alphabeta.debug.model.DebugNode;
import net.chesstango.search.smart.alphabeta.debug.model.DebugOperationTT;

import java.util.List;
import java.util.Optional;

/**
 * @author Mauricio Coria
 */
public class TTablePVDebug implements TTable, Acceptor {

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
        throw new RuntimeException("Save shold not be called on TTableComparatorDebug");
    }


    void trackReadTranspositionEntry(long hashRequested, TranspositionEntry entry) {
        DebugNode currentNode = debugNodeTracker.getCurrentNode();

        List<DebugOperationTT> readList = currentNode.getPvReads();

        Optional<DebugOperationTT> previousReadOpt = readList
                .stream()
                .filter(debugOperation -> debugOperation.getEntry().getHash() == hashRequested)
                .findFirst();

        if (previousReadOpt.isEmpty()) {

            TranspositionEntry entryRead = entry.clone();

            readList.add(new DebugOperationTT()
                    .setEntry(entryRead));
        }
    }
}
