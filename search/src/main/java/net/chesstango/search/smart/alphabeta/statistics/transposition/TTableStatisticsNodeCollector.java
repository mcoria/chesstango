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

    private TTable tTable;

    public TTableStatisticsNodeCollector(TTableCounters tTableCounters) {
        this.tTableCounters = tTableCounters;
    }

    @Override
    public boolean load(long hash, TranspositionEntry entry) {
        boolean result = tTable.load(hash, entry);
        if (result) {
            tTableCounters.increaseReadNodeHits();
        }
        tTableCounters.increaseReads();
        return result;
    }

    @Override
    public SaveResult save(TranspositionEntry entry) {
        SaveResult result = tTable.save(entry);
        if (result == SaveResult.OVER_WRITTEN) {
            tTableCounters.increaseOverWrites();
        } else if (result == SaveResult.UPDATED) {
            tTableCounters.increaseUpdates();
        }
        tTableCounters.increaseWrites();
        return result;
    }

    @Override
    public void increaseAge() {
        tTable.increaseAge();
    }

    @Override
    public void clear() {
        tTable.clear();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
