package net.chesstango.search.smart.transposition;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.debug.DebugNodeTracker;
import net.chesstango.search.smart.debug.model.DebugNode;
import net.chesstango.search.smart.debug.model.DebugPVReadTT;

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
        throw new RuntimeException("Save shold not be called on TTablePVDebug");
    }


    void trackReadTranspositionEntry(long hashRequested, TranspositionEntry entry) {
        DebugNode currentNode = debugNodeTracker.getCurrentNode();

        List<DebugPVReadTT> readList = currentNode.getPvReads();

        Optional<DebugPVReadTT> previousReadOpt = readList
                .stream()
                .filter(entryRead -> entryRead.getHashRequested() == hashRequested)
                .findFirst();

        if (previousReadOpt.isEmpty()) {
            DebugPVReadTT debugPVReadTT = new DebugPVReadTT()
                    .setHashRequested(hashRequested)
                    .setEntry(entry.clone());

            readList.add(debugPVReadTT);
        }
    }
}
