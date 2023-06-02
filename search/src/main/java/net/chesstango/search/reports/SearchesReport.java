package net.chesstango.search.reports;

import net.chesstango.board.moves.Move;
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
        printSearchesStatics("Tango", searchMoveResults);
    }

    public void printSearchesStatics(String engineName, List<SearchMoveResult> searchMoveResults) {
        ReportModel reportModel = collectStatics(engineName, searchMoveResults);

        print(reportModel);
    }

    public void print(ReportModel reportModel) {
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


    public ReportModel collectStatics(String engineName, List<SearchMoveResult> searchMoveResults) {
        ReportModel reportModel = new ReportModel();

        reportModel.engineName = engineName;
        reportModel.moveDetails = new ArrayList<>();

        reportModel.expectedRNodesCounters = new long[30];
        reportModel.visitedRNodesCounters = new long[30];
        reportModel.cutoffPercentages = new int[30];
        reportModel.visitedQNodesCounter = new long[30];

        searchMoveResults.forEach(searchMoveResult -> {
            ReportRowMoveDetail reportModelDetail = new ReportRowMoveDetail();

            Move bestMove = searchMoveResult.getBestMove();
            reportModelDetail.move = String.format("%s%s", bestMove.getFrom().getSquare(), bestMove.getTo().getSquare());

            reportModelDetail.points = searchMoveResult.getEvaluation();
            reportModelDetail.evaluatedGamesCounter = searchMoveResult.getEvaluatedGamesCounter();
            reportModelDetail.principalVariation = getPrincipalVariation(searchMoveResult.getPrincipalVariation());

            reportModelDetail.expectedRNodesCounters = searchMoveResult.getExpectedNodesCounters();
            reportModelDetail.visitedRNodesCounters = searchMoveResult.getVisitedNodesCounters();
            reportModelDetail.visitedQNodesCounters = searchMoveResult.getVisitedNodesQuiescenceCounter();
            reportModelDetail.cutoffPercentages = new int[30];

            for (int i = 0; i < 30; i++) {
                if (reportModelDetail.expectedRNodesCounters[i] <= 0 && reportModelDetail.visitedRNodesCounters[i] > 0) {
                    throw new RuntimeException("expectedNodesCounters[i] <= 0");
                }

                if (reportModelDetail.expectedRNodesCounters[i] > 0 && reportModelDetail.visitedRNodesCounters[i] > 0) {
                    reportModelDetail.cutoffPercentages[i] = (int) (100 - (100 * reportModelDetail.visitedRNodesCounters[i] / reportModelDetail.expectedRNodesCounters[i]));
                }

                reportModel.expectedRNodesCounters[i] += reportModelDetail.expectedRNodesCounters[i];
                reportModel.visitedRNodesCounters[i] += reportModelDetail.visitedRNodesCounters[i];
                reportModel.visitedQNodesCounter[i] += reportModelDetail.visitedQNodesCounters == null ? 0 : reportModelDetail.visitedQNodesCounters[i];
            }

            reportModel.evaluatedGamesCounterTotal += searchMoveResult.getEvaluatedGamesCounter();
            reportModel.moveDetails.add(reportModelDetail);
        });


        for (int i = 0; i < 30; i++) {
            long[] expectedNodesCounters = reportModel.expectedRNodesCounters;
            long[] visitedNodesCounters = reportModel.visitedRNodesCounters;
            long[] visitedNodesQuiescenceCounter = reportModel.visitedQNodesCounter;

            if (expectedNodesCounters[i] > 0) {
                reportModel.cutoffPercentages[i] = (int) (100 - (100 * visitedNodesCounters[i] / expectedNodesCounters[i]));
            }

            if (expectedNodesCounters[i] > 0 && visitedNodesCounters[i] > 0) {
                reportModel.maxSearchRLevel = i + 1; //En el nivel más bajo no exploramos ningun nodo
            }

            if (visitedNodesQuiescenceCounter[i] > 0) {
                reportModel.maxSearchQLevel = i + 1;
            }

            reportModel.visitedRNodesTotal += visitedNodesCounters[i];
            reportModel.visitedQNodesTotal += visitedNodesQuiescenceCounter[i];
        }

        reportModel.visitedNodesTotal = reportModel.visitedRNodesTotal + reportModel.visitedQNodesTotal;

        return reportModel;
    }

    private void printSummary(ReportModel reportModel) {
        System.out.println("----------------------------------------------------------------------------");
        System.out.printf("Moves played by engine: %s\n\n", reportModel.engineName);
        System.out.printf("Visited  Regular Nodes: %8d\n", reportModel.visitedRNodesTotal);
        System.out.printf("Visited         QNodes: %8d\n", reportModel.visitedQNodesTotal);
        System.out.printf("Visited  Total   Nodes: %8d\n", reportModel.visitedNodesTotal);
        System.out.printf("Evaluated        Nodes: %8d\n", reportModel.evaluatedGamesCounterTotal);
        System.out.printf("Max              Depth: %8d\n", reportModel.maxSearchRLevel);
        System.out.printf("Max             QDepth: %8d\n", reportModel.maxSearchQLevel);
        System.out.printf("\n");
    }

    private void printVisitedNodes(ReportModel report) {
        System.out.printf("Visited Nodes\n");

        // Marco superior de la tabla
        System.out.printf(" ________");
        IntStream.range(0, report.maxSearchRLevel).forEach(depth -> System.out.printf("_____________________"));
        IntStream.range(0, report.maxSearchQLevel).forEach(depth -> System.out.printf("____________"));
        System.out.printf("______________");
        System.out.printf("\n");

        // Nombre de las columnas
        System.out.printf("| Move   ");
        IntStream.range(0, report.maxSearchRLevel).forEach(depth -> System.out.printf("|     Level %2d       ", depth + 1));
        IntStream.range(0, report.maxSearchQLevel).forEach(depth -> System.out.printf("| QLevel %2d ", depth + 1));
        System.out.printf("| Evaluations ");
        System.out.printf("|\n");

        // Cuerpo
        for (ReportRowMoveDetail moveDetail : report.moveDetails) {
            System.out.printf("| %6s ", moveDetail.move);
            IntStream.range(0, report.maxSearchRLevel).forEach(depth -> System.out.printf("| %7d / %8d ", moveDetail.visitedRNodesCounters[depth], moveDetail.expectedRNodesCounters[depth]));
            IntStream.range(0, report.maxSearchQLevel).forEach(depth -> System.out.printf("|   %7d ", moveDetail.visitedQNodesCounters[depth]));
            System.out.printf("| %11d ", moveDetail.evaluatedGamesCounter);
            System.out.printf("|\n");
        }

        // Totales
        System.out.printf("|--------");
        IntStream.range(0, report.maxSearchRLevel).forEach(depth -> System.out.printf("---------------------"));
        IntStream.range(0, report.maxSearchQLevel).forEach(depth -> System.out.printf("------------"));
        System.out.printf("|-------------");
        System.out.printf("|\n");
        System.out.printf("| SUM    ");
        IntStream.range(0, report.maxSearchRLevel).forEach(depth -> System.out.printf("| %7d / %8d ", report.visitedRNodesCounters[depth], report.expectedRNodesCounters[depth]));
        IntStream.range(0, report.maxSearchQLevel).forEach(depth -> System.out.printf("|   %7d ", report.visitedQNodesCounter[depth]));
        System.out.printf("| %11d ", report.evaluatedGamesCounterTotal);
        System.out.printf("|\n");


        // Marco inferior de la tabla
        System.out.printf(" ---------");
        IntStream.range(0, report.maxSearchRLevel).forEach(depth -> System.out.printf("---------------------"));
        IntStream.range(0, report.maxSearchQLevel).forEach(depth -> System.out.printf("------------"));
        System.out.printf("-------------");
        System.out.printf("\n\n");
    }

    private void printCutoff(ReportModel report) {
        System.out.printf("Cutoff per search level (higher is better)\n");

        // Marco superior de la tabla
        System.out.printf(" ________");
        IntStream.range(0, report.maxSearchRLevel).forEach(depth -> System.out.printf("___________"));
        System.out.printf("\n");

        // Nombre de las columnas
        System.out.printf("| Move   ");
        IntStream.range(0, report.maxSearchRLevel).forEach(depth -> System.out.printf("| Level %2d ", depth + 1));
        System.out.printf("|\n");

        // Cuerpo
        for (ReportRowMoveDetail moveDetail : report.moveDetails) {
            System.out.printf("| %6s ", moveDetail.move);
            IntStream.range(0, report.maxSearchRLevel).forEach(depth -> System.out.printf("| %6d %% ", moveDetail.cutoffPercentages[depth]));
            System.out.printf("|\n");
        }

        // Marco inferior de la tabla
        System.out.printf(" --------");
        IntStream.range(0, report.maxSearchRLevel).forEach(depth -> System.out.printf("-----------"));
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

    private String getPrincipalVariation(List<Move> principalVariation) {
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

    public static class ReportModel {
        public String engineName;

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

        List<ReportRowMoveDetail> moveDetails;
    }

    private static class ReportRowMoveDetail {
        String move;
        long evaluatedGamesCounter;
        int[] expectedRNodesCounters;
        int[] visitedRNodesCounters;
        int[] cutoffPercentages;
        int[] visitedQNodesCounters;
        String principalVariation;

        int points;
    }
}
