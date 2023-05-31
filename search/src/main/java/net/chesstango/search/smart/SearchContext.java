package net.chesstango.search.smart;

import net.chesstango.board.moves.Move;

import java.util.Map;
import java.util.Set;

/**
 * @author Mauricio Coria
 */
public class SearchContext {
    private final int maxPly;
    private int[] visitedNodesQuiescenceCounter;
    private Map<Long, Transposition> maxMap;
    private Map<Long, Transposition> minMap;

    public SearchContext(int maxPly) {
        this.maxPly = maxPly;
    }

    public int getMaxPly() {
        return maxPly;
    }

    public int[] getVisitedNodesQuiescenceCounter() {
        return visitedNodesQuiescenceCounter;
    }

    public Map<Long, Transposition> getMaxMap() {
        return maxMap;
    }

    public Map<Long, Transposition> getMinMap() {
        return minMap;
    }

    public void setVisitedNodesQuiescenceCounter(int[] visitedNodesQuiescenceCounter) {
        this.visitedNodesQuiescenceCounter = visitedNodesQuiescenceCounter;
    }

    public void setMaxMap(Map<Long, Transposition> maxMap) {
        this.maxMap = maxMap;
    }

    public void setMinMap(Map<Long, Transposition> minMap) {
        this.minMap = minMap;
    }

}
