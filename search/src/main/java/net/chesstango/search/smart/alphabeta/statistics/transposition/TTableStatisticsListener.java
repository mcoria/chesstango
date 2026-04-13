package net.chesstango.search.smart.alphabeta.statistics.transposition;

import lombok.Setter;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.alphabeta.transposition.TTable;
import net.chesstango.search.smart.alphabeta.transposition.TTableArrayPrimitives;

/**
 * @author Mauricio Coria
 */
@Setter
public class TTableStatisticsListener implements Acceptor, SearchByCycleListener {

    private final TTableCounters tTableCounters;
    private final TTable tTable;

    public TTableStatisticsListener(TTableCounters tTableCounters, TTable tTable) {
        this.tTableCounters = tTableCounters;
        this.tTable = tTable;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch() {
    }

    @Override
    public void afterSearch() {
        int mapFillPercentage = getFillPercentage(tTable);

        tTableCounters.setMapFillPercentage(mapFillPercentage);
    }

    private int getFillPercentage(TTable tTable) {
        return switch (tTable) {
            case TTableArrayPrimitives tTableArrayPrimitives -> tTableArrayPrimitives.getFillPercentage();
            default -> 0;
        };
    }

}
