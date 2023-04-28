package net.chesstango.search.reports;

import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.pgn.PGNEncoder;
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

    public void printSearchesStatics(List<SearchMoveResult> searchMoveResults) {
        ReportModel reportModel = collectStatics(searchMoveResults);

        if(printNodesVisitedStatics) {
            printVisitedNodes(reportModel);
        }
        if(printCutoffStatics) {
            printCutoff(reportModel);
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
            Move bestMove = searchMoveResult.getBestMove();

            ReportRowMoveDetail reportModelDetail = new ReportRowMoveDetail();
            
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
                reportModel.maxSearchLevel = i;
            }

            if (visitedNodesQuiescenceCounter[i] > 0) {
                reportModel.maxSearchLevelQuiescence = i;
            }
        }


        return reportModel;
    }


    private void printVisitedNodes(ReportModel report) {
        System.out.printf("Visited Nodes\n");

        // Marco superior de la tabla
        System.out.printf(" ________");
        IntStream.range(0, report.maxSearchLevel).forEach(depth -> System.out.printf("____________________"));
        IntStream.range(0, report.maxSearchLevelQuiescence).forEach(depth -> System.out.printf("____________"));
        System.out.printf("\n");

        // Nombre de las columnas
        System.out.printf("| Move   ");
        IntStream.range(0, report.maxSearchLevel).forEach(depth -> System.out.printf("|     Level %2d      ", depth + 1));
        IntStream.range(0, report.maxSearchLevelQuiescence).forEach(depth -> System.out.printf("| QLevel %2d ", depth + 1));
        System.out.printf("|\n");

        // Cuerpo
        for (ReportRowMoveDetail moveDetail : report.moveDetails) {
            System.out.printf("| %6s ", moveDetail.move);
            IntStream.range(0, report.maxSearchLevel).forEach(depth -> System.out.printf("| %7d / %7d ", moveDetail.visitedNodesCounters[depth], moveDetail.expectedNodesCounters[depth]));
            IntStream.range(0, report.maxSearchLevelQuiescence).forEach(depth -> System.out.printf("|   %7d ", moveDetail.visitedNodesQuiescenceCounter[depth]));
            System.out.printf("|\n");
        }

        // Totales
        System.out.printf("|--------");
        IntStream.range(0, report.maxSearchLevel).forEach(depth -> System.out.printf("--------------------"));
        IntStream.range(0, report.maxSearchLevelQuiescence).forEach(depth -> System.out.printf("------------"));
        System.out.printf("|\n");
        System.out.printf("| SUM    ");
        IntStream.range(0, report.maxSearchLevel).forEach(depth -> System.out.printf("| %7d / %7d ", report.visitedNodesCounters[depth], report.expectedNodesCounters[depth]));
        IntStream.range(0, report.maxSearchLevelQuiescence).forEach(depth -> System.out.printf("|   %7d ", report.visitedNodesQuiescenceCounter[depth]));
        System.out.printf("|\n");


        // Marco inferior de la tabla
        System.out.printf(" --------");
        IntStream.range(0, report.maxSearchLevel).forEach(depth -> System.out.printf("--------------------"));
        IntStream.range(0, report.maxSearchLevelQuiescence).forEach(depth -> System.out.printf("------------"));
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
        System.out.printf("\n");
    }

    public SearchesReport withCutoffStatics() {
        this.printCutoffStatics = true;
        return this;
    }

    public SearchesReport withNodesVisitedStatics() {
        this.printNodesVisitedStatics = true;
        return this;
    }

    private static class ReportModel {
        int maxSearchLevel;
        long[] expectedNodesCounters;
        long [] visitedNodesCounters;
        int [] cutoffPercentages;


        int maxSearchLevelQuiescence;
        long[] visitedNodesQuiescenceCounter;


        List<ReportRowMoveDetail> moveDetails;
    }

    private static class ReportRowMoveDetail {
        String move;
        int[] expectedNodesCounters;
        int[] visitedNodesCounters;
        int[] cutoffPercentages;

        int[] visitedNodesQuiescenceCounter;
    }
}
