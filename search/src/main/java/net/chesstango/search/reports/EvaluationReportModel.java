package net.chesstango.search.reports;

import net.chesstango.board.moves.Move;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.statistics.EvaluationEntry;
import net.chesstango.search.smart.statistics.EvaluationStatistics;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author Mauricio Coria
 */
public class EvaluationReportModel {
    public String engineName;

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


    public List<EvolutionReportModelDetail> moveDetails;

    public static class EvolutionReportModelDetail {
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


    public static EvaluationReportModel collectStatistics(String engineName, List<SearchMoveResult> searchMoveResults) {
        EvaluationReportModel searchesReportModel = new EvaluationReportModel();

        searchesReportModel.engineName = engineName;

        searchesReportModel.load(searchMoveResults);

        return searchesReportModel;
    }

    private void load(List<SearchMoveResult> searchMoveResults) {
        this.moveDetails = new LinkedList<>();

        searchMoveResults.forEach(this::loadModelDetail);
    }

    private void loadModelDetail(SearchMoveResult searchMoveResult) {
        Move bestMove = searchMoveResult.getBestMove();

        EvolutionReportModelDetail reportModelDetail = new EvolutionReportModelDetail();
        reportModelDetail.id = searchMoveResult.getEpdID();
        reportModelDetail.move = String.format("%s%s", bestMove.getFrom().getSquare(), bestMove.getTo().getSquare());
        reportModelDetail.evaluation = searchMoveResult.getEvaluation();

        if (searchMoveResult.getEvaluationStatistics() != null) {
            collectEvaluationStatistics(reportModelDetail, searchMoveResult);
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


    private void collectEvaluationStatistics(EvolutionReportModelDetail reportModelDetail, SearchMoveResult searchMoveResult) {
        EvaluationStatistics evaluationStatistics = searchMoveResult.getEvaluationStatistics();

        reportModelDetail.evaluationCounter = evaluationStatistics.evaluationsCounter();
        reportModelDetail.evaluationsCacheHitCounter = evaluationStatistics.cacheHitsCounter();

        Set<EvaluationEntry> evaluations = evaluationStatistics.evaluations();
        if (evaluations != null) {
            reportModelDetail.evaluations = evaluations;
            reportModelDetail.evaluationPositionCounter = evaluations.size();
            reportModelDetail.evaluationValueCounter = evaluations.parallelStream().mapToInt(EvaluationEntry::value).distinct().count();
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
