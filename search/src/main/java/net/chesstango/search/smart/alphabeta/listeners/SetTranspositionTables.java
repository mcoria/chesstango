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
    private final Map<Long, Transposition> maxMap;
    private final Map<Long, Transposition> minMap;
    private boolean reuseTranspositionTable;

    public SetTranspositionTables(){
        this.maxMap = new HashMap<>();
        this.minMap = new HashMap<>();
    }

    @Override
    public void beforeSearch(Game game, int maxDepth) {
        if(!reuseTranspositionTable) {
            this.maxMap.clear();
            this.minMap.clear();
        }
    }

    @Override
    public void afterSearch(SearchMoveResult result) {
    }

    @Override
    public void beforeSearchByDepth(SearchContext context) {
        context.setMaxMap(maxMap);
        context.setMinMap(minMap);
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {
    }

    @Override
    public void stopSearching() {

    }

    @Override
    public void reset() {
        this.maxMap.clear();
        this.minMap.clear();
    }

    public void setReuseTranspositionTable(boolean reuseTranspositionTable) {
        this.reuseTranspositionTable = reuseTranspositionTable;
    }
}
