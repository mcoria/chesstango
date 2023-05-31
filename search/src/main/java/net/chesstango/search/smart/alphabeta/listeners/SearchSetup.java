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
public class SearchSetup implements SearchLifeCycle {
    private int[] visitedNodesQuiescenceCounter;

    private Map<Long, Transposition> maxMap;

    private Map<Long, Transposition> minMap;

    @Override
    public void initSearch(Game game, int maxDepth) {
        this.visitedNodesQuiescenceCounter = new int[30];
        this.maxMap = new HashMap<>();
        this.minMap = new HashMap<>();
    }

    @Override
    public void closeSearch(SearchMoveResult result) {
        this.visitedNodesQuiescenceCounter = null;
        this.maxMap = null;
        this.minMap = null;
    }

    @Override
    public void init(SearchContext context) {
        context.setVisitedNodesQuiescenceCounter(visitedNodesQuiescenceCounter);
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
    }
}
