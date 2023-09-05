package net.chesstango.search.reports;

import net.chesstango.board.moves.Move;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.statistics.EvaluationEntry;
import net.chesstango.search.smart.statistics.EvaluationStatistics;
import net.chesstango.search.smart.statistics.NodeStatistics;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author Mauricio Coria
 */
public class SearchesReportModel {
    public String engineName;

    // Regular vs Quiescence
    ///////////////////// START TOTALS
    public long visitedNodesTotal;
    public long expectedNodesTotal;
    public int cutoffPercentageTotal;

    public long evaluatedGamesCounterTotal;
    public long distinctEvaluatedGamesCounterTotal;
    public long distinctEvaluatedGamesCounterUniqueTotal;
    public long distinctEvaluatedGamesCounterCollisionsTotal;
    public long evaluationCollisionPercentageTotal;
    public long executedMovesTotal;
    ///////////////////// END TOTALS


    ///////////////////// START VISITED REGULAR NODES
    public int maxSearchRLevel;
    public long[] expectedRNodesCounters;
    public long[] visitedRNodesCounters;
    public int[] cutoffRPercentages;
    public long expectedRNodesTotal;
    public long visitedRNodesTotal;
    ///////////////////// END VISITED REGULAR NODES

    ///////////////////// START VISITED QUIESCENCE NODES
    public int maxSearchQLevel;
    public long[] expectedQNodesCounters;
    public long[] visitedQNodesCounters;
    public int[] cutoffQPercentages;
    public long visitedQNodesTotal;
    public long expectedQNodesTotal;
    ///////////////////// END VISITED QUIESCENCE NODES

    public List<SearchReportModelDetail> moveDetails;

    public static class SearchReportModelDetail {
        public String id;
        public String move;

        /**
         * Evaluation Statistics
         */
        public Set<EvaluationEntry> evaluations;
        public long evaluatedGamesCounter;
        public int distinctEvaluatedGamesCounter;
        public long distinctEvaluatedGamesCounterUnique;
        public long distinctEvaluatedGamesCounterCollisions;
        public long evaluationCollisionPercentage;


        /**
         * Node Statistics
         */

        public long visitedNodesTotal;
        public long expectedNodesTotal;


        public int[] expectedRNodesCounters;
        public int expectedRNodesCounter;
        public int[] visitedRNodesCounters;
        public int visitedRNodesCounter;
        public int[] cutoffRPercentages;


        public int[] expectedQNodesCounters;
        public int expectedQNodesCounter;
        public int[] visitedQNodesCounters;
        public int visitedQNodesCounter;
        public int[] cutoffQPercentages;


        public String principalVariation;
        public int evaluation;
        public int executedMoves;
    }


    public static SearchesReportModel collectStatics(String engineName, List<SearchMoveResult> searchMoveResults) {
        SearchesReportModel searchesReportModel = new SearchesReportModel();

        searchesReportModel.engineName = engineName;

        searchesReportModel.load(searchMoveResults);

        return searchesReportModel;
    }

    private void load(List<SearchMoveResult> searchMoveResults) {
        this.moveDetails = new LinkedList<>();
        this.expectedRNodesCounters = new long[30];
        this.visitedRNodesCounters = new long[30];
        this.cutoffRPercentages = new int[30];
        this.visitedQNodesCounters = new long[30];
        this.expectedQNodesCounters = new long[30];
        this.cutoffQPercentages = new int[30];


        searchMoveResults.forEach(this::loadModelDetail);

        /**
         * Totales sumarizados
         */
        for (int i = 0; i < 30; i++) {
            if (this.expectedRNodesCounters[i] > 0) {
                this.cutoffRPercentages[i] = (int) (100 - (100 * this.visitedRNodesCounters[i] / this.expectedRNodesCounters[i]));
            }

            if (this.expectedQNodesCounters[i] > 0) {
                this.cutoffQPercentages[i] = (int) (100 - (100 * this.visitedQNodesCounters[i] / this.expectedQNodesCounters[i]));
            }

            if (this.expectedRNodesCounters[i] > 0
                    && this.visitedRNodesCounters[i] > 0
                    && this.maxSearchRLevel < i + 1) {
                this.maxSearchRLevel = i + 1; //En el nivel más bajo no exploramos ningun nodo
            }

            if (this.visitedQNodesCounters[i] > 0
                    && this.maxSearchQLevel < i + 1) {
                this.maxSearchQLevel = i + 1;
            }

            this.expectedRNodesTotal += this.expectedRNodesCounters[i];
            this.visitedRNodesTotal += this.visitedRNodesCounters[i];

            this.expectedQNodesTotal += this.expectedQNodesCounters[i];
            this.visitedQNodesTotal += this.visitedQNodesCounters[i];
        }

        if (this.distinctEvaluatedGamesCounterTotal > 0) {
            this.evaluationCollisionPercentageTotal = (100 * this.distinctEvaluatedGamesCounterCollisionsTotal) / this.distinctEvaluatedGamesCounterTotal;
        }

        this.visitedNodesTotal = this.visitedRNodesTotal + this.visitedQNodesTotal;
        this.expectedNodesTotal = this.expectedRNodesTotal + this.expectedQNodesTotal;
        this.cutoffPercentageTotal = (int) ((100 * this.visitedNodesTotal) / this.expectedNodesTotal);
    }

    private void loadModelDetail(SearchMoveResult searchMoveResult) {
        SearchesReportModel.SearchReportModelDetail reportModelDetail = new SearchesReportModel.SearchReportModelDetail();

        Move bestMove = searchMoveResult.getBestMove();
        reportModelDetail.id = searchMoveResult.getEpdID();
        reportModelDetail.move = String.format("%s%s", bestMove.getFrom().getSquare(), bestMove.getTo().getSquare());
        reportModelDetail.evaluation = searchMoveResult.getEvaluation();
        reportModelDetail.principalVariation = getPrincipalVariation(searchMoveResult.getPrincipalVariation());
        reportModelDetail.executedMoves = searchMoveResult.getExecutedMoves();

        if (searchMoveResult.getRegularNodeStatistics() != null) {
            collectStaticsRegularNodeStatistics(reportModelDetail, searchMoveResult);
        }

        if (searchMoveResult.getQuiescenceNodeStatistics() != null) {
            collectStaticsQuiescenceNodeStatistics(reportModelDetail, searchMoveResult);
        }

        if (searchMoveResult.getEvaluationStatistics() != null) {
            collectStaticsEvaluationStatics(reportModelDetail, searchMoveResult);
        }

        this.evaluatedGamesCounterTotal += reportModelDetail.evaluatedGamesCounter;
        this.executedMovesTotal += reportModelDetail.executedMoves;
        this.moveDetails.add(reportModelDetail);
    }

    private void collectStaticsRegularNodeStatistics(SearchesReportModel.SearchReportModelDetail reportModelDetail, SearchMoveResult searchMoveResult) {
        NodeStatistics regularNodeStatistics = searchMoveResult.getRegularNodeStatistics();
        reportModelDetail.expectedRNodesCounters = regularNodeStatistics.expectedNodesCounters();
        reportModelDetail.visitedRNodesCounters = regularNodeStatistics.visitedNodesCounters();
        reportModelDetail.cutoffRPercentages = new int[30];

        for (int i = 0; i < 30; i++) {
            if (reportModelDetail.expectedRNodesCounters[i] <= 0 && reportModelDetail.visitedRNodesCounters[i] > 0) {
                throw new RuntimeException("expectedNodesCounters[i] <= 0");
            } else if (reportModelDetail.expectedRNodesCounters[i] < reportModelDetail.visitedRNodesCounters[i]) {
                throw new RuntimeException("reportModelDetail.expectedRNodesCounters[i] < reportModelDetail.visitedRNodesCounters[i]");
            }

            if (reportModelDetail.visitedRNodesCounters[i] > 0) {
                reportModelDetail.visitedRNodesCounter += reportModelDetail.visitedRNodesCounters[i];
                reportModelDetail.expectedRNodesCounter += reportModelDetail.expectedRNodesCounters[i];

                this.visitedRNodesCounters[i] += reportModelDetail.visitedRNodesCounters[i];
                this.expectedRNodesCounters[i] += reportModelDetail.expectedRNodesCounters[i];

                if (reportModelDetail.expectedRNodesCounters[i] > 0) {
                    reportModelDetail.cutoffRPercentages[i] = (100 - (100 * reportModelDetail.visitedRNodesCounters[i] / reportModelDetail.expectedRNodesCounters[i]));
                }
            }
        }
        reportModelDetail.visitedNodesTotal = reportModelDetail.visitedRNodesCounter;
        reportModelDetail.expectedNodesTotal = reportModelDetail.expectedRNodesCounter;
    }

    private void collectStaticsQuiescenceNodeStatistics(SearchReportModelDetail reportModelDetail, SearchMoveResult searchMoveResult) {
        NodeStatistics quiescenceNodeStatistics = searchMoveResult.getQuiescenceNodeStatistics();
        reportModelDetail.expectedQNodesCounters = quiescenceNodeStatistics.expectedNodesCounters();
        reportModelDetail.visitedQNodesCounters = quiescenceNodeStatistics.visitedNodesCounters();
        reportModelDetail.cutoffQPercentages = new int[30];

        for (int i = 0; i < 30; i++) {

            if (reportModelDetail.visitedQNodesCounters[i] > 0) {
                reportModelDetail.visitedQNodesCounter += reportModelDetail.visitedQNodesCounters[i];
                this.visitedQNodesCounters[i] += reportModelDetail.visitedQNodesCounters[i];

                reportModelDetail.expectedQNodesCounter += reportModelDetail.expectedQNodesCounters[i];
                this.expectedQNodesCounters[i] += reportModelDetail.expectedQNodesCounters[i];

                if (reportModelDetail.expectedQNodesCounters[i] > 0) {
                    reportModelDetail.cutoffQPercentages[i] = (100 - (100 * reportModelDetail.visitedQNodesCounters[i] / reportModelDetail.expectedQNodesCounters[i]));
                }
            }
        }
        reportModelDetail.visitedNodesTotal += reportModelDetail.visitedQNodesCounter;
        reportModelDetail.expectedNodesTotal += reportModelDetail.expectedQNodesCounter;
    }

    private void collectStaticsEvaluationStatics(SearchReportModelDetail reportModelDetail, SearchMoveResult searchMoveResult) {
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
