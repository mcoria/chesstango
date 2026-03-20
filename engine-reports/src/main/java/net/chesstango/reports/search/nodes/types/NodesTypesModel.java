package net.chesstango.reports.search.nodes.types;

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
public class NodesTypesModel implements Model<List<SearchResult>> {
    public String searchGroupName;

    public int searches;

    public long rootNodeCounterTotal;
    public long interiorNodeCounterTotal;
    public long quiescenceNodeCounterTotal;
    public long leafNodeCounterTotal;
    public long terminalNodeCounterTotal;
    public long loopNodeCounterTotal;
    public long egtbNodeCounterTotal;
    public long nodeCounterTotal;

    public int interiorNodeCounterPercentage;
    public int quiescenceNodeCounterPercentage;
    public int leafNodeCounterPercentage;

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
        public long egtbNodeCounter;
        public long nodeCounter;

        public int interiorNodeCounterPercentage;
        public int quiescenceNodeCounterPercentage;
        public int leafNodeCounterPercentage;
    }

    @Override
    public NodesTypesModel collectStatistics(String searchGroupName, List<SearchResult> searchResults) {
        this.searchGroupName = searchGroupName;

        this.load(searchResults);

        return this;
    }

    private void load(List<SearchResult> searchResults) {
        this.searches = searchResults.size();

        this.nodesModelDetails = new LinkedList<>();

        searchResults.forEach(this::loadModelDetail);

        this.interiorNodeCounterPercentage = (int) (100 * this.interiorNodeCounterTotal / this.nodeCounterTotal);
        this.quiescenceNodeCounterPercentage = (int) (100 * this.quiescenceNodeCounterTotal / this.nodeCounterTotal);
        this.leafNodeCounterPercentage = (int) (100 * this.leafNodeCounterTotal / this.nodeCounterTotal);
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
        reportModelDetail.rootNodeCounter = regularNodeStatistics.rootNodeCounter();
        reportModelDetail.interiorNodeCounter = regularNodeStatistics.interiorNodeCounter();
        reportModelDetail.quiescenceNodeCounter = regularNodeStatistics.quiescenceCounter();
        reportModelDetail.leafNodeCounter = regularNodeStatistics.leafCounter();
        reportModelDetail.terminalNodeCounter = regularNodeStatistics.terminalNodeCounter();
        reportModelDetail.loopNodeCounter = regularNodeStatistics.loopNodeCounter();
        reportModelDetail.egtbNodeCounter = regularNodeStatistics.egtbCounter();
        reportModelDetail.nodeCounter = reportModelDetail.rootNodeCounter
                + reportModelDetail.interiorNodeCounter
                + reportModelDetail.quiescenceNodeCounter
                + reportModelDetail.leafNodeCounter
                + reportModelDetail.terminalNodeCounter
                + reportModelDetail.loopNodeCounter
                + reportModelDetail.egtbNodeCounter;

        reportModelDetail.interiorNodeCounterPercentage = (int) (100 * reportModelDetail.interiorNodeCounter / reportModelDetail.nodeCounter);
        reportModelDetail.quiescenceNodeCounterPercentage = (int) (100 * reportModelDetail.quiescenceNodeCounter / reportModelDetail.nodeCounter);
        reportModelDetail.leafNodeCounterPercentage = (int) (100 * reportModelDetail.leafNodeCounter / reportModelDetail.nodeCounter);

        this.rootNodeCounterTotal += reportModelDetail.rootNodeCounter;
        this.interiorNodeCounterTotal += reportModelDetail.interiorNodeCounter;
        this.quiescenceNodeCounterTotal += reportModelDetail.quiescenceNodeCounter;
        this.leafNodeCounterTotal += reportModelDetail.leafNodeCounter;
        this.terminalNodeCounterTotal += reportModelDetail.terminalNodeCounter;
        this.loopNodeCounterTotal += reportModelDetail.loopNodeCounter;
        this.egtbNodeCounterTotal += reportModelDetail.egtbNodeCounter;
        this.nodeCounterTotal += reportModelDetail.nodeCounter;
    }
}
