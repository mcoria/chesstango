package net.chesstango.search.reports;

import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.SANEncoder;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMoveResult;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Mauricio Coria
 */
public class SearchesReport {

    private boolean printCutoffStatics;
    private boolean printNodesVisitedStatics;
    private boolean printPrincipalVariation;

    public void printSearchesStatics(List<SearchMoveResult> searchMoveResults) {
        ReportModel reportModel = collectStatics(searchMoveResults);

        printSummary(reportModel);

        if (printNodesVisitedStatics) {
            printVisitedNodes(reportModel);
        }
        if (printCutoffStatics) {
            printCutoff(reportModel);
        }
        if (printPrincipalVariation) {
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

            int[] expectedNodesCounters = searchMoveResult.getExpectedNodesCounters();
            int[] visitedNodesCounters = searchMoveResult.getVisitedNodesCounters();
            int[] visitedNodesQuiescenceCounter = searchMoveResult.getVisitedNodesQuiescenceCounter();
            int[] cutoffPercentages = new int[30];

            for (int i = 0; i < 30; i++) {

                if (expectedNodesCounters[i] <= 0 && visitedNodesCounters[i] > 0) {
                    throw new RuntimeException("expectedNodesCounters[i] <= 0");
                }

                if (expectedNodesCounters[i] > 0 && visitedNodesCounters[i] > 0) {
                    cutoffPercentages[i] = (int) (100 - (100 * visitedNodesCounters[i] / expectedNodesCounters[i]));
                }

                reportModel.expectedNodesCounters[i] += expectedNodesCounters[i];
                reportModel.visitedNodesCounters[i] += visitedNodesCounters[i];
                reportModel.visitedNodesQuiescenceCounter[i] += visitedNodesQuiescenceCounter == null ? 0 : visitedNodesQuiescenceCounter[i];

                reportModel.visitedNodesTotal += visitedNodesCounters[i];
                reportModel.expectedNodesTotal += expectedNodesCounters[i];
                reportModel.visitedNodesQuiescenceTotal += visitedNodesQuiescenceCounter == null ? 0 : visitedNodesQuiescenceCounter[i];
            }

            reportModelDetail.expectedNodesCounters = expectedNodesCounters;
            reportModelDetail.visitedNodesCounters = visitedNodesCounters;
            reportModelDetail.visitedNodesQuiescenceCounter = visitedNodesQuiescenceCounter;
            reportModelDetail.cutoffPercentages = cutoffPercentages;

            if (searchMoveResult.getPrincipalVariation() == null) {
                reportModelDetail.principalVariation = "-";
            } else {
                StringBuilder sb = new StringBuilder();
                for (Move move : searchMoveResult.getPrincipalVariation()) {
                    sb.append(String.format("%s ", String.format("%s%s", move.getFrom().getSquare(), move.getTo().getSquare())));
                }
                reportModelDetail.principalVariation = sb.toString();
            }

            Move bestMove = searchMoveResult.getBestMove();
            reportModelDetail.move = String.format("%s%s", bestMove.getFrom().getSquare(), bestMove.getTo().getSquare());

            reportModelDetail.points = searchMoveResult.getEvaluation();

            reportModelDetail.evaluatedGamesCounter = searchMoveResult.getEvaluatedGamesCounter();
            reportModel.evaluatedGamesCounterTotal += searchMoveResult.getEvaluatedGamesCounter();

            reportModel.moveDetails.add(reportModelDetail);
        });


        for (int i = 0; i < 30; i++) {
            long[] expectedNodesCounters = reportModel.expectedNodesCounters;
            long[] visitedNodesCounters = reportModel.visitedNodesCounters;
            long[] visitedNodesQuiescenceCounter = reportModel.visitedNodesQuiescenceCounter;

            if (expectedNodesCounters[i] > 0) {
                reportModel.cutoffPercentages[i] = (int) (100 - (100 * visitedNodesCounters[i] / expectedNodesCounters[i]));
            }

            if (expectedNodesCounters[i] > 0 && visitedNodesCounters[i] > 0) {
                reportModel.maxSearchLevel = i + 1; //En el nivel más bajo no exploramos ningun nodo
            }

            if (visitedNodesQuiescenceCounter[i] > 0) {
                reportModel.maxSearchLevelQuiescence = i + 1;
            }
        }

        reportModel.visitedNodesSummaryTotal = reportModel.visitedNodesTotal + reportModel.visitedNodesQuiescenceTotal;

        return reportModel;
    }

    private void printSummary(ReportModel reportModel) {
        System.out.printf("Visited  Regular Nodes: %8d\n", reportModel.visitedNodesTotal);
        System.out.printf("Visited         QNodes: %8d\n", reportModel.visitedNodesQuiescenceTotal);
        System.out.printf("Visited  Total   Nodes: %8d\n", reportModel.visitedNodesSummaryTotal);
        System.out.printf("Evaluated        Nodes: %8d\n", reportModel.evaluatedGamesCounterTotal);
        System.out.printf("Max              Depth: %8d\n", reportModel.maxSearchLevel);
        System.out.printf("Max             QDepth: %8d\n", reportModel.maxSearchLevelQuiescence);
        System.out.printf("\n");
    }

    private void printVisitedNodes(ReportModel report) {
        System.out.printf("Visited Nodes\n");

        // Marco superior de la tabla
        System.out.printf(" ________");
        IntStream.range(0, report.maxSearchLevel).forEach(depth -> System.out.printf("_____________________"));
        IntStream.range(0, report.maxSearchLevelQuiescence).forEach(depth -> System.out.printf("____________"));
        System.out.printf("______________");
        System.out.printf("\n");

        // Nombre de las columnas
        System.out.printf("| Move   ");
        IntStream.range(0, report.maxSearchLevel).forEach(depth -> System.out.printf("|     Level %2d       ", depth + 1));
        IntStream.range(0, report.maxSearchLevelQuiescence).forEach(depth -> System.out.printf("| QLevel %2d ", depth + 1));
        System.out.printf("| Evaluations ");
        System.out.printf("|\n");

        // Cuerpo
        for (ReportRowMoveDetail moveDetail : report.moveDetails) {
            System.out.printf("| %6s ", moveDetail.move);
            IntStream.range(0, report.maxSearchLevel).forEach(depth -> System.out.printf("| %7d / %8d ", moveDetail.visitedNodesCounters[depth], moveDetail.expectedNodesCounters[depth]));
            IntStream.range(0, report.maxSearchLevelQuiescence).forEach(depth -> System.out.printf("|   %7d ", moveDetail.visitedNodesQuiescenceCounter[depth]));
            System.out.printf("| %11d ", moveDetail.evaluatedGamesCounter);
            System.out.printf("|\n");
        }

        // Totales
        System.out.printf("|--------");
        IntStream.range(0, report.maxSearchLevel).forEach(depth -> System.out.printf("---------------------"));
        IntStream.range(0, report.maxSearchLevelQuiescence).forEach(depth -> System.out.printf("------------"));
        System.out.printf("|-------------");
        System.out.printf("|\n");
        System.out.printf("| SUM    ");
        IntStream.range(0, report.maxSearchLevel).forEach(depth -> System.out.printf("| %7d / %8d ", report.visitedNodesCounters[depth], report.expectedNodesCounters[depth]));
        IntStream.range(0, report.maxSearchLevelQuiescence).forEach(depth -> System.out.printf("|   %7d ", report.visitedNodesQuiescenceCounter[depth]));
        System.out.printf("| %11d ", report.evaluatedGamesCounterTotal);
        System.out.printf("|\n");


        // Marco inferior de la tabla
        System.out.printf(" ---------");
        IntStream.range(0, report.maxSearchLevel).forEach(depth -> System.out.printf("---------------------"));
        IntStream.range(0, report.maxSearchLevelQuiescence).forEach(depth -> System.out.printf("------------"));
        System.out.printf("-------------");
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
            if (moveDetail.points == GameEvaluator.WHITE_WON || moveDetail.points == GameEvaluator.BLACK_WON) {
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
        long[] visitedNodesCounters;
        int[] cutoffPercentages;

        int maxSearchLevelQuiescence;
        long[] visitedNodesQuiescenceCounter;

        long expectedNodesTotal;
        long visitedNodesTotal;
        long visitedNodesQuiescenceTotal;

        long visitedNodesSummaryTotal;

        long evaluatedGamesCounterTotal;

        List<ReportRowMoveDetail> moveDetails;
    }

    private static class ReportRowMoveDetail {
        String move;
        long evaluatedGamesCounter;
        int[] expectedNodesCounters;
        int[] visitedNodesCounters;
        int[] cutoffPercentages;
        int[] visitedNodesQuiescenceCounter;
        String principalVariation;

        int points;
    }
}
