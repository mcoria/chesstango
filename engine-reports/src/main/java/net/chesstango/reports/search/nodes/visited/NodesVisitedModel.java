package net.chesstango.reports.search.nodes.visited;

import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.reports.Model;
import net.chesstango.search.SearchResult;
import net.chesstango.search.smart.alphabeta.statistics.node.NodeStatistics;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class NodesVisitedModel implements Model<List<SearchResult>> {
    public String searchGroupName;

    public int searches;

    /// ////////////////// START REGULAR NODES
    public int maxDepth;
    public long[] expectedNodesCounters;
    public long[] visitedNodesCounters;
    public int[] cutoffPercentages;
    public long expectedNodesTotal;
    public long visitedNodesTotal;
    public int cutoffPercentageTotal;
    /// ////////////////// END REGULAR NODES


    public List<NodesModelDetail> nodesModelDetails;

    public static class NodesModelDetail {
        public String id;

        public String move;

        /**
         * Node Statistics
         */
        public int maxDepth;
        public long[] expectedNodesCounters;
        public long expectedNodesCounter;
        public long[] visitedNodesCounters;
        public long visitedNodesCounter;
        public int[] cutoffPercentages;
        public int cutoffPercentage;
    }

    @Override
    public NodesVisitedModel collectStatistics(String searchGroupName, List<SearchResult> searchResults) {
        this.searchGroupName = searchGroupName;

        this.load(searchResults);

        return this;
    }

    private void load(List<SearchResult> searchResults) {
        this.searches = searchResults.size();

        this.nodesModelDetails = new LinkedList<>();

        this.expectedNodesCounters = new long[30];
        this.visitedNodesCounters = new long[30];
        this.cutoffPercentages = new int[30];

        searchResults.forEach(this::loadModelDetail);

        /**
         * Totales sumarizados
         */
        for (int i = 0; i < 30; i++) {
            if (this.visitedNodesCounters[i] > 0) {
                this.cutoffPercentages[i] = (int) (100 - (100 * this.visitedNodesCounters[i] / this.expectedNodesCounters[i]));
                this.maxDepth = i;
            }
            this.visitedNodesTotal += this.visitedNodesCounters[i];
            this.expectedNodesTotal += this.expectedNodesCounters[i];
        }

        if (this.expectedNodesTotal > 0) {
            this.cutoffPercentageTotal = (int) (100 - (100 * this.visitedNodesTotal / this.expectedNodesTotal));
        }
    }

    private void loadModelDetail(SearchResult searchResult) {
        NodesModelDetail reportModelDetail = new NodesModelDetail();

        Move bestMove = searchResult.getBestMove();
        reportModelDetail.id = searchResult.getId();
        reportModelDetail.move = bestMove != null ? SimpleMoveEncoder.INSTANCE.encode(bestMove) : "";

        if (searchResult.getNodeStatistics() != null) {
            collectRegularNodeStatistics(reportModelDetail, searchResult.getNodeStatistics());
        }

        this.nodesModelDetails.add(reportModelDetail);
    }

    private void collectRegularNodeStatistics(NodesModelDetail reportModelDetail, NodeStatistics regularNodeStatistics) {
        reportModelDetail.expectedNodesCounters = regularNodeStatistics.expectedNodesCounters();
        reportModelDetail.visitedNodesCounters = regularNodeStatistics.visitedNodesCounters();
        reportModelDetail.cutoffPercentages = new int[30];

        for (int i = 0; i < 30; i++) {
            if (reportModelDetail.expectedNodesCounters[i] < reportModelDetail.visitedNodesCounters[i]) {
                throw new RuntimeException(String.format("reportModelDetail.expectedNodesCounters[%d] (%d) < reportModelDetail.visitedNodesCounters[%d] (%d)", i, reportModelDetail.expectedNodesCounters[i], i, reportModelDetail.visitedNodesCounters[i]));
            }

            if (reportModelDetail.visitedNodesCounters[i] > 0) {
                reportModelDetail.maxDepth = i;
                reportModelDetail.visitedNodesCounter += reportModelDetail.visitedNodesCounters[i];
                reportModelDetail.expectedNodesCounter += reportModelDetail.expectedNodesCounters[i];

                this.visitedNodesCounters[i] += reportModelDetail.visitedNodesCounters[i];
                this.expectedNodesCounters[i] += reportModelDetail.expectedNodesCounters[i];

                if (reportModelDetail.expectedNodesCounters[i] > 0) {
                    reportModelDetail.cutoffPercentages[i] = (int) (100 - (100 * reportModelDetail.visitedNodesCounters[i] / reportModelDetail.expectedNodesCounters[i]));
                }
            }
        }

        if (reportModelDetail.expectedNodesCounter > 0) {
            reportModelDetail.cutoffPercentage = (int) (100 - (100 * reportModelDetail.visitedNodesCounter / reportModelDetail.expectedNodesCounter));
        }
    }
}
