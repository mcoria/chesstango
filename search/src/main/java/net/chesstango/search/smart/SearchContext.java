package net.chesstango.search.smart;

import net.chesstango.board.moves.Move;

import java.util.HashSet;
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

    public SearchContext(int maxPly) {
        this.maxPly = maxPly;
        this.visitedNodesCounters = new int[30];
        this.visitedNodesQuiescenceCounter = new int[30];
        this.expectedNodesCounters = new int[30];
        this.distinctMovesPerLevel = new Set[30];
        IntStream.range(0, 30).forEach(i -> distinctMovesPerLevel[i] = new HashSet<>() );
    }

    public SearchContext(int maxPly, int[] visitedNodesCounters, int[] expectedNodesCounters, int[] visitedNodesQuiescenceCounter, Set<Move>[] distinctMovesPerLevel) {
        this.maxPly = maxPly;
        this.visitedNodesCounters = visitedNodesCounters;
        this.visitedNodesQuiescenceCounter = visitedNodesQuiescenceCounter;
        this.expectedNodesCounters = expectedNodesCounters;
        this.distinctMovesPerLevel = distinctMovesPerLevel;
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

}
