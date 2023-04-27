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
        ReportModel reportRowModel = new ReportModel();

        reportRowModel.moveDetails = new ArrayList<>();

        reportRowModel.expectedNodesCounters = new long[30];
        reportRowModel.visitedNodesCounters = new long[30];
        reportRowModel.cutoffPercentages = new int[30];

        searchMoveResults.forEach(searchMoveResult -> {
            Move bestMove = searchMoveResult.getBestMove();

            ReportRowMoveDetail moveDetail = new ReportRowMoveDetail();
            
            moveDetail.move = String.format("%s%s", bestMove.getFrom().getSquare(), bestMove.getTo().getSquare());

            int[] expectedNodesCounters = searchMoveResult.getExpectedNodesCounters();
            int[] visitedNodesCounters = searchMoveResult.getVisitedNodesCounters();
            int[] cutoffPercentages = new int[30];

            for (int i = 0; i < 30; i++) {
                if (expectedNodesCounters[i] <= 0 && visitedNodesCounters[i] > 0) {
                    throw new RuntimeException("expectedNodesCounters[i] <= 0");
                }
                if (expectedNodesCounters[i] > 0) {
                    cutoffPercentages[i] = (int) (100 - (100 * visitedNodesCounters[i] / expectedNodesCounters[i]));
                }

                reportRowModel.expectedNodesCounters[i] += expectedNodesCounters[i];
                reportRowModel.visitedNodesCounters[i] += visitedNodesCounters[i];
            }

            moveDetail.expectedNodesCounters = expectedNodesCounters;
            moveDetail.visitedNodesCounters = visitedNodesCounters;
            moveDetail.cutoffPercentages = cutoffPercentages;

            reportRowModel.moveDetails.add(moveDetail);
        });

        for (int i = 0; i < 30; i++) {
            long[] expectedNodesCounters = reportRowModel.expectedNodesCounters;
            long[] visitedNodesCounters = reportRowModel.visitedNodesCounters;
            int[] cutoffPercentages = reportRowModel.cutoffPercentages;

            if (expectedNodesCounters[i] <= 0 && visitedNodesCounters[i] > 0) {
                throw new RuntimeException("expectedNodesCounters[i] <= 0");
            }
            if (expectedNodesCounters[i] > 0) {
                cutoffPercentages[i] = (int) (100 - (100 * visitedNodesCounters[i] / expectedNodesCounters[i]));

                if (reportRowModel.maxSearchLevel < i) {
                    reportRowModel.maxSearchLevel = i;
                }
            }
        }


        return reportRowModel;
    }


    private void printVisitedNodes(ReportModel report) {
        System.out.printf("Visited Nodes\n");

        // Marco superior de la tabla
        System.out.printf(" ________");
        IntStream.range(0, report.maxSearchLevel).forEach(depth -> System.out.printf("____________________"));
        System.out.printf("\n");

        // Nombre de las columnas
        System.out.printf("| Move   ");
        IntStream.range(0, report.maxSearchLevel).forEach(depth -> System.out.printf("|     Level %2d      ", depth + 1));
        System.out.printf("|\n");

        // Cuerpo
        for (ReportRowMoveDetail moveDetail : report.moveDetails) {
            System.out.printf("| %6s ", moveDetail.move);
            IntStream.range(0, report.maxSearchLevel).forEach(depth -> System.out.printf("| %7d / %7d ", moveDetail.visitedNodesCounters[depth], moveDetail.expectedNodesCounters[depth]));
            System.out.printf("|\n");
        }

        // Totales
        System.out.printf("|--------");
        IntStream.range(0, report.maxSearchLevel).forEach(depth -> System.out.printf("--------------------"));
        System.out.printf("|\n");
        System.out.printf("| SUM    ");
        IntStream.range(0, report.maxSearchLevel).forEach(depth -> System.out.printf("| %7d / %7d ", report.visitedNodesCounters[depth], report.expectedNodesCounters[depth]));
        System.out.printf("|\n");


        // Marco inferior de la tabla
        System.out.printf(" --------");
        IntStream.range(0, report.maxSearchLevel).forEach(depth -> System.out.printf("--------------------"));
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

        List<ReportRowMoveDetail> moveDetails;
    }

    private static class ReportRowMoveDetail {
        String move;
        int[] expectedNodesCounters;
        int[] visitedNodesCounters;
        int[] cutoffPercentages;
    }
}
