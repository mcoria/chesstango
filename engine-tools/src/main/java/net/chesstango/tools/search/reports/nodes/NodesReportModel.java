package net.chesstango.tools.search.reports.nodes;

import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.search.SearchResult;
import net.chesstango.search.smart.features.statistics.node.NodeStatistics;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class NodesReportModel {
    public String reportTitle;

    public int searches;

    // Regular and Quiescence
    ///////////////////// START TOTALS
    public long visitedNodesTotal;
    public long expectedNodesTotal;
    public int cutoffPercentageTotal;
    public long executedMovesTotal;

    ///////////////////// END TOTALS


    ///////////////////// START VISITED REGULAR NODES
    public int maxSearchRLevel;
    public long[] expectedRNodesCounters;
    public long[] visitedRNodesCounters;
    public int[] cutoffRPercentages;
    public long expectedRNodesTotal;
    public long visitedRNodesTotal;
    ///////////////////// END VISITED REGULAR NODES

    ///////////////////// START VISITED QUIESCENCE NODES
    public int maxSearchQLevel;
    public long[] expectedQNodesCounters;
    public long[] visitedQNodesCounters;
    public int[] cutoffQPercentages;
    public long visitedQNodesTotal;
    public long expectedQNodesTotal;
    ///////////////////// END VISITED QUIESCENCE NODES

    public List<SearchesReportModelDetail> moveDetails;

    public static class SearchesReportModelDetail {
        public String id;

        public String move;
        public int executedMoves;


        /**
         * Node Statistics
         */

        public long visitedNodesTotal;
        public long expectedNodesTotal;
        public int cutoffPercentageTotal;


        public int[] expectedRNodesCounters;
        public int expectedRNodesCounter;
        public int[] visitedRNodesCounters;
        public int visitedRNodesCounter;
        public int[] cutoffRPercentages;


        public int[] expectedQNodesCounters;
        public int expectedQNodesCounter;
        public int[] visitedQNodesCounters;
        public int visitedQNodesCounter;
        public int[] cutoffQPercentages;
    }


    public static NodesReportModel collectStatistics(String reportTitle, List<SearchResult> searchResults) {
        NodesReportModel nodesReportModel = new NodesReportModel();

        nodesReportModel.reportTitle = reportTitle;

        nodesReportModel.load(searchResults);

        return nodesReportModel;
    }

    private void load(List<SearchResult> searchResults) {
        this.searches = searchResults.size();

        this.moveDetails = new LinkedList<>();
        this.expectedRNodesCounters = new long[30];
        this.visitedRNodesCounters = new long[30];
        this.cutoffRPercentages = new int[30];
        this.visitedQNodesCounters = new long[30];
        this.expectedQNodesCounters = new long[30];
        this.cutoffQPercentages = new int[30];


        searchResults.forEach(this::loadModelDetail);

        /**
         * Totales sumarizados
         */
        for (int i = 0; i < 30; i++) {
            if (this.expectedRNodesCounters[i] > 0) {
                this.cutoffRPercentages[i] = (int) (100 - (100 * this.visitedRNodesCounters[i] / this.expectedRNodesCounters[i]));
                this.maxSearchRLevel = i + 1;
            }

            if (this.expectedQNodesCounters[i] > 0) {
                this.cutoffQPercentages[i] = (int) (100 - (100 * this.visitedQNodesCounters[i] / this.expectedQNodesCounters[i]));
                this.maxSearchQLevel = i + 1;
            }

            this.expectedRNodesTotal += this.expectedRNodesCounters[i];
            this.visitedRNodesTotal += this.visitedRNodesCounters[i];

            this.expectedQNodesTotal += this.expectedQNodesCounters[i];
            this.visitedQNodesTotal += this.visitedQNodesCounters[i];
        }

        this.visitedNodesTotal = this.visitedRNodesTotal + this.visitedQNodesTotal;
        this.expectedNodesTotal = this.expectedRNodesTotal + this.expectedQNodesTotal;
        this.cutoffPercentageTotal = (int) (100 - ((100 * this.visitedNodesTotal) / this.expectedNodesTotal));
    }

    private void loadModelDetail(SearchResult searchResult) {
        SearchesReportModelDetail reportModelDetail = new SearchesReportModelDetail();
        SimpleMoveEncoder simpleMoveEncoder = new SimpleMoveEncoder();

        Move bestMove = searchResult.getBestMove();
        reportModelDetail.id = searchResult.getId();
        reportModelDetail.move = bestMove != null ? simpleMoveEncoder.encode(bestMove) : "";
        reportModelDetail.executedMoves = searchResult.getExecutedMoves();

        if (searchResult.getRegularNodeStatistics() != null) {
            collectRegularNodeStatistics(reportModelDetail, searchResult);
        }

        if (searchResult.getQuiescenceNodeStatistics() != null) {
            collectQuiescenceNodeStatistics(reportModelDetail, searchResult);
        }

        reportModelDetail.visitedNodesTotal = reportModelDetail.visitedRNodesCounter + reportModelDetail.visitedQNodesCounter;
        reportModelDetail.expectedNodesTotal = reportModelDetail.expectedRNodesCounter + reportModelDetail.expectedQNodesCounter;
        reportModelDetail.cutoffPercentageTotal = (int) (100 - (100 * reportModelDetail.visitedNodesTotal / reportModelDetail.expectedNodesTotal));

        this.executedMovesTotal += reportModelDetail.executedMoves;
        this.moveDetails.add(reportModelDetail);
    }

    private void collectRegularNodeStatistics(SearchesReportModelDetail reportModelDetail, SearchResult searchResult) {
        NodeStatistics regularNodeStatistics = searchResult.getRegularNodeStatistics();
        reportModelDetail.expectedRNodesCounters = regularNodeStatistics.expectedNodesCounters();
        reportModelDetail.visitedRNodesCounters = regularNodeStatistics.visitedNodesCounters();
        reportModelDetail.cutoffRPercentages = new int[30];

        for (int i = 0; i < 30; i++) {
            if (reportModelDetail.expectedRNodesCounters[i] < reportModelDetail.visitedRNodesCounters[i]) {
                throw new RuntimeException("reportModelDetail.expectedRNodesCounters[i] < reportModelDetail.visitedRNodesCounters[i]");
            }

            if (reportModelDetail.visitedRNodesCounters[i] > 0) {
                reportModelDetail.visitedRNodesCounter += reportModelDetail.visitedRNodesCounters[i];
                reportModelDetail.expectedRNodesCounter += reportModelDetail.expectedRNodesCounters[i];

                this.visitedRNodesCounters[i] += reportModelDetail.visitedRNodesCounters[i];
                this.expectedRNodesCounters[i] += reportModelDetail.expectedRNodesCounters[i];

                if (reportModelDetail.expectedRNodesCounters[i] > 0) {
                    reportModelDetail.cutoffRPercentages[i] = (100 - (100 * reportModelDetail.visitedRNodesCounters[i] / reportModelDetail.expectedRNodesCounters[i]));
                }
            }
        }
    }

    private void collectQuiescenceNodeStatistics(SearchesReportModelDetail reportModelDetail, SearchResult searchResult) {
        NodeStatistics quiescenceNodeStatistics = searchResult.getQuiescenceNodeStatistics();
        reportModelDetail.expectedQNodesCounters = quiescenceNodeStatistics.expectedNodesCounters();
        reportModelDetail.visitedQNodesCounters = quiescenceNodeStatistics.visitedNodesCounters();
        reportModelDetail.cutoffQPercentages = new int[30];

        for (int i = 0; i < 30; i++) {
            if (reportModelDetail.expectedQNodesCounters[i] < reportModelDetail.visitedQNodesCounters[i]) {
                throw new RuntimeException("reportModelDetail.expectedQNodesCounters[i] < reportModelDetail.visitedQNodesCounters[i]");
            }
            if (reportModelDetail.visitedQNodesCounters[i] > 0) {
                reportModelDetail.visitedQNodesCounter += reportModelDetail.visitedQNodesCounters[i];
                this.visitedQNodesCounters[i] += reportModelDetail.visitedQNodesCounters[i];

                reportModelDetail.expectedQNodesCounter += reportModelDetail.expectedQNodesCounters[i];
                this.expectedQNodesCounters[i] += reportModelDetail.expectedQNodesCounters[i];

                if (reportModelDetail.expectedQNodesCounters[i] > 0) {
                    reportModelDetail.cutoffQPercentages[i] = (100 - (100 * reportModelDetail.visitedQNodesCounters[i] / reportModelDetail.expectedQNodesCounters[i]));
                }
            }
        }
    }
}
