package net.chesstango.search.reports;

import net.chesstango.board.moves.Move;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.statistics.EvaluationEntry;
import net.chesstango.search.smart.statistics.EvaluationStatistics;
import net.chesstango.search.smart.statistics.NodeStatistics;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Mauricio Coria
 */
public class SearchesReportModel {
    String engineName;

    // Regular vs Quiescence
    ///////////////////// START TOTALS
    long visitedNodesTotal;
    long expectedNodesTotal;
    int cutoffPercentage;

    long evaluatedGamesCounterTotal;
    long distinctEvaluatedGamesCounterTotal;
    long distinctEvaluatedGamesCounterUniqueTotal;
    long distinctEvaluatedGamesCounterCollisionsTotal;
    long evaluationCollisionPercentageTotal;
    long executedMovesTotal;
    ///////////////////// END TOTALS


    ///////////////////// START VISITED REGULAR NODES
    int maxSearchRLevel;
    long[] expectedRNodesCounters;
    long[] visitedRNodesCounters;
    int[] cutoffRPercentages;
    long expectedRNodesTotal;
    long visitedRNodesTotal;
    ///////////////////// END VISITED REGULAR NODES

    ///////////////////// START VISITED QUIESCENCE NODES
    int maxSearchQLevel;
    long[] expectedQNodesCounters;
    long[] visitedQNodesCounters;
    int[] cutoffQPercentages;
    long visitedQNodesTotal;
    long expectedQNodesTotal;
    ///////////////////// END VISITED QUIESCENCE NODES

    List<SearchReportModelDetail> moveDetails;

    public static class SearchReportModelDetail {
        String id;
        String move;

        /**
         * Evaluation Statistics
         */
        Set<EvaluationEntry> evaluations;
        long evaluatedGamesCounter;
        int distinctEvaluatedGamesCounter;
        long distinctEvaluatedGamesCounterUnique;
        long distinctEvaluatedGamesCounterCollisions;
        long evaluationCollisionPercentage;


        /**
         * Node Statistics
         */

        long visitedNodesTotal;
        long expectedNodesTotal;


        int[] expectedRNodesCounters;
        int expectedRNodesCounter;
        int[] visitedRNodesCounters;
        int visitedRNodesCounter;
        int[] cutoffRPercentages;


        int[] expectedQNodesCounters;
        int expectedQNodesCounter;
        int[] visitedQNodesCounters;
        int visitedQNodesCounter;
        int[] cutoffQPercentages;


        String principalVariation;
        int evaluation;
        int executedMoves;
    }


    public static SearchesReportModel collectStatics(String engineName, List<SearchMoveResult> searchMoveResults) {
        SearchesReportModel searchesReportModel = new SearchesReportModel();

        searchesReportModel.engineName = engineName;
        searchesReportModel.moveDetails = new ArrayList<>();

        searchesReportModel.expectedRNodesCounters = new long[30];
        searchesReportModel.visitedRNodesCounters = new long[30];
        searchesReportModel.cutoffRPercentages = new int[30];
        searchesReportModel.visitedQNodesCounters = new long[30];
        searchesReportModel.expectedQNodesCounters = new long[30];
        searchesReportModel.cutoffQPercentages = new int[30];

        searchMoveResults.forEach(searchMoveResult -> {
            SearchesReportModel.SearchReportModelDetail reportModelDetail = new SearchesReportModel.SearchReportModelDetail();

            Move bestMove = searchMoveResult.getBestMove();
            reportModelDetail.id = searchMoveResult.getEpdID();
            reportModelDetail.move = String.format("%s%s", bestMove.getFrom().getSquare(), bestMove.getTo().getSquare());
            reportModelDetail.evaluation = searchMoveResult.getEvaluation();
            reportModelDetail.principalVariation = getPrincipalVariation(searchMoveResult.getPrincipalVariation());
            reportModelDetail.executedMoves = searchMoveResult.getExecutedMoves();

            if (searchMoveResult.getRegularNodeStatistics() != null) {
                collectStaticsRegularNodeStatistics(searchesReportModel, reportModelDetail, searchMoveResult);
            }

            if (searchMoveResult.getQuiescenceNodeStatistics() != null) {
                collectStaticsQuiescenceNodeStatistics(searchesReportModel, reportModelDetail, searchMoveResult);
            }

            if (searchMoveResult.getEvaluationStatistics() != null) {
                collectStaticsEvaluationStatics(searchesReportModel, reportModelDetail, searchMoveResult);
            }

            searchesReportModel.evaluatedGamesCounterTotal += reportModelDetail.evaluatedGamesCounter;
            searchesReportModel.executedMovesTotal += reportModelDetail.executedMoves;
            searchesReportModel.moveDetails.add(reportModelDetail);
        });


        /**
         * Totales sumarizados
         */
        for (int i = 0; i < 30; i++) {
            if (searchesReportModel.expectedRNodesCounters[i] > 0) {
                searchesReportModel.cutoffRPercentages[i] = (int) (100 - (100 * searchesReportModel.visitedRNodesCounters[i] / searchesReportModel.expectedRNodesCounters[i]));
            }

            if (searchesReportModel.expectedQNodesCounters[i] > 0) {
                searchesReportModel.cutoffQPercentages[i] = (int) (100 - (100 * searchesReportModel.visitedQNodesCounters[i] / searchesReportModel.expectedQNodesCounters[i]));
            }

            if (searchesReportModel.expectedRNodesCounters[i] > 0
                    && searchesReportModel.visitedRNodesCounters[i] > 0
                    && searchesReportModel.maxSearchRLevel < i + 1) {
                searchesReportModel.maxSearchRLevel = i + 1; //En el nivel más bajo no exploramos ningun nodo
            }

            if (searchesReportModel.visitedQNodesCounters[i] > 0
                    && searchesReportModel.maxSearchQLevel < i + 1) {
                searchesReportModel.maxSearchQLevel = i + 1;
            }

            searchesReportModel.expectedRNodesTotal += searchesReportModel.expectedRNodesCounters[i];
            searchesReportModel.visitedRNodesTotal += searchesReportModel.visitedRNodesCounters[i];

            searchesReportModel.expectedQNodesTotal += searchesReportModel.expectedQNodesCounters[i];
            searchesReportModel.visitedQNodesTotal += searchesReportModel.visitedQNodesCounters[i];
        }

        if (searchesReportModel.distinctEvaluatedGamesCounterTotal > 0) {
            searchesReportModel.evaluationCollisionPercentageTotal = (100 * searchesReportModel.distinctEvaluatedGamesCounterCollisionsTotal) / searchesReportModel.distinctEvaluatedGamesCounterTotal;
        }

        searchesReportModel.visitedNodesTotal = searchesReportModel.visitedRNodesTotal + searchesReportModel.visitedQNodesTotal;
        searchesReportModel.expectedNodesTotal = searchesReportModel.expectedRNodesTotal + searchesReportModel.expectedQNodesTotal;
        searchesReportModel.cutoffPercentage = (int) ((100 * searchesReportModel.visitedNodesTotal) / searchesReportModel.expectedNodesTotal);

        return searchesReportModel;
    }


    private static void collectStaticsRegularNodeStatistics(SearchesReportModel searchesReportModel, SearchesReportModel.SearchReportModelDetail reportModelDetail, SearchMoveResult searchMoveResult) {
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

                searchesReportModel.visitedRNodesCounters[i] += reportModelDetail.visitedRNodesCounters[i];
                searchesReportModel.expectedRNodesCounters[i] += reportModelDetail.expectedRNodesCounters[i];

                if (reportModelDetail.expectedRNodesCounters[i] > 0) {
                    reportModelDetail.cutoffRPercentages[i] = (100 - (100 * reportModelDetail.visitedRNodesCounters[i] / reportModelDetail.expectedRNodesCounters[i]));
                }
            }
        }
        reportModelDetail.visitedNodesTotal = reportModelDetail.visitedRNodesCounter;
        reportModelDetail.expectedNodesTotal = reportModelDetail.expectedRNodesCounter;
    }

    private static void collectStaticsQuiescenceNodeStatistics(SearchesReportModel searchesReportModel, SearchReportModelDetail reportModelDetail, SearchMoveResult searchMoveResult) {
        NodeStatistics quiescenceNodeStatistics = searchMoveResult.getQuiescenceNodeStatistics();
        reportModelDetail.expectedQNodesCounters = quiescenceNodeStatistics.expectedNodesCounters();
        reportModelDetail.visitedQNodesCounters = quiescenceNodeStatistics.visitedNodesCounters();
        reportModelDetail.cutoffQPercentages = new int[30];

        for (int i = 0; i < 30; i++) {

            if (reportModelDetail.visitedQNodesCounters[i] > 0) {
                reportModelDetail.visitedQNodesCounter += reportModelDetail.visitedQNodesCounters[i];
                searchesReportModel.visitedQNodesCounters[i] += reportModelDetail.visitedQNodesCounters[i];

                reportModelDetail.expectedQNodesCounter += reportModelDetail.expectedQNodesCounters[i];
                searchesReportModel.expectedQNodesCounters[i] += reportModelDetail.expectedQNodesCounters[i];

                if (reportModelDetail.expectedQNodesCounters[i] > 0) {
                    reportModelDetail.cutoffQPercentages[i] = (100 - (100 * reportModelDetail.visitedQNodesCounters[i] / reportModelDetail.expectedQNodesCounters[i]));
                }
            }
        }
        reportModelDetail.visitedNodesTotal += reportModelDetail.visitedQNodesCounter;
        reportModelDetail.expectedNodesTotal += reportModelDetail.expectedQNodesCounter;
    }

    private static void collectStaticsEvaluationStatics(SearchesReportModel searchesReportModel, SearchReportModelDetail reportModelDetail, SearchMoveResult searchMoveResult) {
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

        searchesReportModel.distinctEvaluatedGamesCounterTotal += reportModelDetail.distinctEvaluatedGamesCounter;
        searchesReportModel.distinctEvaluatedGamesCounterUniqueTotal += reportModelDetail.distinctEvaluatedGamesCounterUnique;
        searchesReportModel.distinctEvaluatedGamesCounterCollisionsTotal += reportModelDetail.distinctEvaluatedGamesCounterCollisions;
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
