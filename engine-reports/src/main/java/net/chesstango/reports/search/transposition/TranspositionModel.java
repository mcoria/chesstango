package net.chesstango.reports.search.transposition;

import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.search.SearchResult;
import net.chesstango.search.smart.features.statistics.transposition.TTableStatistics;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class TranspositionModel {
    public String searchGroupName;

    public int searches;

    public long hitsTotal;

    public long replacesTotal;

    public List<TranspositionModelDetail> transpositionModelDetail;

    public static class TranspositionModelDetail {
        public String id;

        public String move;

        public long hits;

        public long replaces;
    }

    public static TranspositionModel collectStatistics(String reportTitle, List<SearchResult> searchResults) {
        TranspositionModel model = new TranspositionModel();

        model.searchGroupName = reportTitle;

        model.load(searchResults);

        return model;
    }

    private void load(List<SearchResult> searchResults) {
        transpositionModelDetail = new LinkedList<>();
        searchResults.forEach(this::loadModelDetail);
    }

    private void loadModelDetail(SearchResult searchResult) {
        TTableStatistics ttableStatistics = searchResult.getTTableStatistics();

        if (ttableStatistics != null) {
            TranspositionModelDetail transpositionModelDetail = new TranspositionModelDetail();

            Move bestMove = searchResult.getBestMove();
            transpositionModelDetail.id = searchResult.getId();
            transpositionModelDetail.move = bestMove != null ? SimpleMoveEncoder.INSTANCE.encode(bestMove) : "";

            transpositionModelDetail.hits = ttableStatistics.tableHits();
            transpositionModelDetail.replaces = ttableStatistics.tableCollisions();

            this.searches++;
            this.hitsTotal += transpositionModelDetail.hits;
            this.replacesTotal += transpositionModelDetail.replaces;
            this.transpositionModelDetail.add(transpositionModelDetail);
        }
    }
}
