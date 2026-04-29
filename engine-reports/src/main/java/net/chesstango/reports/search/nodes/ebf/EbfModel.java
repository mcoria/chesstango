package net.chesstango.reports.search.nodes.ebf;

import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.reports.Model;
import net.chesstango.search.SearchResult;
import net.chesstango.search.smart.Constants;
import net.chesstango.search.smart.alphabeta.statistics.node.NodeStatistics;

import java.util.LinkedList;
import java.util.List;

import static net.chesstango.search.smart.Constants.MAX_DEPTH;

/**
 * @author Mauricio Coria
 */
public class EbfModel implements Model<List<SearchResult>> {
    public String searchGroupName;

    public int searches;

    public int maxIteration;

    public List<EbfModelDetail> nodesModelDetails;

    public static class EbfModelDetail {
        public String id;

        public String move;

        public int maxIteration;

        public float[] ebf;
    }

    @Override
    public EbfModel collectStatistics(String searchGroupName, List<SearchResult> searchResults) {
        this.searchGroupName = searchGroupName;

        this.load(searchResults);

        return this;
    }

    private void load(List<SearchResult> searchResults) {
        this.searches = searchResults.size();

        this.nodesModelDetails = new LinkedList<>();

        searchResults.forEach(this::loadModelDetail);
    }

    private void loadModelDetail(SearchResult searchResult) {
        EbfModelDetail ebfModelDetail = new EbfModelDetail();

        Move bestMove = searchResult.getBestMove();
        ebfModelDetail.id = searchResult.getId();
        ebfModelDetail.move = bestMove != null ? SimpleMoveEncoder.INSTANCE.encode(bestMove) : "";

        ebfModelDetail.maxIteration = searchResult.getSearchResultByDepths().size();

        if (searchResult.getNodeStatistics() != null) {
            collectEbfStatistics(ebfModelDetail, searchResult.getNodeStatistics());
        }

        if (this.maxIteration < ebfModelDetail.maxIteration) {
            this.maxIteration = ebfModelDetail.maxIteration;
        }

        this.nodesModelDetails.add(ebfModelDetail);
    }

    private void collectEbfStatistics(EbfModelDetail ebfModelDetail, NodeStatistics regularNodeStatistics) {
        ebfModelDetail.ebf = new float[MAX_DEPTH];

        long[] regularNodeCounters = regularNodeStatistics.regularNodeCounters();

        for (int i = 2; i < MAX_DEPTH; i++) {
            if (regularNodeCounters[i - 2] != 0) {
                ebfModelDetail.ebf[i] = (float) Math.sqrt((double) regularNodeCounters[i] / regularNodeCounters[i - 2]);
            }
        }
    }
}
