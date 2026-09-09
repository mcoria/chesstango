package net.chesstango.reports.search.evalcache;

import net.chesstango.board.moves.Move;
import net.chesstango.reports.Model;
import net.chesstango.search.SearchResult;
import net.chesstango.search.smart.statistics.evalcache.EvaluatorCacheStatistics;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class EvaluationCacheModel implements Model<List<SearchResult>> {
    public String searchGroupName;
    public int searches;

    /**
     * Statistics
     */
    public long readNodesTotal;
    public long readNodeHitsTotal;
    public long readNodeHitsPercentageTotal;

    public long readComparatorsTotal;
    public long readComparatorHitsTotal;
    public long readComparatorHitsPercentageTotal;

    public int fillPercentageAvg;

    /// ////////////////// END TOTALS

    public List<EvaluationCacheModelDetail> moveDetails;

    public static class EvaluationCacheModelDetail {
        public String id;

        public String move;

        public int evaluation;

        public long readNodes;
        public long readNodeHits;
        public long readNodeHitsPercentage;

        public long readComparators;
        public long readComparatorHits;
        public long readComparatorHitsPercentage;

        public int fillPercentage;
    }

    @Override
    public EvaluationCacheModel collectStatistics(String searchGroupName, List<SearchResult> searchResults) {
        this.searchGroupName = searchGroupName;

        this.load(searchResults);

        return this;
    }

    private void load(List<SearchResult> searchResults) {
        this.moveDetails = new LinkedList<>();
        this.searches = searchResults.size();

        searchResults.forEach(this::loadModelDetail);

        this.readNodesTotal = this.moveDetails.stream().mapToLong(detail -> detail.readNodes).sum();
        this.readNodeHitsTotal = this.moveDetails.stream().mapToLong(detail -> detail.readNodeHits).sum();
        this.readNodeHitsPercentageTotal = this.readNodesTotal > 0 ? this.readNodeHitsTotal * 100 / this.readNodesTotal : 0;
        this.readComparatorsTotal = this.moveDetails.stream().mapToLong(detail -> detail.readComparators).sum();
        this.readComparatorHitsTotal = this.moveDetails.stream().mapToLong(detail -> detail.readComparatorHits).sum();
        this.readComparatorHitsPercentageTotal = this.readComparatorsTotal > 0 ? this.readComparatorHitsTotal * 100 / this.readComparatorsTotal : 0;
        this.fillPercentageAvg = (int) this.moveDetails.stream().mapToInt(detail -> detail.fillPercentage).average().orElse(0);
    }

    private void loadModelDetail(SearchResult searchResult) {
        Move bestMove = searchResult.getBestMove();

        EvaluationCacheModelDetail reportModelDetail = new EvaluationCacheModelDetail();
        reportModelDetail.id = searchResult.getId();
        reportModelDetail.move = bestMove != null ? bestMove.coordinateEncoding() : "";
        reportModelDetail.evaluation = searchResult.getBestEvaluation() != null ? searchResult.getBestEvaluation() : 0;

        if (searchResult.getEvaluatorStatistics() != null) {
            collectEvaluationStatistics(reportModelDetail, searchResult.getEvaluatorCacheStatistics());
        }

        this.moveDetails.add(reportModelDetail);
    }


    private void collectEvaluationStatistics(EvaluationCacheModelDetail reportModelDetail, EvaluatorCacheStatistics evaluatorCacheStatistics) {
        reportModelDetail.readNodes = evaluatorCacheStatistics.readNodes();
        reportModelDetail.readNodeHits = evaluatorCacheStatistics.readNodeHits();
        reportModelDetail.readNodeHitsPercentage = reportModelDetail.readNodes > 0 ? reportModelDetail.readNodeHits * 100 / reportModelDetail.readNodes : 0;
        reportModelDetail.readComparators = evaluatorCacheStatistics.readComparators();
        reportModelDetail.readComparatorHits = evaluatorCacheStatistics.readComparatorHits();
        reportModelDetail.readComparatorHitsPercentage = reportModelDetail.readComparators > 0 ? reportModelDetail.readComparatorHits * 100 / reportModelDetail.readComparators : 0;
        reportModelDetail.fillPercentage = evaluatorCacheStatistics.fillPercentage();
    }

}
