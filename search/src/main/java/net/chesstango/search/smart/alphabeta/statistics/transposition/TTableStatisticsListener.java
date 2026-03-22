package net.chesstango.search.smart.alphabeta.statistics.transposition;

import lombok.Setter;
import net.chesstango.search.SearchResult;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.ResetListener;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.alphabeta.transposition.TTable;

/**
 * @author Mauricio Coria
 */
@Setter
public class TTableStatisticsListener implements SearchByCycleListener {

    private final TTableCounters tTableCounters;
    private final TTable maxMap;
    private final TTable minMap;

    public TTableStatisticsListener(TTableCounters tTableCounters, TTable maxMap, TTable minMap) {
        this.tTableCounters = tTableCounters;
        this.maxMap = maxMap;
        this.minMap = minMap;
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

    }

}
