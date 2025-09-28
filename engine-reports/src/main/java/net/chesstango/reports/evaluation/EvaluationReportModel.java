package net.chesstango.reports.evaluation;

import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.search.SearchResult;
import net.chesstango.search.smart.features.statistics.evaluation.EvaluationEntry;
import net.chesstango.search.smart.features.statistics.evaluation.EvaluationStatistics;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author Mauricio Coria
 */
public class EvaluationReportModel {
    public String reportTitle;

    /**
     * Evaluation Statistics
     */
    public long evaluationCounterTotal;
    public long evaluationsCacheHitCounterTotal;
    public long evaluationPositionCounterTotal;
    public long evaluationValueCounterTotal;
    public long evaluationPositionValueCollisionsCounterTotal;
    public int evaluationCollisionPercentageTotal;

    ///////////////////// END TOTALS


    public List<EvaluationReportModelDetail> moveDetails;

    public static class EvaluationReportModelDetail {
        public String id;

        public String move;

        public int evaluation;

        /**
         * Evaluation Statistics
         */
        public Set<EvaluationEntry> evaluations;

        /**
         * Contador de evaluaciones se hicieron
         */
        public long evaluationCounter;

        /**
         * Contador de evaluaciones que fueron encontradas en cache
         */
        public long evaluationsCacheHitCounter;

        /**
         * Contador de evaluacion de posiciones (distintas) se hicieron
         */
        public long evaluationPositionCounter;

        /**
         * Contador de evaluacion de posiciones (distintas) resultaron en distinto valor
         */
        public long evaluationValueCounter;

        /**
         * Contador de evaluacion de posiciones (distintas) cuya evaluacion coincide
         */
        public long evaluationPositionValueCollisionsCounter;


        /**
         * Contador de evaluacion de posiciones (distintas) cuya evaluacion coincide
         */
        public int evaluationPositionValueCollisionsPercentage;

    }


    public static EvaluationReportModel collectStatistics(String reportTitle, List<SearchResult> searchResults) {
        EvaluationReportModel searchesReportModel = new EvaluationReportModel();

        searchesReportModel.reportTitle = reportTitle;

        searchesReportModel.load(searchResults);

        return searchesReportModel;
    }

    private void load(List<SearchResult> searchResults) {
        this.moveDetails = new LinkedList<>();

        searchResults.forEach(this::loadModelDetail);
    }

    private void loadModelDetail(SearchResult searchResult) {
        Move bestMove = searchResult.getBestMove();
        SimpleMoveEncoder simpleMoveEncoder = new SimpleMoveEncoder();

        EvaluationReportModelDetail reportModelDetail = new EvaluationReportModelDetail();
        reportModelDetail.id = searchResult.getId();
        reportModelDetail.move = bestMove != null ? simpleMoveEncoder.encode(bestMove) : "";
        reportModelDetail.evaluation = searchResult.getBestEvaluation() != null ? searchResult.getBestEvaluation() : 0;

        if (searchResult.getEvaluationStatistics() != null) {
            collectEvaluationStatistics(reportModelDetail, searchResult);
        }

        this.evaluationCounterTotal += reportModelDetail.evaluationCounter;
        this.evaluationsCacheHitCounterTotal += reportModelDetail.evaluationsCacheHitCounter;
        this.evaluationPositionCounterTotal += reportModelDetail.evaluationPositionCounter;
        this.evaluationValueCounterTotal += reportModelDetail.evaluationValueCounter;
        this.evaluationPositionValueCollisionsCounterTotal += reportModelDetail.evaluationPositionValueCollisionsCounter;
        if (this.evaluationPositionCounterTotal > 0) {
            this.evaluationCollisionPercentageTotal = (int) ((100 * this.evaluationPositionValueCollisionsCounterTotal) / this.evaluationPositionCounterTotal);
        }
        this.moveDetails.add(reportModelDetail);
    }


    private void collectEvaluationStatistics(EvaluationReportModelDetail reportModelDetail, SearchResult searchResult) {
        EvaluationStatistics evaluationStatistics = searchResult.getEvaluationStatistics();

        reportModelDetail.evaluationCounter = evaluationStatistics.evaluationsCounter();
        reportModelDetail.evaluationsCacheHitCounter = evaluationStatistics.cacheHitsCounter();

        Set<EvaluationEntry> evaluations = evaluationStatistics.evaluations();
        if (evaluations != null) {
            reportModelDetail.evaluations = evaluations;
            reportModelDetail.evaluationPositionCounter = evaluations.size();
            reportModelDetail.evaluationValueCounter = evaluations.stream().mapToInt(EvaluationEntry::value).distinct().count();
            reportModelDetail.evaluationPositionValueCollisionsCounter = reportModelDetail.evaluationPositionCounter - reportModelDetail.evaluationValueCounter;

            /*
             * Cuando TT reuse está habilitado y depth=1 se puede dar que no se evaluan algunas posiciones
             */
            if (reportModelDetail.evaluationPositionCounter != 0) {
                reportModelDetail.evaluationPositionValueCollisionsPercentage = (int) ((100 * reportModelDetail.evaluationPositionValueCollisionsCounter) / reportModelDetail.evaluationPositionCounter);
            }
        }
    }

}
