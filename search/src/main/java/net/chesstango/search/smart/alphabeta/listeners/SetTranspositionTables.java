package net.chesstango.search.smart.alphabeta.listeners;

import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.*;
import net.chesstango.search.smart.transposition.ArrayTTable;
import net.chesstango.search.smart.transposition.TTable;

/**
 * @author Mauricio Coria
 */
public class SetTranspositionTables implements SearchByCycleListener, ResetListener, SearchByDepthListener {
    private final TTable maxMap;
    private final TTable minMap;
    private final TTable qMaxMap;
    private final TTable qMinMap;
    private boolean reuseTranspositionTable;

    public SetTranspositionTables() {
        this.maxMap = new ArrayTTable();
        this.minMap = new ArrayTTable();
        this.qMaxMap = new ArrayTTable();
        this.qMinMap = new ArrayTTable();
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
