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

    private int[] visitedNodesCounter;

    private List<Set<Move>> distinctMoves;

    public SearchContext(int maxPly) {
        this.maxPly = maxPly;
    }

    public int getMaxPly() {
        return maxPly;
    }

    public SearchContext setVisitedNodesCounter(int[] visitedNodesCounter) {
        this.visitedNodesCounter = visitedNodesCounter;
        return this;
    }

    public int[] getVisitedNodesCounter() {
        return visitedNodesCounter;
    }

    public List<Set<Move>> getDistinctMoves() {
        return distinctMoves;
    }

    public SearchContext setDistinctMoves(List<Set<Move>> distinctMoves) {
        this.distinctMoves = distinctMoves;
        return this;
    }
}
