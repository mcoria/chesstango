package net.chesstango.search.smart.statistics.transposition;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.search.Acceptor;
import net.chesstango.search.SearchListener;
import net.chesstango.search.Visitor;

/**
 * @author Mauricio Coria
 */
@Getter
public class TTableCounters implements Acceptor, SearchListener {
    // Node statistics reads
    private long readNodes;
    private long readNodeHits;

    // Node statistics writes
    private long writes;
    private long updates;
    private long overWrites;

    // Comparator statistics
    private long readComparators;
    private long readComparatorHits;

    @Setter
    private int mapFillPercentage;

    public void increaseReadNodes() {
        readNodes++;
    }

    public void increaseReadNodeHits() {
        readNodeHits++;
    }

    public void increaseReadComparators() {
        readComparators++;
    }

    public void increaseReadComparatorHits() {
        readComparatorHits++;
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
        readNodes = 0;
        readNodeHits = 0;

        writes = 0;
        updates = 0;
        overWrites = 0;

        readComparators = 0;
        readComparatorHits = 0;

        mapFillPercentage = 0;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public TTableStatistics getTTableStatistics() {
        return new TTableStatistics(
                // Node statistics reads
                readNodes,
                readNodeHits,

                // Node statistics writes
                writes,
                updates,
                overWrites,

                // Comparator statistics
                readComparators,
                readComparatorHits,

                mapFillPercentage);
    }

}
