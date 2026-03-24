package net.chesstango.reports.search.evaluation.cache;

import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.reports.Model;
import net.chesstango.search.SearchResult;
import net.chesstango.search.smart.alphabeta.statistics.evaluation.EvaluationStatistics;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class EvaluationCacheModel implements Model<List<SearchResult>> {
    public String searchGroupName;
    public int searches;

    /**
     * Evaluation Statistics
     */
    public long evaluationCounterTotal;
    public long evaluationsCacheHitsCounterTotal;
    public long evaluationsCacheHitsPercentageTotal;
    public long readFromCacheCounterTotal;
    public long readFromCacheHitsCounterTotal;
    public long readFromCacheHitsPercentageTotal;

    public int fillPercentageAvg;

    /// ////////////////// END TOTALS

    public List<EvaluationCacheModelDetail> moveDetails;

    public static class EvaluationCacheModelDetail {
        public String id;

        public String move;

        public int evaluation;


        public long evaluationCounter;
        public long evaluationsCacheHitsCounter;
        public long evaluationsCacheHitsPercentage;
        public long readFromCacheCounter;
        public long readFromCacheHitsCounter;
        public long readFromCacheHitsPercentage;

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

        this.evaluationCounterTotal = this.moveDetails.stream().mapToLong(detail -> detail.evaluationCounter).sum();
        this.evaluationsCacheHitsCounterTotal = this.moveDetails.stream().mapToLong(detail -> detail.evaluationsCacheHitsCounter).sum();
        this.evaluationsCacheHitsPercentageTotal = this.evaluationsCacheHitsCounterTotal * 100 / this.evaluationCounterTotal;
        this.readFromCacheCounterTotal = this.moveDetails.stream().mapToLong(detail -> detail.readFromCacheCounter).sum();
        this.readFromCacheHitsCounterTotal = this.moveDetails.stream().mapToLong(detail -> detail.readFromCacheHitsCounter).sum();
        this.readFromCacheHitsPercentageTotal = this.readFromCacheHitsCounterTotal * 100 / this.readFromCacheCounterTotal;
        this.fillPercentageAvg = (int) this.moveDetails.stream().mapToInt(detail -> detail.fillPercentage).average().orElse(0);
    }

    private void loadModelDetail(SearchResult searchResult) {
        Move bestMove = searchResult.getBestMove();
        SimpleMoveEncoder simpleMoveEncoder = new SimpleMoveEncoder();

        EvaluationCacheModelDetail reportModelDetail = new EvaluationCacheModelDetail();
        reportModelDetail.id = searchResult.getId();
        reportModelDetail.move = bestMove != null ? simpleMoveEncoder.encode(bestMove) : "";
        reportModelDetail.evaluation = searchResult.getBestEvaluation() != null ? searchResult.getBestEvaluation() : 0;

        if (searchResult.getEvaluationStatistics() != null) {
            collectEvaluationStatistics(reportModelDetail, searchResult.getEvaluationStatistics());
        }

        this.moveDetails.add(reportModelDetail);
    }


    private void collectEvaluationStatistics(EvaluationCacheModelDetail reportModelDetail, EvaluationStatistics evaluationStatistics) {
        reportModelDetail.evaluationCounter = evaluationStatistics.evaluationsCounter();
        reportModelDetail.evaluationsCacheHitsCounter = evaluationStatistics.evaluationsCacheHitsCounter();
        reportModelDetail.evaluationsCacheHitsPercentage = reportModelDetail.evaluationCounter > 0 ? reportModelDetail.evaluationsCacheHitsCounter * 100 / reportModelDetail.evaluationCounter : 0;
        reportModelDetail.readFromCacheCounter = evaluationStatistics.readFromCacheCounter();
        reportModelDetail.readFromCacheHitsCounter = evaluationStatistics.readFromCacheHitsCounter();
        reportModelDetail.readFromCacheHitsPercentage = reportModelDetail.readFromCacheCounter > 0 ? reportModelDetail.readFromCacheHitsCounter * 100 / reportModelDetail.readFromCacheCounter : 0;
        reportModelDetail.fillPercentage = evaluationStatistics.fillPercentage();
    }

}
