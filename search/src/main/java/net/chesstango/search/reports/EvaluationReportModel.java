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
    public long evaluatedGamesCounterTotal;
    public long distinctEvaluatedGamesCounterTotal;
    public long distinctEvaluatedGamesCounterUniqueTotal;
    public long distinctEvaluatedGamesCounterCollisionsTotal;
    public long evaluationCollisionPercentageTotal;

    ///////////////////// END TOTALS


    public List<EvolutionReportModelDetail> moveDetails;

    public static class EvolutionReportModelDetail {
        public String id;

        public String move;
        public String principalVariation;

        public int evaluation;
        public int executedMoves;

        /**
         * Evaluation Statistics
         */
        public Set<EvaluationEntry> evaluations;
        public long evaluatedGamesCounter;
        public int distinctEvaluatedGamesCounter;
        public long distinctEvaluatedGamesCounterUnique;
        public long distinctEvaluatedGamesCounterCollisions;
        public long evaluationCollisionPercentage;

    }


    public static EvaluationReportModel collectStatics(String engineName, List<SearchMoveResult> searchMoveResults) {
        EvaluationReportModel searchesReportModel = new EvaluationReportModel();

        searchesReportModel.engineName = engineName;

        searchesReportModel.load(searchMoveResults);

        return searchesReportModel;
    }

    private void load(List<SearchMoveResult> searchMoveResults) {
        this.moveDetails = new LinkedList<>();


        searchMoveResults.forEach(this::loadModelDetail);

        if (this.distinctEvaluatedGamesCounterTotal > 0) {
            this.evaluationCollisionPercentageTotal = (100 * this.distinctEvaluatedGamesCounterCollisionsTotal) / this.distinctEvaluatedGamesCounterTotal;
        }
    }

    private void loadModelDetail(SearchMoveResult searchMoveResult) {
        EvolutionReportModelDetail reportModelDetail = new EvolutionReportModelDetail();

        Move bestMove = searchMoveResult.getBestMove();
        reportModelDetail.id = searchMoveResult.getEpdID();
        reportModelDetail.move = String.format("%s%s", bestMove.getFrom().getSquare(), bestMove.getTo().getSquare());
        reportModelDetail.evaluation = searchMoveResult.getEvaluation();
        reportModelDetail.principalVariation = getPrincipalVariation(searchMoveResult.getPrincipalVariation());
        reportModelDetail.executedMoves = searchMoveResult.getExecutedMoves();


        if (searchMoveResult.getEvaluationStatistics() != null) {
            collectStaticsEvaluationStatics(reportModelDetail, searchMoveResult);
        }

        this.evaluatedGamesCounterTotal += reportModelDetail.evaluatedGamesCounter;
        this.moveDetails.add(reportModelDetail);
    }


    private void collectStaticsEvaluationStatics(EvolutionReportModelDetail reportModelDetail, SearchMoveResult searchMoveResult) {
        EvaluationStatistics evaluationStatistics = searchMoveResult.getEvaluationStatistics();

        Set<EvaluationEntry> evaluations = evaluationStatistics.evaluations();

        reportModelDetail.evaluatedGamesCounter = evaluationStatistics.evaluatedGamesCounter();
        reportModelDetail.evaluations = evaluations;


        if (reportModelDetail.evaluations != null) {
            reportModelDetail.distinctEvaluatedGamesCounter = evaluations.size();
            reportModelDetail.distinctEvaluatedGamesCounterUnique = evaluations.parallelStream().mapToInt(EvaluationEntry::value).distinct().count();
            reportModelDetail.distinctEvaluatedGamesCounterCollisions = reportModelDetail.distinctEvaluatedGamesCounter - reportModelDetail.distinctEvaluatedGamesCounterUnique;

            /*
             * Cuando TT reuse está habilitado y depth=1 se puede dar que no se evaluan algunas posiciones
             */
            if (reportModelDetail.distinctEvaluatedGamesCounter != 0) {
                reportModelDetail.evaluationCollisionPercentage = (100 * reportModelDetail.distinctEvaluatedGamesCounterCollisions) / reportModelDetail.distinctEvaluatedGamesCounter;
            }
        }

        this.distinctEvaluatedGamesCounterTotal += reportModelDetail.distinctEvaluatedGamesCounter;
        this.distinctEvaluatedGamesCounterUniqueTotal += reportModelDetail.distinctEvaluatedGamesCounterUnique;
        this.distinctEvaluatedGamesCounterCollisionsTotal += reportModelDetail.distinctEvaluatedGamesCounterCollisions;
    }

    private static String getPrincipalVariation(List<Move> principalVariation) {
        if (principalVariation == null) {
            return "-";
        } else {
            StringBuilder sb = new StringBuilder();
            for (Move move : principalVariation) {
                sb.append(String.format("%s ", String.format("%s%s", move.getFrom().getSquare(), move.getTo().getSquare())));
            }
            return sb.toString();
        }
    }
}
