package net.chesstango.reports.search.transposition;

import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.reports.Model;
import net.chesstango.search.SearchResult;
import net.chesstango.search.smart.alphabeta.statistics.transposition.TTableStatistics;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class TranspositionModel implements Model<List<SearchResult>> {
    public String searchGroupName;

    public int searches;

    public long readsTotal;

    public long readHitsTotal;

    public int readHitPercentageTotal;

    public long writesTotal;

    public long overWritesTotal;

    public int overWritePercentageTotal;

    public List<TranspositionModelDetail> transpositionModelDetail;

    public static class TranspositionModelDetail {
        public String id;

        public String move;

        public long reads;

        public long readHits;

        public int readHitPercentage;

        public long writes;

        public long overWrites;

        public int overWritePercentage;

    }

    @Override
    public TranspositionModel collectStatistics(String searchGroupName, List<SearchResult> searchResults) {
        this.searchGroupName = searchGroupName;

        load(searchResults);

        return this;
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

            transpositionModelDetail.reads = ttableStatistics.reads();
            transpositionModelDetail.readHits = ttableStatistics.readHits();
            transpositionModelDetail.readHitPercentage = ttableStatistics.reads() > 0 ? (int) (100 * ttableStatistics.readHits() / ttableStatistics.reads()) : 0;
            transpositionModelDetail.writes = ttableStatistics.writes();
            transpositionModelDetail.overWrites = ttableStatistics.overWrites();
            transpositionModelDetail.overWritePercentage = ttableStatistics.writes() > 0 ? (int) (100 * ttableStatistics.overWrites() / ttableStatistics.writes()) : 0;


            this.searches++;
            this.readsTotal += transpositionModelDetail.reads;
            this.readHitsTotal += transpositionModelDetail.readHits;
            this.readHitPercentageTotal =  readsTotal > 0 ? (int) (100 * readHitsTotal / readsTotal) : 0;
            this.writesTotal += transpositionModelDetail.writes;
            this.overWritesTotal += transpositionModelDetail.overWrites;
            this.overWritePercentageTotal =  writesTotal > 0 ? (int) (100 * overWritesTotal / writesTotal) : 0;
            this.transpositionModelDetail.add(transpositionModelDetail);
        }
    }
}
