package net.chesstango.search.smart;

import net.chesstango.board.moves.Move;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * @author Mauricio Coria
 */
public class SearchContext {
    private final int maxPly;
    private final int[] visitedNodesCounters;
    private final int[] expectedNodesCounters;
    private final int[] visitedNodesQuiescenceCounter;

    private final Set<Move>[] distinctMovesPerLevel;

    private final Map<Long, TableEntry> maxMap;

    private final Map<Long, TableEntry> minMap;

    private final Map<Long, TableEntry> qMaxMap;

    private final Map<Long, TableEntry> qMinMap;

    public SearchContext(int maxPly) {
        this.maxPly = maxPly;
        this.visitedNodesCounters = new int[30];
        this.visitedNodesQuiescenceCounter = new int[30];
        this.expectedNodesCounters = new int[30];
        this.distinctMovesPerLevel = new Set[30];
        IntStream.range(0, 30).forEach(i -> this.distinctMovesPerLevel[i] = new HashSet<>());
        this.maxMap = new HashMap<>();
        this.minMap = new HashMap<>();
        this.qMaxMap = new HashMap<>();
        this.qMinMap = new HashMap<>();
    }

    public SearchContext(int maxPly,
                         int[] visitedNodesCounters,
                         int[] expectedNodesCounters,
                         int[] visitedNodesQuiescenceCounter,
                         Set<Move>[] distinctMovesPerLevel,
                         Map<Long, TableEntry> maxMap,
                         Map<Long, TableEntry> minMap,
                         Map<Long, TableEntry> qMaxMap,
                         Map<Long, TableEntry> qMinMap) {
        this.maxPly = maxPly;
        this.visitedNodesCounters = visitedNodesCounters;
        this.visitedNodesQuiescenceCounter = visitedNodesQuiescenceCounter;
        this.expectedNodesCounters = expectedNodesCounters;
        this.distinctMovesPerLevel = distinctMovesPerLevel;
        this.maxMap = maxMap;
        this.minMap = minMap;
        this.qMaxMap = qMaxMap;
        this.qMinMap = qMinMap;
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

    public Map<Long, TableEntry> getMaxMap() {
        return maxMap;
    }

    public Map<Long, TableEntry> getMinMap() {
        return minMap;
    }

    public Map<Long, TableEntry> getQMaxMap() {
        return qMaxMap;
    }

    public Map<Long, TableEntry> getQMinMap() {
        return qMinMap;
    }

    public static class TableEntry {
        public int searchDepth;

        public long bestMoveAndValue;
        public int alpha;
        public int beta;
        public int value;
        public boolean exact;
    }

}
