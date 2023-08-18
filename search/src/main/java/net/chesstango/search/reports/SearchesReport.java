package net.chesstango.search.reports;

import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.statics.EvaluationEntry;
import net.chesstango.search.smart.statics.EvaluationStatics;
import net.chesstango.search.smart.statics.RNodeStatics;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * @author Mauricio Coria
 */
public class SearchesReport {
    private boolean printCutoffStatics;
    private boolean printNodesVisitedStatics;
    private boolean printPrincipalVariation;
    private boolean exportEvaluations;

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

        if (exportEvaluations) {
            exportEvaluations(reportModel);
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
            reportModelDetail.principalVariation = getPrincipalVariation(searchMoveResult.getPrincipalVariation());

            if(searchMoveResult.getEvaluationStatics() != null) {
                collectStaticsEvaluationStatics(reportModelDetail, searchMoveResult);
            }

            if(searchMoveResult.getRegularNodeStatics()!=null){
                collectStaticsRegularNodeStatic(reportModel, reportModelDetail, searchMoveResult);
            }

            reportModelDetail.visitedQNodesCounters = searchMoveResult.getVisitedNodesQuiescenceCounter();
            for (int i = 0; i < 30; i++) {
                reportModel.visitedQNodesCounter[i] += reportModelDetail.visitedQNodesCounters == null ? 0 : reportModelDetail.visitedQNodesCounters[i];
            }

			reportModel.evaluatedGamesCounterTotal += reportModelDetail.evaluatedGamesCounter;
            reportModel.moveDetails.add(reportModelDetail);
        });


        /**
         * Totales sumarizados
         */
        for (int i = 0; i < 30; i++) {
            if (reportModel.expectedRNodesCounters[i] > 0) {
                reportModel.cutoffPercentages[i] = (int) (100 - (100 * reportModel.visitedRNodesCounters[i] / reportModel.expectedRNodesCounters[i]));
            }

            if (reportModel.expectedRNodesCounters[i] > 0 && reportModel.visitedRNodesCounters[i] > 0) {
                reportModel.maxSearchRLevel = i + 1; //En el nivel más bajo no exploramos ningun nodo
            }

            if (reportModel.visitedQNodesCounter[i] > 0) {
                reportModel.maxSearchQLevel = i + 1;
            }

            reportModel.visitedRNodesTotal += reportModel.visitedRNodesCounters[i];
            reportModel.visitedQNodesTotal += reportModel.visitedQNodesCounter[i];
        }

        reportModel.visitedNodesTotal = reportModel.visitedRNodesTotal + reportModel.visitedQNodesTotal;

        return reportModel;
    }

    private void collectStaticsRegularNodeStatic(ReportModel reportModel, ReportRowMoveDetail reportModelDetail, SearchMoveResult searchMoveResult) {
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

            reportModel.expectedRNodesCounters[i] += reportModelDetail.expectedRNodesCounters[i];
            reportModel.visitedRNodesCounters[i] += reportModelDetail.visitedRNodesCounters[i];
        }
    }

    private void collectStaticsEvaluationStatics(ReportRowMoveDetail reportModelDetail, SearchMoveResult searchMoveResult) {
        EvaluationStatics evaluationStatics = searchMoveResult.getEvaluationStatics();
                
        reportModelDetail.evaluatedGamesCounter = evaluationStatics.evaluatedGamesCounter();
        reportModelDetail.evaluations = evaluationStatics.evaluations();


        reportModelDetail.distinctEvaluatedGamesCounter = evaluationStatics.evaluations().size();
        reportModelDetail.distinctEvaluatedGamesCounterCollisions = evaluationStatics.evaluatedGamesCounter() - evaluationStatics.evaluations().parallelStream().mapToInt(EvaluationEntry::getValue).distinct().count();

        /*
         * Cuando TT reuse está habilitado y depth=1 se puede dar que no se evaluan algunas posiciones
         */
        if(reportModelDetail.distinctEvaluatedGamesCounter != 0){
            reportModelDetail.collisionPercentage =  (100 * reportModelDetail.distinctEvaluatedGamesCounterCollisions) / reportModelDetail.distinctEvaluatedGamesCounter;
        }
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
        System.out.printf("_______________");
        System.out.printf("_____________");
        System.out.printf("______________");
        System.out.printf("\n");

        // Nombre de las columnas
        System.out.printf("| Move   ");
        IntStream.range(0, report.maxSearchRLevel).forEach(depth -> System.out.printf("|     Level %2d       ", depth + 1));
        IntStream.range(0, report.maxSearchQLevel).forEach(depth -> System.out.printf("| QLevel %2d ", depth + 1));
        System.out.printf("| Evaluations ");
        System.out.printf("| UEvaluations ");
        System.out.printf("| Collisions ");
        System.out.printf("| PCollisions ");
        System.out.printf("|\n");

        // Cuerpo
        for (ReportRowMoveDetail moveDetail : report.moveDetails) {
            System.out.printf("| %6s ", moveDetail.move);
            IntStream.range(0, report.maxSearchRLevel).forEach(depth -> System.out.printf("| %7d / %8d ", moveDetail.visitedRNodesCounters[depth], moveDetail.expectedRNodesCounters[depth]));
            IntStream.range(0, report.maxSearchQLevel).forEach(depth -> System.out.printf("|   %7d ", moveDetail.visitedQNodesCounters[depth]));
            System.out.printf("| %11d ", moveDetail.evaluatedGamesCounter);
            System.out.printf("| %12d ", moveDetail.distinctEvaluatedGamesCounter);
            System.out.printf("| %10d ", moveDetail.distinctEvaluatedGamesCounterCollisions);
            System.out.printf("| %11d ", moveDetail.collisionPercentage);
            System.out.printf("|\n");
        }

        // Totales
        System.out.printf("|--------");
        IntStream.range(0, report.maxSearchRLevel).forEach(depth -> System.out.printf("---------------------"));
        IntStream.range(0, report.maxSearchQLevel).forEach(depth -> System.out.printf("------------"));
        System.out.printf("--------------");
        System.out.printf("---------------");
        System.out.printf("-------------");
        System.out.printf("--------------");
        System.out.printf("|\n");
        System.out.printf("| SUM    ");
        IntStream.range(0, report.maxSearchRLevel).forEach(depth -> System.out.printf("| %7d / %8d ", report.visitedRNodesCounters[depth], report.expectedRNodesCounters[depth]));
        IntStream.range(0, report.maxSearchQLevel).forEach(depth -> System.out.printf("|   %7d ", report.visitedQNodesCounter[depth]));
        System.out.printf("| %11d ", report.evaluatedGamesCounterTotal);
        System.out.printf("| %12d ", report.evaluatedGamesCounterTotal);
        System.out.printf("| %10d ", report.evaluatedGamesCounterTotal);
        System.out.printf("| %11d ", report.evaluatedGamesCounterTotal);
        System.out.printf("|\n");


        // Marco inferior de la tabla
        System.out.printf(" ---------");
        IntStream.range(0, report.maxSearchRLevel).forEach(depth -> System.out.printf("---------------------"));
        IntStream.range(0, report.maxSearchQLevel).forEach(depth -> System.out.printf("------------"));
        System.out.printf("--------------");
        System.out.printf("---------------");
        System.out.printf("-------------");
        System.out.printf("--------------");
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

    private void exportEvaluations(ReportModel report) {
        int fileCounter = 0;
        // Cuerpo
        for (ReportRowMoveDetail moveDetail : report.moveDetails) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(String.format("evaluations-%d.txt", fileCounter)))) {

                writer.append(String.format("%6s:", moveDetail.move));
                writer.append(String.format(" %s; Points = %d ", moveDetail.principalVariation, moveDetail.points));
                if (moveDetail.points == GameEvaluator.WHITE_WON || moveDetail.points == GameEvaluator.BLACK_WON) {
                    writer.append(String.format(" MATE"));
                }
                writer.append(String.format("\n"));

                HexFormat hexFormat = HexFormat.of().withUpperCase();
                for (EvaluationEntry evaluation : moveDetail.evaluations) {
                    writer.append(String.format("0x%sL\t%d\n", hexFormat.formatHex(longToByte(evaluation.getKey())), evaluation.getValue()));
                }
                writer.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            fileCounter++;
        }
    }

    private byte[] longToByte(long lng) {
        byte[] b = new byte[]{
                (byte) (lng >> 56),
                (byte) (lng >> 48),
                (byte) (lng >> 40),
                (byte) (lng >> 32),
                (byte) (lng >> 24),
                (byte) (lng >> 16),
                (byte) (lng >> 8),
                (byte) lng
        };
        return b;
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

    public SearchesReport withExportEvaluations() {
        this.exportEvaluations = true;
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
}
