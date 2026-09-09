package net.chesstango.reports.search.transposition;

import net.chesstango.board.moves.Move;
import net.chesstango.reports.Model;
import net.chesstango.search.SearchResult;
import net.chesstango.search.smart.statistics.transposition.TTableStatistics;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class TranspositionModel implements Model<List<SearchResult>> {
    public String searchGroupName;

    public int searches;

    public long readsNodeTotal;

    public long readNodeHitsTotal;

    public int readNodeHitPercentageTotal;

    public long readComparatorTotal;

    public long readComparatorHitsTotal;

    public int readComparatorHitPercentageTotal;

    public long writesTotal;

    public long updatesTotal;

    public int updatesPercentageTotal;

    public long overWritesTotal;

    public int overWritesPercentageTotal;

    public int mapFillPercentageAvg;

    public List<TranspositionModelDetail> transpositionModelDetail;

    public static class TranspositionModelDetail {
        public String id;

        public String move;

        public long readNodes;

        public long readNodeHits;

        public int readNodeHitPercentage;

        public long readComparators;

        public long readComparatorHits;

        public int readComparatorHitPercentage;

        public long writes;

        public long updates;

        public int updatesPercentage;

        public long overWrites;

        public int overWritePercentage;

        public int mapFillPercentage;
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

        this.readNodeHitPercentageTotal = readsNodeTotal > 0 ? (int) (100 * readNodeHitsTotal / readsNodeTotal) : 0;
        this.readComparatorHitPercentageTotal = readComparatorTotal > 0 ? (int) (100 * readComparatorHitsTotal / readComparatorTotal) : 0;
        this.updatesPercentageTotal = writesTotal > 0 ? (int) (100 * updatesTotal / writesTotal) : 0;
        this.overWritesPercentageTotal = writesTotal > 0 ? (int) (100 * overWritesTotal / writesTotal) : 0;

        this.mapFillPercentageAvg = (int) transpositionModelDetail.stream().mapToInt(detail -> detail.mapFillPercentage).average().orElse(0);
    }

    private void loadModelDetail(SearchResult searchResult) {
        TTableStatistics ttableStatistics = searchResult.getTTableStatistics();

        if (ttableStatistics != null) {
            TranspositionModelDetail transpositionModelDetail = new TranspositionModelDetail();

            Move bestMove = searchResult.getBestMove();
            transpositionModelDetail.id = searchResult.getId();
            transpositionModelDetail.move = bestMove != null ? bestMove.coordinateEncoding() : "";

            transpositionModelDetail.readNodes = ttableStatistics.readNodes();
            transpositionModelDetail.readNodeHits = ttableStatistics.readNodeHits();
            transpositionModelDetail.readNodeHitPercentage = ttableStatistics.readNodes() > 0 ? (int) (100 * ttableStatistics.readNodeHits() / ttableStatistics.readNodes()) : 0;

            transpositionModelDetail.readComparators = ttableStatistics.readComparators();
            transpositionModelDetail.readComparatorHits = ttableStatistics.readComparatorHits();
            transpositionModelDetail.readComparatorHitPercentage = ttableStatistics.readComparators() > 0 ? (int) (100 * ttableStatistics.readComparatorHits() / ttableStatistics.readComparators()) : 0;

            transpositionModelDetail.writes = ttableStatistics.writes();
            transpositionModelDetail.updates = ttableStatistics.updates();
            transpositionModelDetail.updatesPercentage = ttableStatistics.writes() > 0 ? (int) (100 * ttableStatistics.updates() / ttableStatistics.writes()) : 0;
            transpositionModelDetail.overWrites = ttableStatistics.overWrites();
            transpositionModelDetail.overWritePercentage = ttableStatistics.writes() > 0 ? (int) (100 * ttableStatistics.overWrites() / ttableStatistics.writes()) : 0;
            transpositionModelDetail.mapFillPercentage = ttableStatistics.mapFillPercentage();

            this.searches++;
            this.readsNodeTotal += transpositionModelDetail.readNodes;
            this.readNodeHitsTotal += transpositionModelDetail.readNodeHits;
            this.readComparatorTotal += transpositionModelDetail.readComparators;
            this.readComparatorHitsTotal += transpositionModelDetail.readComparatorHits;
            this.writesTotal += transpositionModelDetail.writes;
            this.updatesTotal += transpositionModelDetail.updates;
            this.overWritesTotal += transpositionModelDetail.overWrites;
            this.transpositionModelDetail.add(transpositionModelDetail);
        }
    }
}
