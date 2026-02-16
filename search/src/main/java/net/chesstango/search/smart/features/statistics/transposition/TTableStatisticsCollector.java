package net.chesstango.search.smart.features.statistics.transposition;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.features.transposition.TTable;
import net.chesstango.search.smart.features.transposition.TranspositionEntry;

/**
 * @author Mauricio Coria
 */
@Getter
@Setter
public class TTableStatisticsCollector implements TTable, SearchByCycleListener, Acceptor {

    private TTable tTable;

    private long tableHits;

    private long tableCollisions;

    @Override
    public boolean load(long hash, TranspositionEntry entry) {
        boolean result = tTable.load(hash, entry);
        if (result) {
            tableHits++;
        }
        return result;
    }

    @Override
    public InsertResult save(TranspositionEntry entry) {
        InsertResult result = tTable.save(entry);
        if (result == InsertResult.REPLACED) {
            tableCollisions++;
        }
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

    @Override
    public void beforeSearch() {
        tableHits = 0;
        tableCollisions = 0;
    }

    public TTableStatistics getTTableStatistics() {
        return new TTableStatistics(tableHits, tableCollisions);
    }
}
