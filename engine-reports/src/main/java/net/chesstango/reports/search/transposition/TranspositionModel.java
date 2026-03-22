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

    public long readNodeHitsTotal;

    public long readComparatorHitsTotal;

    public int readNodeHitPercentageTotal;

    public int readComparatorHitPercentageTotal;

    public long writesTotal;

    public long updatesTotal;

    public int updatesPercentageTotal;

    public long overWritesTotal;

    public int overWritesPercentageTotal;

    public int maxMapFillPercentageAvg;

    public int minMapFillPercentageAvg;

    public List<TranspositionModelDetail> transpositionModelDetail;

    public static class TranspositionModelDetail {
        public String id;

        public String move;

        public long reads;

        public long readNodeHits;

        public long readComparatorHits;

        public int readNodeHitPercentage;

        public int readComparatorHitPercentage;

        public long writes;

        public long updates;

        public int updatesPercentage;

        public long overWrites;

        public int overWritePercentage;

        public int maxMapFillPercentage;

        public int minMapFillPercentage;
    }

    @Override
    public TranspositionModel collectStatistics(String searchGroupName, List<SearchResult> searchResults) {
        this.searchGroupName = searchGroupName;

        load(searchResults);

        return this;
    }

    private void load(List<SearchResult> searchResults) {
        this.transpositionModelDetail = new LinkedList<>();

        searchResults.forEach(this::loadModelDetail);

        this.readNodeHitPercentageTotal = readsTotal > 0 ? (int) (100 * readNodeHitsTotal / readsTotal) : 0;
        this.readComparatorHitPercentageTotal = readsTotal > 0 ? (int) (100 * readComparatorHitsTotal / readsTotal) : 0;
        this.updatesPercentageTotal = writesTotal > 0 ? (int) (100 * updatesTotal / writesTotal) : 0;
        this.overWritesPercentageTotal = writesTotal > 0 ? (int) (100 * overWritesTotal / writesTotal) : 0;

        this.maxMapFillPercentageAvg = (int) transpositionModelDetail.stream().mapToInt(detail -> detail.maxMapFillPercentage).average().orElse(0);
        this.minMapFillPercentageAvg = (int) transpositionModelDetail.stream().mapToInt(detail -> detail.minMapFillPercentage).average().orElse(0);
    }

    private void loadModelDetail(SearchResult searchResult) {
        TTableStatistics ttableStatistics = searchResult.getTTableStatistics();

        if (ttableStatistics != null) {
            TranspositionModelDetail transpositionModelDetail = new TranspositionModelDetail();

            Move bestMove = searchResult.getBestMove();
            transpositionModelDetail.id = searchResult.getId();
            transpositionModelDetail.move = bestMove != null ? SimpleMoveEncoder.INSTANCE.encode(bestMove) : "";

            transpositionModelDetail.reads = ttableStatistics.reads();
            transpositionModelDetail.readNodeHits = ttableStatistics.readNodeHits();
            transpositionModelDetail.readComparatorHits = ttableStatistics.readComparatorHits();
            transpositionModelDetail.readNodeHitPercentage = ttableStatistics.reads() > 0 ? (int) (100 * ttableStatistics.readNodeHits() / ttableStatistics.reads()) : 0;
            transpositionModelDetail.readComparatorHitPercentage = ttableStatistics.reads() > 0 ? (int) (100 * ttableStatistics.readComparatorHits() / ttableStatistics.reads()) : 0;
            transpositionModelDetail.writes = ttableStatistics.writes();
            transpositionModelDetail.updates = ttableStatistics.updates();
            transpositionModelDetail.updatesPercentage = ttableStatistics.writes() > 0 ? (int) (100 * ttableStatistics.updates() / ttableStatistics.writes()) : 0;
            transpositionModelDetail.overWrites = ttableStatistics.overWrites();
            transpositionModelDetail.overWritePercentage = ttableStatistics.writes() > 0 ? (int) (100 * ttableStatistics.overWrites() / ttableStatistics.writes()) : 0;
            transpositionModelDetail.maxMapFillPercentage = ttableStatistics.maxMapFillPercentage();
            transpositionModelDetail.minMapFillPercentage = ttableStatistics.minMapFillPercentage();


            this.searches++;
            this.readsTotal += transpositionModelDetail.reads;
            this.readNodeHitsTotal += transpositionModelDetail.readNodeHits;
            this.readComparatorHitsTotal += transpositionModelDetail.readComparatorHits;
            this.writesTotal += transpositionModelDetail.writes;
            this.updatesTotal += transpositionModelDetail.updates;
            this.overWritesTotal += transpositionModelDetail.overWrites;
            this.transpositionModelDetail.add(transpositionModelDetail);
        }
    }
}
