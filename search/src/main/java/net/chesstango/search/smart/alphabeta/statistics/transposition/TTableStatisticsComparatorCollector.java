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
public class TTableStatisticsComparatorCollector implements TTable, Acceptor {

    private final TTableCounters tTableCounters;

    private TTable tTable;

    public TTableStatisticsComparatorCollector(TTableCounters tTableCounters) {
        this.tTableCounters = tTableCounters;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean load(long hash, TranspositionEntry entry) {
        boolean result = tTable.load(hash, entry);
        if (result && hash == entry.getHash()) {
            tTableCounters.increaseReadComparatorHits();
        }
        tTableCounters.increaseReads();
        return result;
    }

    @Override
    public void save(TranspositionEntry entry) {
        throw new RuntimeException("save() should not be called on a comparator");
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
