package net.chesstango.search.smart.alphabeta.statistics.transposition;

import lombok.Getter;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleListener;

/**
 * @author Mauricio Coria
 */
@Getter
public class TTableCounters implements SearchByCycleListener {
    private long readNodeHits;

    private long readComparatorHits;

    private long reads;

    private long overWrites;

    private long writes;

    private long updates;

    public void increaseReads() {
        reads++;
    }

    public void increaseReadComparatorHits() {
        readComparatorHits++;
    }

    public void increaseReadNodeHits() {
        readNodeHits++;
    }

    public void increaseWrites() {
        writes++;
    }

    public void increaseUpdates() {
        updates++;
    }

    public void increaseOverWrites() {
        overWrites++;
    }


    @Override
    public void beforeSearch() {
        reads = 0;
        readNodeHits = 0;
        readComparatorHits = 0;

        writes = 0;
        updates = 0;
        overWrites = 0;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public TTableStatistics getTTableStatistics() {
        return new TTableStatistics(reads, readNodeHits, readComparatorHits, writes, updates, overWrites);
    }

}
