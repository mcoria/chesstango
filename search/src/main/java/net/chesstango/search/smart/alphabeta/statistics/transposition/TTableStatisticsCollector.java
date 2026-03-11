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
public class TTableStatisticsCollector implements TTable, Acceptor {

    private final TTableCounters TTableCounters;

    private TTable tTable;

    public TTableStatisticsCollector(TTableCounters TTableCounters) {
        this.TTableCounters = TTableCounters;
    }


    @Override
    public boolean load(long hash, TranspositionEntry entry) {
        boolean result = tTable.load(hash, entry);
        if (result) {
            TTableCounters.increaseReadHits();
        }
        TTableCounters.increaseReads();
        return result;
    }

    @Override
    public SaveResult save(TranspositionEntry entry) {
        SaveResult result = tTable.save(entry);
        if (result == SaveResult.OVER_WRITTEN) {
            TTableCounters.increaseOverWrites();
        } else if (result == SaveResult.UPDATED) {
            TTableCounters.increaseUpdates();
        }
        TTableCounters.increaseWrites();
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
