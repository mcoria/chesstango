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

    private List<Set<Move>> distinctMovesPerLevel;

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

    public List<Set<Move>> getDistinctMovesPerLevel() {
        return distinctMovesPerLevel;
    }

    public SearchContext setDistinctMovesPerLevel(List<Set<Move>> distinctMovesPerLevel) {
        this.distinctMovesPerLevel = distinctMovesPerLevel;
        return this;
    }
}
