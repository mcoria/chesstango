package net.chesstango.search.reports;

import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.pgn.PGNEncoder;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMoveResult;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 *
 * @author Mauricio Coria
 */
public class SearchesReport {

    private boolean printCutoffStatics;
    private boolean printNodesVisitedStatics;

    private boolean printPrincipalVariation;

    public void printSearchesStatics(List<SearchMoveResult> searchMoveResults) {
        ReportModel reportModel = collectStatics(searchMoveResults);

        if(printNodesVisitedStatics) {
            printVisitedNodes(reportModel);
        }
        if(printCutoffStatics) {
            printCutoff(reportModel);
        }

        if(printPrincipalVariation){
            printPrincipalVariation(reportModel);
        }
    }


    private ReportModel collectStatics(List<SearchMoveResult> searchMoveResults) {
        ReportModel reportModel = new ReportModel();

        reportModel.moveDetails = new ArrayList<>();

        reportModel.expectedNodesCounters = new long[30];
        reportModel.visitedNodesCounters = new long[30];
        reportModel.cutoffPercentages = new int[30];
        reportModel.visitedNodesQuiescenceCounter = new long[30];

        searchMoveResults.forEach(searchMoveResult -> {
            ReportRowMoveDetail reportModelDetail = new ReportRowMoveDetail();

            Move bestMove = searchMoveResult.getBestMove();
            
            reportModelDetail.move = String.format("%s%s", bestMove.getFrom().getSquare(), bestMove.getTo().getSquare());

            int[] expectedNodesCounters = searchMoveResult.getExpectedNodesCounters();
            int[] visitedNodesCounters = searchMoveResult.getVisitedNodesCounters();
            int[] visitedNodesQuiescenceCounter = searchMoveResult.getVisitedNodesQuiescenceCounter();
            int[] cutoffPercentages = new int[30];

            for (int i = 0; i < 30; i++) {

                if (expectedNodesCounters[i] <= 0 && visitedNodesCounters[i] > 0) {
                    throw new RuntimeException("expectedNodesCounters[i] <= 0");
                }

                if (expectedNodesCounters[i] > 0) {
                    cutoffPercentages[i] = (int) (100 - (100 * visitedNodesCounters[i] / expectedNodesCounters[i]));
                }

                reportModel.expectedNodesCounters[i] += expectedNodesCounters[i];
                reportModel.visitedNodesCounters[i] += visitedNodesCounters[i];
                reportModel.visitedNodesQuiescenceCounter[i] += visitedNodesQuiescenceCounter[i];
            }

            reportModelDetail.expectedNodesCounters = expectedNodesCounters;
            reportModelDetail.visitedNodesCounters = visitedNodesCounters;
            reportModelDetail.visitedNodesQuiescenceCounter = visitedNodesQuiescenceCounter;
            reportModelDetail.cutoffPercentages = cutoffPercentages;


            StringBuilder sb = new StringBuilder();
            for (String moveStr: searchMoveResult.getPrincipalVariation()) {
                sb.append( String.format("%s ", moveStr) );
            }

            reportModelDetail.principalVariation = sb.toString();

            reportModelDetail.points = searchMoveResult.getEvaluation();

            reportModel.moveDetails.add(reportModelDetail);
        });

        for (int i = 0; i < 30; i++) {
            long[] expectedNodesCounters = reportModel.expectedNodesCounters;
            long[] visitedNodesCounters = reportModel.visitedNodesCounters;
            int[] cutoffPercentages = reportModel.cutoffPercentages;
            long[] visitedNodesQuiescenceCounter = reportModel.visitedNodesQuiescenceCounter;

            if (expectedNodesCounters[i] <= 0 && visitedNodesCounters[i] > 0) {
                throw new RuntimeException("expectedNodesCounters[i] <= 0");
            }

            if (expectedNodesCounters[i] > 0) {
                cutoffPercentages[i] = (int) (100 - (100 * visitedNodesCounters[i] / expectedNodesCounters[i]));
                reportModel.maxSearchLevel = i; //En el nivel más bajo no exploramos ningun nodo
            }

            if (visitedNodesQuiescenceCounter[i] > 0) {
                reportModel.maxSearchLevelQuiescence = i + 1;
            }

            reportModel.visitedNodesTotal += visitedNodesCounters[i];
            reportModel.expectedNodesTotal += expectedNodesCounters[i];
            reportModel.visitedNodesQuiescenceTotal += visitedNodesQuiescenceCounter[i];
        }

        reportModel.visitedNodesSummaryTotal = reportModel.visitedNodesTotal + reportModel.visitedNodesQuiescenceTotal;

        return reportModel;
    }


    private void printVisitedNodes(ReportModel report) {
        System.out.printf("Visited Nodes\n");

        // Marco superior de la tabla
        System.out.printf(" ________");
        IntStream.range(0, report.maxSearchLevel).forEach(depth -> System.out.printf("_____________________"));
        IntStream.range(0, report.maxSearchLevelQuiescence).forEach(depth -> System.out.printf("____________"));
        System.out.printf("\n");

        // Nombre de las columnas
        System.out.printf("| Move   ");
        IntStream.range(0, report.maxSearchLevel).forEach(depth -> System.out.printf("|     Level %2d       ", depth + 1));
        IntStream.range(0, report.maxSearchLevelQuiescence).forEach(depth -> System.out.printf("| QLevel %2d ", depth + 1));
        System.out.printf("|\n");

        // Cuerpo
        for (ReportRowMoveDetail moveDetail : report.moveDetails) {
            System.out.printf("| %6s ", moveDetail.move);
            IntStream.range(0, report.maxSearchLevel).forEach(depth -> System.out.printf("| %7d / %8d ", moveDetail.visitedNodesCounters[depth], moveDetail.expectedNodesCounters[depth]));
            IntStream.range(0, report.maxSearchLevelQuiescence).forEach(depth -> System.out.printf("|   %7d ", moveDetail.visitedNodesQuiescenceCounter[depth]));
            System.out.printf("|\n");
        }

        // Totales
        System.out.printf("|--------");
        IntStream.range(0, report.maxSearchLevel).forEach(depth -> System.out.printf("---------------------"));
        IntStream.range(0, report.maxSearchLevelQuiescence).forEach(depth -> System.out.printf("------------"));
        System.out.printf("|\n");
        System.out.printf("| SUM    ");
        IntStream.range(0, report.maxSearchLevel).forEach(depth -> System.out.printf("| %7d / %8d ", report.visitedNodesCounters[depth], report.expectedNodesCounters[depth]));
        IntStream.range(0, report.maxSearchLevelQuiescence).forEach(depth -> System.out.printf("|   %7d ", report.visitedNodesQuiescenceCounter[depth]));
        System.out.printf("|\n");


        // Marco inferior de la tabla
        System.out.printf(" --------");
        IntStream.range(0, report.maxSearchLevel).forEach(depth -> System.out.printf("---------------------"));
        IntStream.range(0, report.maxSearchLevelQuiescence).forEach(depth -> System.out.printf("------------"));
        System.out.printf("\n");

        System.out.printf("Visited  Regular Nodes: %8d\n", report.visitedNodesTotal);
        System.out.printf("Visited         QNodes: %8d\n", report.visitedNodesQuiescenceTotal);
        System.out.printf("Visited  Total   Nodes: %8d\n", report.visitedNodesSummaryTotal);
        System.out.printf("\n\n");
    }

    private void printCutoff(ReportModel report) {
        System.out.printf("Cutoff per search level (higher is better)\n");

        // Marco superior de la tabla
        System.out.printf(" ________");
        IntStream.range(0, report.maxSearchLevel).forEach(depth -> System.out.printf("___________"));
        System.out.printf("\n");

        // Nombre de las columnas
        System.out.printf("| Move   ");
        IntStream.range(0, report.maxSearchLevel).forEach(depth -> System.out.printf("| Level %2d ", depth + 1));
        System.out.printf("|\n");

        // Cuerpo
        for (ReportRowMoveDetail moveDetail : report.moveDetails) {
            System.out.printf("| %6s ", moveDetail.move);
            IntStream.range(0, report.maxSearchLevel).forEach(depth -> System.out.printf("| %6d %% ", moveDetail.cutoffPercentages[depth]));
            System.out.printf("|\n");
        }

        // Marco inferior de la tabla
        System.out.printf(" --------");
        IntStream.range(0, report.maxSearchLevel).forEach(depth -> System.out.printf("-----------"));
        System.out.printf("\n\n");
    }

    private void printPrincipalVariation(ReportModel report) {
        System.out.printf("Principal Variations\n");


        // Nombre de las columnas
        System.out.printf("Move\n");

        // Cuerpo
        for (ReportRowMoveDetail moveDetail : report.moveDetails) {
            System.out.printf("%6s:", moveDetail.move);
            System.out.printf(" %s; Points = %d ", moveDetail.principalVariation, moveDetail.points);
            if(moveDetail.points == GameEvaluator.WHITE_WON || moveDetail.points == GameEvaluator.BLACK_WON){
                System.out.printf(" MATE");
            }
            System.out.printf("\n");
        }
    }

    public SearchesReport withCutoffStatics() {
        this.printCutoffStatics = true;
        return this;
    }

    public SearchesReport withNodesVisitedStatics() {
        this.printNodesVisitedStatics = true;
        return this;
    }

    public SearchesReport withPrincipalVariation() {
        this.printPrincipalVariation = true;
        return this;
    }

    private static class ReportModel {
        int maxSearchLevel;
        long[] expectedNodesCounters;
        long [] visitedNodesCounters;
        int [] cutoffPercentages;

        int maxSearchLevelQuiescence;
        long[] visitedNodesQuiescenceCounter;

        long expectedNodesTotal;
        long visitedNodesTotal;
        long visitedNodesQuiescenceTotal;

        long visitedNodesSummaryTotal;

        List<ReportRowMoveDetail> moveDetails;
    }

    private static class ReportRowMoveDetail {
        String move;
        int[] expectedNodesCounters;
        int[] visitedNodesCounters;
        int[] cutoffPercentages;
        int[] visitedNodesQuiescenceCounter;
        String principalVariation;

        int points;
    }
}
