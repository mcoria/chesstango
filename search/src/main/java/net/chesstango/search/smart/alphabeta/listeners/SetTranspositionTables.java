package net.chesstango.search.smart.alphabeta.listeners;

import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.QTransposition;
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

    private final Map<Long, QTransposition> qMaxMap;
    private final Map<Long, QTransposition> qMinMap;

    private boolean reuseTranspositionTable;

    public SetTranspositionTables(){
        this.maxMap = new HashMap<>();
        this.minMap = new HashMap<>();
        this.qMaxMap = new HashMap<>();
        this.qMinMap = new HashMap<>();
    }

    @Override
    public void beforeSearch(Game game, int maxDepth) {
        if(!reuseTranspositionTable) {
            this.maxMap.clear();
            this.minMap.clear();

            this.qMaxMap.clear();
            this.qMinMap.clear();
        }
    }

    @Override
    public void afterSearch(SearchMoveResult result) {
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
