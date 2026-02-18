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

    public long readHitsTotal;

    public long readsTotal;

    public long overWritesTotal;

    public long writesTotal;

    public List<TranspositionModelDetail> transpositionModelDetail;

    public static class TranspositionModelDetail {
        public String id;

        public String move;

        public long readHits;

        public long reads;

        public long overWrites;

        public long writes;
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

            transpositionModelDetail.readHits = ttableStatistics.readHits();
            transpositionModelDetail.reads = ttableStatistics.reads();
            transpositionModelDetail.overWrites = ttableStatistics.overWrites();
            transpositionModelDetail.writes = ttableStatistics.writes();

            this.searches++;
            this.readHitsTotal += transpositionModelDetail.readHits;
            this.readsTotal += transpositionModelDetail.reads;
            this.overWritesTotal += transpositionModelDetail.overWrites;
            this.writesTotal += transpositionModelDetail.writes;
            this.transpositionModelDetail.add(transpositionModelDetail);
        }
    }
}
