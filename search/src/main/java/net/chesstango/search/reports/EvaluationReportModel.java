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
    public long evaluatedPositionsCounterTotal;
    public long evaluatedPositionsCacheHitCounterTotal;
    public long evaluatedUniquePositionsCounterTotal;
    public long evaluatedUniquePositionsValuesCounterTotal;
    public long evaluatedUniquePositionsValuesCollisionsCounterTotal;
    public long evaluationCollisionPercentageTotal;

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
        public long evaluatedPositionsCounter;
        public long evaluatedPositionsCacheHitCounter;
        public long evaluatedUniquePositionsCounter;
        public long evaluatedUniquePositionsValuesCounter;
        public long evaluatedUniquePositionsValuesCollisionsCounter;
        public long evaluationCollisionPercentage;

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

        if (this.evaluatedUniquePositionsCounterTotal > 0) {
            this.evaluationCollisionPercentageTotal = (100 * this.evaluatedUniquePositionsValuesCollisionsCounterTotal) / this.evaluatedUniquePositionsCounterTotal;
        }
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

        this.evaluatedPositionsCounterTotal += reportModelDetail.evaluatedPositionsCounter;
        this.evaluatedPositionsCacheHitCounterTotal += reportModelDetail.evaluatedPositionsCacheHitCounter;
        this.evaluatedUniquePositionsCounterTotal += reportModelDetail.evaluatedUniquePositionsCounter;
        this.evaluatedUniquePositionsValuesCounterTotal += reportModelDetail.evaluatedUniquePositionsValuesCounter;
        this.evaluatedUniquePositionsValuesCollisionsCounterTotal += reportModelDetail.evaluatedUniquePositionsValuesCollisionsCounter;
        this.moveDetails.add(reportModelDetail);
    }


    private void collectEvaluationStatistics(EvolutionReportModelDetail reportModelDetail, SearchMoveResult searchMoveResult) {
        EvaluationStatistics evaluationStatistics = searchMoveResult.getEvaluationStatistics();

        reportModelDetail.evaluatedPositionsCounter = evaluationStatistics.evaluationsCounter();
        reportModelDetail.evaluatedPositionsCacheHitCounter = evaluationStatistics.cacheHitsCounter();

        Set<EvaluationEntry> evaluations = evaluationStatistics.evaluations();
        if (evaluations != null) {
            reportModelDetail.evaluations = evaluations;
            reportModelDetail.evaluatedUniquePositionsCounter = evaluations.size();
            reportModelDetail.evaluatedUniquePositionsValuesCounter = evaluations.parallelStream().mapToInt(EvaluationEntry::value).distinct().count();
            reportModelDetail.evaluatedUniquePositionsValuesCollisionsCounter = reportModelDetail.evaluatedUniquePositionsCounter - reportModelDetail.evaluatedUniquePositionsValuesCounter;

            /*
             * Cuando TT reuse está habilitado y depth=1 se puede dar que no se evaluan algunas posiciones
             */
            if (reportModelDetail.evaluatedUniquePositionsCounter != 0) {
                reportModelDetail.evaluationCollisionPercentage = (100 * reportModelDetail.evaluatedUniquePositionsValuesCollisionsCounter) / reportModelDetail.evaluatedUniquePositionsCounter;
            }
        }
    }

}
