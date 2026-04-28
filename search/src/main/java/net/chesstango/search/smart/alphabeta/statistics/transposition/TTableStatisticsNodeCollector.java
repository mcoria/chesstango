package net.chesstango.search.smart.alphabeta.statistics.transposition;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.transposition.TTable;
import net.chesstango.search.smart.alphabeta.transposition.TranspositionEntry;

/**
 * @author Mauricio Coria
 */
@Getter
@Setter
public class TTableStatisticsNodeCollector implements TTable, Acceptor {

    private final TTableCounters tTableCounters;

    private final TranspositionEntry localEntry;

    private TTable tTable;

    public TTableStatisticsNodeCollector(TTableCounters tTableCounters) {
        this.tTableCounters = tTableCounters;
        this.localEntry = new TranspositionEntry();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean load(long hash, TranspositionEntry entry) {
        boolean loaded = tTable.load(hash, entry);
        if (loaded && hash == entry.getHash()) {
            tTableCounters.increaseReadNodeHits();
        }
        tTableCounters.increaseReads();
        return loaded;
    }

    @Override
    public void save(TranspositionEntry entry) {
        boolean loaded = tTable.load(entry.getHash(), localEntry);
        if (loaded) {
            if (localEntry.getHash() == entry.getHash()) {
                tTableCounters.increaseUpdates();
            } else {
                tTableCounters.increaseOverWrites();
            }
        }
        tTableCounters.increaseWrites();
        tTable.save(entry);
    }

    @Override
    public void increaseAge() {
        tTable.increaseAge();
    }

    @Override
    public void clear() {
        tTable.clear();
    }

}
