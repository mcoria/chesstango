package net.chesstango.reports.search.nodes;

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
public class NodesModel implements Model<List<SearchResult>> {
    public String searchGroupName;

    public int searches;

    public long rootNodeCounterTotal;
    public long interiorNodeCounterTotal;
    public long quiescenceNodeCounterTotal;
    public long leafNodeCounterTotal;
    public long terminalNodeCounterTotal;
    public long loopNodeCounterTotal;
    public long egtbCounterTotal;
    public long nodeCounterTotal;


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

        public long rootNodeCounter;
        public long interiorNodeCounter;
        public long quiescenceNodeCounter;
        public long leafNodeCounter;
        public long terminalNodeCounter;
        public long loopNodeCounter;
        public long egtbCounter;

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
    public NodesModel collectStatistics(String searchGroupName, List<SearchResult> searchResults) {
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

        this.nodeCounterTotal = this.rootNodeCounterTotal
                + this.interiorNodeCounterTotal
                + this.quiescenceNodeCounterTotal
                + this.leafNodeCounterTotal
                + this.terminalNodeCounterTotal
                + this.loopNodeCounterTotal
                + this.egtbCounterTotal;
    }

    private void loadModelDetail(SearchResult searchResult) {
        NodesModelDetail reportModelDetail = new NodesModelDetail();

        Move bestMove = searchResult.getBestMove();
        reportModelDetail.id = searchResult.getId();
        reportModelDetail.move = bestMove != null ? SimpleMoveEncoder.INSTANCE.encode(bestMove) : "";

        if (searchResult.getRegularNodeStatistics() != null) {
            collectRegularNodeStatistics(reportModelDetail, searchResult.getRegularNodeStatistics());
        }

        this.nodesModelDetails.add(reportModelDetail);
    }

    private void collectRegularNodeStatistics(NodesModelDetail reportModelDetail, NodeStatistics regularNodeStatistics) {
        reportModelDetail.expectedNodesCounters = regularNodeStatistics.expectedNodesCounters();
        reportModelDetail.visitedNodesCounters = regularNodeStatistics.visitedNodesCounters();
        reportModelDetail.cutoffPercentages = new int[30];

        reportModelDetail.rootNodeCounter = regularNodeStatistics.rootNodeCounter();
        reportModelDetail.interiorNodeCounter = regularNodeStatistics.interiorNodeCounter();
        reportModelDetail.quiescenceNodeCounter = regularNodeStatistics.quiescenceCounter();
        reportModelDetail.leafNodeCounter = regularNodeStatistics.leafCounter();
        reportModelDetail.terminalNodeCounter = regularNodeStatistics.terminalNodeCounter();
        reportModelDetail.loopNodeCounter = regularNodeStatistics.loopNodeCounter();
        reportModelDetail.egtbCounter = regularNodeStatistics.egtbCounter();

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

        this.rootNodeCounterTotal += reportModelDetail.rootNodeCounter;
        this.interiorNodeCounterTotal += reportModelDetail.interiorNodeCounter;
        this.quiescenceNodeCounterTotal += reportModelDetail.quiescenceNodeCounter;
        this.leafNodeCounterTotal += reportModelDetail.leafNodeCounter;
        this.terminalNodeCounterTotal += reportModelDetail.terminalNodeCounter;
        this.loopNodeCounterTotal += reportModelDetail.loopNodeCounter;
        this.egtbCounterTotal += reportModelDetail.egtbCounter;
    }
}
