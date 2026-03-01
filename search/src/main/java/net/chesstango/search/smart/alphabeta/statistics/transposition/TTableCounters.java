package net.chesstango.search.smart.alphabeta.statistics.transposition;

import lombok.Getter;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleListener;

/**
 * @author Mauricio Coria
 */
@Getter
public class TTableCounters implements SearchByCycleListener {
    private long readHits;

    private long reads;

    private long overWrites;

    private long writes;

    private long updates;

    public void increaseReads() {
        reads++;
    }

    public void increaseReadHits() {
        readHits++;
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
        readHits = 0;

        writes = 0;
        updates = 0;
        overWrites = 0;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public TTableStatistics getTTableStatistics() {
        return new TTableStatistics(reads, readHits, writes, updates, overWrites);
    }

}
