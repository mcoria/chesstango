package net.chesstango.uci.arena.reports;

/**
 * @author Mauricio Coria
 */
public class SessionReportModel {
    public String engineName;
    public long searches;

    ///////////////////// START COLLISIONS
    public long searchesWithoutCollisions;
    public int searchesWithoutCollisionsPercentage;
    public long searchesWithCollisions;
    public int searchesWithCollisionsPercentage;
    public double avgOptionsPerCollision;
    ///////////////////// END COLLISIONS

    ///////// START TOTALS
    public long visitedNodesTotal;
    public int visitedNodesTotalAvg;
    public long visitedRNodesTotal;
    public long visitedQNodesTotal;

    public int visitedRNodesAvg;
    public int visitedQNodesAvg;
    //////// END TOTALS


    ///////////////////// START VISITED REGULAR NODES
    public int maxSearchRLevel;
    public long[] visitedRNodesCounters;
    public int[] visitedRNodesCountersAvg;
    public int[] cutoffPercentages;
    ///////////////////// END VISITED REGULAR NODES

    ///////////////////// START VISITED QUIESCENCE NODES
    public int maxSearchQLevel;
    public int[] visitedQNodesCounters;
    public int[] visitedQNodesCountersAvg;
    ///////////////////// END VISITED QUIESCENCE NODES

    public int[] maxDistinctMovesPerLevel;
}
