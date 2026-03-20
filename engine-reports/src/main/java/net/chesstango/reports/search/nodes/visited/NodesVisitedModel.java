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
    public int maxSelDepth;
    public long[] expectedRNodesCounters;
    public long[] visitedRNodesCounters;
    public int[] cutoffRPercentages;
    public long expectedRNodesTotal;
    public long visitedRNodesTotal;
    public int cutoffRPercentageTotal;
    /// ////////////////// END REGULAR NODES


    public List<NodesModelDetail> nodesModelDetails;

    public static class NodesModelDetail {
        public String id;

        public String move;

        /**
         * Node Statistics
         */
        public int selDepth;
        public long[] expectedRNodesCounters;
        public long expectedRNodesCounter;
        public long[] visitedRNodesCounters;
        public long visitedRNodesCounter;
        public int[] cutoffRPercentages;
        public int cutoffRPercentage;
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

        this.expectedRNodesCounters = new long[NodeStatistics.MAX_DEPTH];
        this.visitedRNodesCounters = new long[NodeStatistics.MAX_DEPTH];
        this.cutoffRPercentages = new int[NodeStatistics.MAX_DEPTH];

        searchResults.forEach(this::loadModelDetail);

        /**
         * Totales sumarizados
         */
        for (int i = 0; i < NodeStatistics.MAX_DEPTH; i++) {
            if (this.visitedRNodesCounters[i] > 0) {
                this.cutoffRPercentages[i] = (int) (100 - (100 * this.visitedRNodesCounters[i] / this.expectedRNodesCounters[i]));
                this.maxSelDepth = i;
            }
            this.visitedRNodesTotal += this.visitedRNodesCounters[i];
            this.expectedRNodesTotal += this.expectedRNodesCounters[i];
        }

        if (this.expectedRNodesTotal > 0) {
            this.cutoffRPercentageTotal = (int) (100 - (100 * this.visitedRNodesTotal / this.expectedRNodesTotal));
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
        reportModelDetail.expectedRNodesCounters = regularNodeStatistics.expectedRNodesCounters();
        reportModelDetail.visitedRNodesCounters = regularNodeStatistics.visitedRNodesCounters();
        reportModelDetail.cutoffRPercentages = new int[NodeStatistics.MAX_DEPTH];

        for (int i = 0; i < NodeStatistics.MAX_DEPTH; i++) {
            if (reportModelDetail.expectedRNodesCounters[i] < reportModelDetail.visitedRNodesCounters[i]) {
                throw new RuntimeException(String.format("reportModelDetail.expectedRNodesCounters[%d] (%d) < reportModelDetail.visitedRNodesCounters[%d] (%d)", i, reportModelDetail.expectedRNodesCounters[i], i, reportModelDetail.visitedRNodesCounters[i]));
            }

            if (reportModelDetail.visitedRNodesCounters[i] > 0) {
                reportModelDetail.selDepth = i;
                reportModelDetail.visitedRNodesCounter += reportModelDetail.visitedRNodesCounters[i];
                reportModelDetail.expectedRNodesCounter += reportModelDetail.expectedRNodesCounters[i];

                this.visitedRNodesCounters[i] += reportModelDetail.visitedRNodesCounters[i];
                this.expectedRNodesCounters[i] += reportModelDetail.expectedRNodesCounters[i];

                if (reportModelDetail.expectedRNodesCounters[i] > 0) {
                    reportModelDetail.cutoffRPercentages[i] = (int) (100 - (100 * reportModelDetail.visitedRNodesCounters[i] / reportModelDetail.expectedRNodesCounters[i]));
                }
            }
        }

        if (reportModelDetail.expectedRNodesCounter > 0) {
            reportModelDetail.cutoffRPercentage = (int) (100 - (100 * reportModelDetail.visitedRNodesCounter / reportModelDetail.expectedRNodesCounter));
        }
    }
}
