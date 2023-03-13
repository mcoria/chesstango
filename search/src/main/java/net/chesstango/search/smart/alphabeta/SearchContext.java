package net.chesstango.search.smart.alphabeta;

public class SearchContext {
    private final int maxPly;
    private final int[] visitedNodesCounter;

    public SearchContext(int maxPly, int[] visitedNodesCounter) {
        this.maxPly = maxPly;
        this.visitedNodesCounter = visitedNodesCounter;
    }

    public int getMaxPly() {
        return maxPly;
    }

    public int[] getVisitedNodesCounter() {
        return visitedNodesCounter;
    }
}
