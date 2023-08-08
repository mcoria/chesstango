package net.chesstango.search.smart.alphabeta.listeners;

import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.SearchLifeCycle;
import net.chesstango.search.smart.Transposition;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mauricio Coria
 */
public class SetTranspositionTables implements SearchLifeCycle {

    private Map<Long, Transposition> maxMap;

    private Map<Long, Transposition> minMap;

    private boolean reuseTranspositionTable;

    @Override
    public void beforeSearch(Game game, int maxDepth) {
        if(!reuseTranspositionTable) {
            this.maxMap = new HashMap<>();
            this.minMap = new HashMap<>();
        }
    }

    @Override
    public void afterSearch(SearchMoveResult result) {
        if(!reuseTranspositionTable) {
            this.maxMap = null;
            this.minMap = null;
        }
    }

    @Override
    public void init(SearchContext context) {
        context.setMaxMap(maxMap);
        context.setMinMap(minMap);
    }

    @Override
    public void close(SearchMoveResult result) {
    }

    @Override
    public void stopSearching() {

    }

    @Override
    public void reset() {
        this.maxMap = new HashMap<>();
        this.minMap = new HashMap<>();
    }

    public void setReuseTranspositionTable(boolean reuseTranspositionTable) {
        this.reuseTranspositionTable = reuseTranspositionTable;
    }
}
