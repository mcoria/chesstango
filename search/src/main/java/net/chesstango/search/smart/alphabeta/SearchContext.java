package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.moves.Move;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Mauricio Coria
 */
public class SearchContext {
    private final int maxPly;
    private final int[] visitedNodesCounter;

    private List<Set<Move>> distinctMoves;

    public SearchContext(int maxPly, int[] visitedNodesCounter) {
        this.maxPly = maxPly;
        this.visitedNodesCounter = visitedNodesCounter;

        this.distinctMoves = new ArrayList<>(visitedNodesCounter.length);
        for (int i = 0; i < visitedNodesCounter.length; i++) {
            distinctMoves.add(new HashSet<>());
        }
    }

    public int getMaxPly() {
        return maxPly;
    }

    public int[] getVisitedNodesCounter() {
        return visitedNodesCounter;
    }

    public List<Set<Move>> getDistinctMoves() {
        return distinctMoves;
    }

    public void setDistinctMoves(List<Set<Move>> distinctMoves) {
        this.distinctMoves = distinctMoves;
    }
}
