package net.chesstango.search.smart.alphabeta.statistics.transposition;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.alphabeta.transposition.TTable;
import net.chesstango.search.smart.alphabeta.transposition.TranspositionEntry;

/**
 * @author Mauricio Coria
 */
@Getter
@Setter
public class TTableStatisticsCollector implements TTable, Acceptor {

    private final TTCounters ttCounters;

    private TTable tTable;

    public TTableStatisticsCollector(TTCounters ttCounters) {
        this.ttCounters = ttCounters;
    }


    @Override
    public boolean load(long hash, TranspositionEntry entry) {
        boolean result = tTable.load(hash, entry);
        if (result) {
            ttCounters.increaseReadHits();
        }
        ttCounters.increaseReads();
        return result;
    }

    @Override
    public SaveResult save(TranspositionEntry entry) {
        SaveResult result = tTable.save(entry);
        if (result == SaveResult.OVER_WRITTEN) {
            ttCounters.increaseOverWrites();
        } else if (result == SaveResult.UPDATED) {
            ttCounters.increaseUpdates();
        }
        ttCounters.increaseWrites();
        return result;
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
