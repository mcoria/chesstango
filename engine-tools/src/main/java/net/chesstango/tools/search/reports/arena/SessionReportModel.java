package net.chesstango.tools.search.reports.arena;

import net.chesstango.search.SearchResult;
import net.chesstango.search.smart.features.statistics.node.NodeStatistics;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public class SessionReportModel {
    public String engineName;
    public long searches;

    ///////// START TOTALS
    public long expectedNodesTotal;
    public long expectedRNodesTotal;
    public long expectedQNodesTotal;
    public long visitedNodesTotal;
    public long visitedRNodesTotal;
    public long visitedQNodesTotal;
    public int cutoffPercentage;
    public int visitedNodesTotalAvg;
    public int visitedRNodesAvg;
    public int visitedQNodesAvg;
    //////// END TOTALS


    ///////////////////// START VISITED REGULAR NODES
    public int maxSearchRLevel;
    public long[] visitedRNodesCounters;
    public long[] expectedRNodesCounters;
    public int[] visitedRNodesCountersAvg;
    public int[] cutoffRPercentages;
    ///////////////////// END VISITED REGULAR NODES

    ///////////////////// START VISITED QUIESCENCE NODES
    public int maxSearchQLevel;
    public long[] visitedQNodesCounters;
    public long[] expectedQNodesCounters;
    public int[] cutoffQPercentages;
    public int[] visitedQNodesCountersAvg;
    ///////////////////// END VISITED QUIESCENCE NODES

    public static SessionReportModel collectStatics(String engineName, List<SearchResult> searchResults) {
        SessionReportModel sessionReportModel = new SessionReportModel();

        sessionReportModel.engineName = engineName;

        sessionReportModel.load(searchResults);

        return sessionReportModel;
    }

    private void load(List<SearchResult> searchResults) {
        this.searches = searchResults.size();

        this.expectedRNodesCounters = new long[30];
        this.visitedRNodesCounters = new long[30];
        this.visitedQNodesCounters = new long[30];
        this.expectedQNodesCounters = new long[30];
        this.visitedRNodesCountersAvg = new int[30];
        this.visitedQNodesCountersAvg = new int[30];
        this.cutoffRPercentages = new int[30];
        this.cutoffQPercentages = new int[30];


        searchResults.forEach(this::summarize);

        /**
         * Totales sumarizados
         */
        for (int i = 0; i < 30; i++) {
            if (this.visitedRNodesCounters[i] > 0) {
                this.cutoffRPercentages[i] = (int) (100 - (100 * this.visitedRNodesCounters[i] / this.expectedRNodesCounters[i]));
                this.maxSearchRLevel = i + 1;
            }

            if (this.visitedQNodesCounters[i] > 0) {
                this.cutoffQPercentages[i] = (int) (100 - (100 * this.visitedQNodesCounters[i] / this.expectedQNodesCounters[i]));
                this.maxSearchQLevel = i + 1;
            }

            this.visitedRNodesTotal += this.visitedRNodesCounters[i];
            this.visitedQNodesTotal += this.visitedQNodesCounters[i];
            this.visitedRNodesCountersAvg[i] = (int) (this.visitedRNodesCounters[i] / this.searches);
            this.visitedQNodesCountersAvg[i] = (int) (this.visitedQNodesCounters[i] / this.searches);

            this.expectedRNodesTotal += this.expectedRNodesCounters[i];
            this.expectedQNodesTotal += this.expectedQNodesCounters[i];
        }

        this.visitedNodesTotal = this.visitedRNodesTotal + this.visitedQNodesTotal;
        this.expectedNodesTotal = this.expectedRNodesTotal + this.expectedQNodesTotal;
        this.cutoffPercentage = (int) (100 - (100 * this.visitedNodesTotal / this.expectedNodesTotal));


        this.visitedRNodesAvg = (int) (this.visitedRNodesTotal / this.searches);
        this.visitedQNodesAvg = (int) (this.visitedQNodesTotal / this.searches);
        this.visitedNodesTotalAvg = (int) (this.visitedNodesTotal / this.searches);
    }

    private void summarize(SearchResult searchResult) {
        if (searchResult.getRegularNodeStatistics() != null) {
            collectRegularNodeStatistics(searchResult);
        }

        if (searchResult.getQuiescenceNodeStatistics() != null) {
            collectQuiescenceNodeStatistics(searchResult);
        }
    }

    private void collectRegularNodeStatistics(SearchResult searchResult) {
        NodeStatistics regularNodeStatistics = searchResult.getRegularNodeStatistics();
        int[] visitedRNodeCounters = regularNodeStatistics.visitedNodesCounters();
        int[] expectedRNodeCounters = regularNodeStatistics.expectedNodesCounters();

        for (int i = 0; i < 30; i++) {
            if (expectedRNodeCounters[i] < visitedRNodeCounters[i]) {
                throw new RuntimeException("reportModelDetail.expectedRNodesCounters[i] < reportModelDetail.visitedRNodesCounters[i]");
            }
            if (visitedRNodeCounters[i] > 0) {
                this.visitedRNodesCounters[i] += visitedRNodeCounters[i];
                this.expectedRNodesCounters[i] += expectedRNodeCounters[i];
            }
        }
    }

    private void collectQuiescenceNodeStatistics(SearchResult searchResult) {
        NodeStatistics quiescenceNodeStatistics = searchResult.getQuiescenceNodeStatistics();
        int[] visitedQNodesCounters = quiescenceNodeStatistics.visitedNodesCounters();
        int[] expectedQNodesCounters = quiescenceNodeStatistics.expectedNodesCounters();

        for (int i = 0; i < 30; i++) {
            if (expectedQNodesCounters[i] < visitedQNodesCounters[i]) {
                throw new RuntimeException("reportModelDetail.expectedQNodesCounters[i] < reportModelDetail.visitedQNodesCounters[i]");
            }
            if (visitedQNodesCounters[i] > 0) {
                this.visitedQNodesCounters[i] += visitedQNodesCounters[i];
                this.expectedQNodesCounters[i] += expectedQNodesCounters[i];
            }
        }
    }
}
