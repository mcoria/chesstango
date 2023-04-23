package net.chesstango.search.smart;

import net.chesstango.board.moves.Move;

import java.util.List;
import java.util.Set;

/**
 * @author Mauricio Coria
 */
public class SearchContext {
    private final int maxPly;
    private int[] visitedNodesCounters;
    private int[] expectedNodesCounters;
    private Set<Move>[] distinctMovesPerLevel;
    public SearchContext(int maxPly) {
        this.maxPly = maxPly;
    }

    public int getMaxPly() {
        return maxPly;
    }

    public SearchContext setVisitedNodesCounters(int[] visitedNodesCounters) {
        this.visitedNodesCounters = visitedNodesCounters;
        return this;
    }

    public int[] getVisitedNodesCounters() {
        return visitedNodesCounters;
    }

    public int[] getExpectedNodesCounters() {
        return expectedNodesCounters;
    }

    public SearchContext setExpectedNodesCounters(int[] expectedNodesCounters) {
        this.expectedNodesCounters = expectedNodesCounters;
        return this;
    }

    public Set<Move>[] getDistinctMovesPerLevel() {
        return distinctMovesPerLevel;
    }

    public SearchContext setDistinctMovesPerLevel(Set<Move>[] distinctMovesPerLevel) {
        this.distinctMovesPerLevel = distinctMovesPerLevel;
        return this;
    }
}
