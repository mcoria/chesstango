package net.chesstango.uci.arena.reports;

import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.statistics.NodeStatistics;

import java.util.List;

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


    public static SessionReportModel collectStatics(String engineName, List<SearchMoveResult> searches) {
        SessionReportModel rowModel = new SessionReportModel();
        rowModel.engineName = engineName;

        rowModel.searches = searches.stream().count();
        rowModel.searchesWithoutCollisions = searches.stream().mapToInt(SearchMoveResult::getBestMovesCounter).filter(value -> value == 0).count();
        rowModel.searchesWithoutCollisionsPercentage = (int) ((rowModel.searchesWithoutCollisions * 100) / rowModel.searches);
        rowModel.searchesWithCollisions = searches.stream().mapToInt(SearchMoveResult::getBestMovesCounter).filter(value -> value > 0).count();
        rowModel.searchesWithCollisionsPercentage = (int) ((rowModel.searchesWithCollisions * 100) / rowModel.searches);

        if (rowModel.searchesWithCollisions > 0) {
            rowModel.avgOptionsPerCollision = searches.stream().mapToInt(SearchMoveResult::getBestMovesCounter).filter(value -> value > 0).average().getAsDouble();
        }


        long[] expectedRNodesCounters = new long[30];
        rowModel.visitedRNodesCounters = new long[30];
        rowModel.visitedQNodesCounters = new int[30];
        rowModel.visitedRNodesCountersAvg = new int[30];
        rowModel.visitedQNodesCountersAvg = new int[30];
        rowModel.cutoffPercentages = new int[30];
        rowModel.maxDistinctMovesPerLevel = new int[30];
        rowModel.maxSearchRLevel = 0;
        rowModel.visitedNodesTotal = 0;


        searches.forEach(searchMoveResult -> {
            NodeStatistics regularNodeStatistics = searchMoveResult.getRegularNodeStatistics();
            int[] visitedRNodeCounters = regularNodeStatistics.visitedNodesCounters();
            int[] expectedRNodeCounters = regularNodeStatistics.expectedNodesCounters();
            for (int i = 0; i < visitedRNodeCounters.length; i++) {
                rowModel.visitedRNodesCounters[i] += visitedRNodeCounters[i];
                expectedRNodesCounters[i] += expectedRNodeCounters[i];

            }

            NodeStatistics quiescenceNodeStatistics = searchMoveResult.getQuiescenceNodeStatistics();
            int[] visitedQNodesCounters = quiescenceNodeStatistics.visitedNodesCounters();
            for (int i = 0; i < visitedQNodesCounters.length; i++) {
                rowModel.visitedQNodesCounters[i] += visitedQNodesCounters[i];
            }

            /*
            if (searchMoveResult.getDistinctMovesPerLevel() != null) {
                int level = 0;
                for (Set<Move> moveCollection :
                        searchMoveResult.getDistinctMovesPerLevel()) {
                    if (rowModel.maxDistinctMovesPerLevel[level] < moveCollection.size()) {
                        rowModel.maxDistinctMovesPerLevel[level] = moveCollection.size();
                    }
                    level++;
                }
            }
             */
        });

        for (int i = 0; i < 30; i++) {
            if (rowModel.visitedRNodesCounters[i] > 0) {
                rowModel.cutoffPercentages[i] = (int) (100 - (100 * rowModel.visitedRNodesCounters[i] / expectedRNodesCounters[i]));
                rowModel.maxSearchRLevel = i + 1;
            }

            if (rowModel.visitedQNodesCounters[i] > 0) {
                rowModel.maxSearchQLevel = i + 1;
            }

            rowModel.visitedRNodesTotal += rowModel.visitedRNodesCounters[i];
            rowModel.visitedQNodesTotal += rowModel.visitedQNodesCounters[i];
            rowModel.visitedRNodesCountersAvg[i] = (int) (rowModel.visitedRNodesCounters[i] / rowModel.searches);
            rowModel.visitedQNodesCountersAvg[i] = (int) (rowModel.visitedQNodesCounters[i] / rowModel.searches);
        }

        rowModel.visitedNodesTotal = rowModel.visitedRNodesTotal + rowModel.visitedQNodesTotal;
        rowModel.visitedNodesTotalAvg = (int) (rowModel.visitedNodesTotal / rowModel.searches);

        rowModel.visitedRNodesAvg = (int) (rowModel.visitedRNodesTotal/ rowModel.searches);
        rowModel.visitedQNodesAvg = (int) (rowModel.visitedQNodesTotal/ rowModel.searches);

        return rowModel;
    }
}
