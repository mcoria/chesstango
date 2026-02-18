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

    private long readHits;

    private long reads;

    private long overWrites;

    private long writes;

    @Override
    public boolean load(long hash, TranspositionEntry entry) {
        boolean result = tTable.load(hash, entry);
        if (result) {
            readHits++;
        }
        reads++;
        return result;
    }

    @Override
    public InsertResult save(TranspositionEntry entry) {
        InsertResult result = tTable.save(entry);
        if (result == InsertResult.OVER_WRITTEN) {
            overWrites++;
        }
        writes++;
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
        readHits = 0;
        reads = 0;
        overWrites = 0;
        writes = 0;
    }

    public TTableStatistics getTTableStatistics() {
        return new TTableStatistics(reads, readHits, writes, overWrites);
    }
}
