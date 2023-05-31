package net.chesstango.search.smart;

import net.chesstango.board.moves.Move;

import java.util.Map;
import java.util.Set;

/**
 * @author Mauricio Coria
 */
public class SearchContext {
    private final int maxPly;
    private int[] visitedNodesCounters;
    private int[] expectedNodesCounters;
    private int[] visitedNodesQuiescenceCounter;
    private Set<Move>[] distinctMovesPerLevel;
    private Map<Long, Transposition> maxMap;
    private Map<Long, Transposition> minMap;

    public SearchContext(int maxPly) {
        this.maxPly = maxPly;
    }

    public int getMaxPly() {
        return maxPly;
    }

    public int[] getVisitedNodesCounters() {
        return visitedNodesCounters;
    }

    public int[] getExpectedNodesCounters() {
        return expectedNodesCounters;
    }

    public int[] getVisitedNodesQuiescenceCounter() {
        return visitedNodesQuiescenceCounter;
    }

    public Set<Move>[] getDistinctMovesPerLevel() {
        return distinctMovesPerLevel;
    }

    public Map<Long, Transposition> getMaxMap() {
        return maxMap;
    }

    public Map<Long, Transposition> getMinMap() {
        return minMap;
    }

    public void setVisitedNodesCounters(int[] visitedNodesCounters) {
        this.visitedNodesCounters = visitedNodesCounters;
    }

    public void setExpectedNodesCounters(int[] expectedNodesCounters) {
        this.expectedNodesCounters = expectedNodesCounters;
    }

    public void setVisitedNodesQuiescenceCounter(int[] visitedNodesQuiescenceCounter) {
        this.visitedNodesQuiescenceCounter = visitedNodesQuiescenceCounter;
    }

    public void setDistinctMovesPerLevel(Set<Move>[] distinctMovesPerLevel) {
        this.distinctMovesPerLevel = distinctMovesPerLevel;
    }

    public void setMaxMap(Map<Long, Transposition> maxMap) {
        this.maxMap = maxMap;
    }

    public void setMinMap(Map<Long, Transposition> minMap) {
        this.minMap = minMap;
    }


}
