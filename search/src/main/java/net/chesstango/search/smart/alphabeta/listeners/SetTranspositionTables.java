package net.chesstango.search.smart.alphabeta.listeners;

import lombok.Setter;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.ResetListener;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.transposition.ArrayTTable;
import net.chesstango.search.smart.transposition.TTable;

/**
 * @author Mauricio Coria
 */
public class SetTranspositionTables implements SearchByCycleListener, ResetListener {
    protected final TTable maxMap;

    protected final TTable minMap;
    protected final TTable qMaxMap;
    protected final TTable qMinMap;

    @Setter
    protected boolean reuseTranspositionTable;

    public SetTranspositionTables() {
        this.maxMap = createMaxTTable();
        this.minMap = createMinTTable();
        this.qMaxMap = createQMaxTTable();
        this.qMinMap = createQMinTTable();
    }

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        context.setMaxMap(maxMap);
        context.setMinMap(minMap);
        context.setQMaxMap(qMaxMap);
        context.setQMinMap(qMinMap);
    }

    @Override
    public void afterSearch(SearchMoveResult searchMoveResult) {
        if (!reuseTranspositionTable) {
            reset();
        }
    }


    @Override
    public void reset() {
        this.maxMap.clear();
        this.minMap.clear();
        this.qMaxMap.clear();
        this.qMinMap.clear();
    }

    protected TTable createQMinTTable() {
        return defaultCreateTTable();
    }

    protected TTable createQMaxTTable() {
        return defaultCreateTTable();
    }

    protected TTable createMinTTable() {
        return defaultCreateTTable();
    }

    protected TTable createMaxTTable() {
        return defaultCreateTTable();
    }

    protected TTable defaultCreateTTable() {
        return new ArrayTTable();
    }
}
