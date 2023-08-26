package net.chesstango.search.reports;

import net.chesstango.board.moves.Move;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.statics.EvaluationEntry;
import net.chesstango.search.smart.statics.EvaluationStatics;
import net.chesstango.search.smart.statics.RNodeStatics;

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
    long evaluatedGamesCounterTotal;
    ///////////////////// END TOTALS


    ///////////////////// START VISITED REGULAR NODES
    int maxSearchRLevel;
    long[] expectedRNodesCounters;
    long[] visitedRNodesCounters;
    int[] cutoffPercentages;
    long visitedRNodesTotal;
    ///////////////////// END VISITED REGULAR NODES

    ///////////////////// START VISITED QUIESCENCE NODES
    int maxSearchQLevel;
    long[] visitedQNodesCounter;
    long visitedQNodesTotal;
    ///////////////////// END VISITED QUIESCENCE NODES

    List<SearchReportModelDetail> moveDetails;

    public static class SearchReportModelDetail {
        public String id;
        String move;
        Set<EvaluationEntry> evaluations;
        long evaluatedGamesCounter;
        int distinctEvaluatedGamesCounter;
        long distinctEvaluatedGamesCounterCollisions;
        long collisionPercentage;
        int[] expectedRNodesCounters;
        int[] visitedRNodesCounters;
        int[] cutoffPercentages;
        int[] visitedQNodesCounters;
        String principalVariation;

        int points;
    }


    public static SearchesReportModel collectStatics(String engineName, List<SearchMoveResult> searchMoveResults) {
        SearchesReportModel searchesReportModel = new SearchesReportModel();

        searchesReportModel.engineName = engineName;
        searchesReportModel.moveDetails = new ArrayList<>();

        searchesReportModel.expectedRNodesCounters = new long[30];
        searchesReportModel.visitedRNodesCounters = new long[30];
        searchesReportModel.cutoffPercentages = new int[30];
        searchesReportModel.visitedQNodesCounter = new long[30];

        searchMoveResults.forEach(searchMoveResult -> {
            SearchesReportModel.SearchReportModelDetail reportModelDetail = new SearchesReportModel.SearchReportModelDetail();

            Move bestMove = searchMoveResult.getBestMove();
            reportModelDetail.id = searchMoveResult.getEpdID();
            reportModelDetail.move = String.format("%s%s", bestMove.getFrom().getSquare(), bestMove.getTo().getSquare());
            reportModelDetail.points = searchMoveResult.getEvaluation();
            reportModelDetail.principalVariation = getPrincipalVariation(searchMoveResult.getPrincipalVariation());

            if (searchMoveResult.getEvaluationStatics() != null) {
                collectStaticsEvaluationStatics(reportModelDetail, searchMoveResult);
            }

            if (searchMoveResult.getRegularNodeStatics() != null) {
                collectStaticsRegularNodeStatic(searchesReportModel, reportModelDetail, searchMoveResult);
            }

            reportModelDetail.visitedQNodesCounters = searchMoveResult.getVisitedNodesQuiescenceCounter();
            for (int i = 0; i < 30; i++) {
                searchesReportModel.visitedQNodesCounter[i] += reportModelDetail.visitedQNodesCounters == null ? 0 : reportModelDetail.visitedQNodesCounters[i];
            }

            searchesReportModel.evaluatedGamesCounterTotal += reportModelDetail.evaluatedGamesCounter;
            searchesReportModel.moveDetails.add(reportModelDetail);
        });


        /**
         * Totales sumarizados
         */
        for (int i = 0; i < 30; i++) {
            if (searchesReportModel.expectedRNodesCounters[i] > 0) {
                searchesReportModel.cutoffPercentages[i] = (int) (100 - (100 * searchesReportModel.visitedRNodesCounters[i] / searchesReportModel.expectedRNodesCounters[i]));
            }

            if (searchesReportModel.expectedRNodesCounters[i] > 0 && searchesReportModel.visitedRNodesCounters[i] > 0) {
                searchesReportModel.maxSearchRLevel = i + 1; //En el nivel más bajo no exploramos ningun nodo
            }

            if (searchesReportModel.visitedQNodesCounter[i] > 0) {
                searchesReportModel.maxSearchQLevel = i + 1;
            }

            searchesReportModel.visitedRNodesTotal += searchesReportModel.visitedRNodesCounters[i];
            searchesReportModel.visitedQNodesTotal += searchesReportModel.visitedQNodesCounter[i];
        }

        searchesReportModel.visitedNodesTotal = searchesReportModel.visitedRNodesTotal + searchesReportModel.visitedQNodesTotal;

        return searchesReportModel;
    }

    private static void collectStaticsRegularNodeStatic(SearchesReportModel searchesReportModel, SearchesReportModel.SearchReportModelDetail reportModelDetail, SearchMoveResult searchMoveResult) {
        RNodeStatics regularNodeStatics = searchMoveResult.getRegularNodeStatics();
        reportModelDetail.expectedRNodesCounters = regularNodeStatics.expectedNodesCounters();
        reportModelDetail.visitedRNodesCounters = regularNodeStatics.visitedNodesCounters();
        reportModelDetail.cutoffPercentages = new int[30];

        for (int i = 0; i < 30; i++) {

            if (reportModelDetail.expectedRNodesCounters[i] <= 0 && reportModelDetail.visitedRNodesCounters[i] > 0) {
                throw new RuntimeException("expectedNodesCounters[i] <= 0");
            } else if (reportModelDetail.expectedRNodesCounters[i] < reportModelDetail.visitedRNodesCounters[i]) {
                throw new RuntimeException("reportModelDetail.expectedRNodesCounters[i] < reportModelDetail.visitedRNodesCounters[i]");
            }
            if (reportModelDetail.expectedRNodesCounters[i] > 0 && reportModelDetail.visitedRNodesCounters[i] > 0) {
                reportModelDetail.cutoffPercentages[i] = (int) (100 - (100 * reportModelDetail.visitedRNodesCounters[i] / reportModelDetail.expectedRNodesCounters[i]));
            }

            searchesReportModel.expectedRNodesCounters[i] += reportModelDetail.expectedRNodesCounters[i];
            searchesReportModel.visitedRNodesCounters[i] += reportModelDetail.visitedRNodesCounters[i];
        }
    }

    private static void collectStaticsEvaluationStatics(SearchReportModelDetail reportModelDetail, SearchMoveResult searchMoveResult) {
        EvaluationStatics evaluationStatics = searchMoveResult.getEvaluationStatics();

        reportModelDetail.evaluatedGamesCounter = evaluationStatics.evaluatedGamesCounter();
        reportModelDetail.evaluations = evaluationStatics.evaluations();


        if(reportModelDetail.evaluations !=null) {
            reportModelDetail.distinctEvaluatedGamesCounter = evaluationStatics.evaluations().size();
            reportModelDetail.distinctEvaluatedGamesCounterCollisions = evaluationStatics.evaluatedGamesCounter() - evaluationStatics.evaluations().parallelStream().mapToInt(EvaluationEntry::getValue).distinct().count();

            /*
             * Cuando TT reuse está habilitado y depth=1 se puede dar que no se evaluan algunas posiciones
             */
            if (reportModelDetail.distinctEvaluatedGamesCounter != 0) {
                reportModelDetail.collisionPercentage = (100 * reportModelDetail.distinctEvaluatedGamesCounterCollisions) / reportModelDetail.distinctEvaluatedGamesCounter;
            }
        }
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
