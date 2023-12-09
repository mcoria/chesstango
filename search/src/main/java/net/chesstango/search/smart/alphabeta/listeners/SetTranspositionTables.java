package net.chesstango.search.smart.alphabeta.listeners;

import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.ResetListener;
import net.chesstango.search.smart.SearchByDepthListener;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.SearchByCycleListener;
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
    public void beforeSearch(Game game) {
    }

    @Override
    public void afterSearch(SearchMoveResult result) {
        if (!reuseTranspositionTable) {
            reset();
        }
    }

    @Override
    public void beforeSearchByDepth(SearchContext context) {
        context.setMaxMap(maxMap);
        context.setMinMap(minMap);
        context.setQMaxMap(qMaxMap);
        context.setQMinMap(qMinMap);
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
