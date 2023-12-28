package net.chesstango.search.smart.alphabeta.listeners;

import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.*;
import net.chesstango.search.smart.transposition.ArrayTTable;
import net.chesstango.search.smart.transposition.TTable;

/**
 * @author Mauricio Coria
 */
public class SetTranspositionTables implements SearchByCycleListener, ResetListener, SearchByDepthListener {
    protected final TTable maxMap;
    protected final TTable minMap;
    protected final TTable qMaxMap;
    protected final TTable qMinMap;
    protected boolean reuseTranspositionTable;

    public SetTranspositionTables() {
        this.maxMap = createTTable("maxMap");
        this.minMap = createTTable("minMap");
        this.qMaxMap = createTTable("qMaxMap");
        this.qMinMap = createTTable("qMinMap");
    }

    protected TTable createTTable(String tableName) {
        return new ArrayTTable();
    }

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        context.setMaxMap(maxMap);
        context.setMinMap(minMap);
        context.setQMaxMap(qMaxMap);
        context.setQMinMap(qMinMap);
    }

    @Override
    public void afterSearch() {
        if (!reuseTranspositionTable) {
            reset();
        }
    }

    @Override
    public void beforeSearchByDepth(SearchByDepthContext context) {
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {
    }

    @Override
    public void reset() {
        this.maxMap.clear();
        this.minMap.clear();
        this.qMaxMap.clear();
        this.qMinMap.clear();
    }

    public void setReuseTranspositionTable(boolean reuseTranspositionTable) {
        this.reuseTranspositionTable = reuseTranspositionTable;
    }
}
