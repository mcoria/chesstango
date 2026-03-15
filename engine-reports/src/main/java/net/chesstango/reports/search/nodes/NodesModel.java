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
    public long nodeCounterTotal;


    /// ////////////////// START REGULAR NODES
    public int maxSearchRLevel;
    public long[] expectedRNodesCounters;
    public long[] visitedRNodesCounters;
    public int[] cutoffRPercentages;
    public long expectedRNodesTotal;
    public long visitedRNodesTotal;
    public int cutoffPercentageTotal;
    ///////////////////// END REGULAR NODES

    /// ////////////////// START QUIESCENCE NODES
    //public int maxSearchQLevel;
    /// ////////////////// END QUIESCENCE NODES

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

        /**
         * Node Statistics
         */
        public long[] expectedRNodesCounters;
        public long expectedRNodesCounter;
        public long[] visitedRNodesCounters;
        public long visitedRNodesCounter;
        public int[] cutoffRPercentages;
        public int cutoffPercentageTotal;
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

        this.expectedRNodesCounters = new long[30];
        this.visitedRNodesCounters = new long[30];
        this.cutoffRPercentages = new int[30];

        searchResults.forEach(this::loadModelDetail);

        /**
         * Totales sumarizados
         */
        for (int i = 0; i < 30; i++) {
            if (this.visitedRNodesCounters[i] > 0) {
                this.cutoffRPercentages[i] = (int) (100 - (100 * this.visitedRNodesCounters[i] / this.expectedRNodesCounters[i]));
                this.maxSearchRLevel = i + 1;
            }
            this.visitedRNodesTotal += this.visitedRNodesCounters[i];
            this.expectedRNodesTotal += this.expectedRNodesCounters[i];
        }

        if (this.expectedRNodesTotal > 0) {
            this.cutoffPercentageTotal = (int) (100 - (100 * this.visitedRNodesTotal / this.expectedRNodesTotal));
        }

        this.nodeCounterTotal = this.rootNodeCounterTotal
                + this.interiorNodeCounterTotal
                + this.quiescenceNodeCounterTotal
                + this.leafNodeCounterTotal
                + this.terminalNodeCounterTotal
                + this.loopNodeCounterTotal;
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
        reportModelDetail.expectedRNodesCounters = regularNodeStatistics.expectedNodesCounters();
        reportModelDetail.visitedRNodesCounters = regularNodeStatistics.visitedNodesCounters();
        reportModelDetail.cutoffRPercentages = new int[30];

        reportModelDetail.rootNodeCounter = regularNodeStatistics.rootNodeCounter();
        reportModelDetail.interiorNodeCounter = regularNodeStatistics.interiorNodeCounter();
        reportModelDetail.quiescenceNodeCounter = regularNodeStatistics.quiescenceCounter();
        reportModelDetail.leafNodeCounter = regularNodeStatistics.leafCounter();
        reportModelDetail.terminalNodeCounter = regularNodeStatistics.terminalNodeCounter();
        reportModelDetail.loopNodeCounter = regularNodeStatistics.loopNodeCounter();

        for (int i = 0; i < 30; i++) {
            if (reportModelDetail.expectedRNodesCounters[i] < reportModelDetail.visitedRNodesCounters[i]) {
                throw new RuntimeException(String.format("reportModelDetail.expectedRNodesCounters[%d] (%d) < reportModelDetail.visitedRNodesCounters[%d] (%d)", i, reportModelDetail.expectedRNodesCounters[i], i, reportModelDetail.visitedRNodesCounters[i]));
            }

            if (reportModelDetail.visitedRNodesCounters[i] > 0) {
                reportModelDetail.visitedRNodesCounter += reportModelDetail.visitedRNodesCounters[i];
                reportModelDetail.expectedRNodesCounter += reportModelDetail.expectedRNodesCounters[i];

                this.visitedRNodesCounters[i] += reportModelDetail.visitedRNodesCounters[i];
                this.expectedRNodesCounters[i] += reportModelDetail.expectedRNodesCounters[i];

                if (reportModelDetail.expectedRNodesCounters[i] > 0) {
                    reportModelDetail.cutoffRPercentages[i] = (int) (100 - (100 * reportModelDetail.visitedRNodesCounters[i] / reportModelDetail.expectedRNodesCounters[i]));
                }
            }
        }

        this.rootNodeCounterTotal += reportModelDetail.rootNodeCounter;
        this.interiorNodeCounterTotal += reportModelDetail.interiorNodeCounter;
        this.quiescenceNodeCounterTotal += reportModelDetail.quiescenceNodeCounter;
        this.leafNodeCounterTotal += reportModelDetail.leafNodeCounter;
        this.terminalNodeCounterTotal += reportModelDetail.terminalNodeCounter;
        this.loopNodeCounterTotal += reportModelDetail.loopNodeCounter;
    }
}
