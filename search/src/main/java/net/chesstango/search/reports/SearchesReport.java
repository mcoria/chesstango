package net.chesstango.search.reports;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.statics.EvaluationEntry;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HexFormat;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Mauricio Coria
 */
public class SearchesReport {
    private boolean printCutoffStatics;
    private boolean printNodesVisitedStatics;
    private boolean printPrincipalVariation;
    private boolean exportEvaluations;

    @Setter
    @Accessors(chain = true)
    private String engineName = "Tango";

    @Setter
    @Accessors(chain = true)
    private SearchesReportModel reportModel;


    private PrintStream out;

    public SearchesReport printReport(PrintStream output) {
        out = output;
        print();
        return this;
    }


    protected void print() {
        printSummary();

        if (printNodesVisitedStatics) {
            printVisitedNodes();
        }
        if (printCutoffStatics) {
            printCutoff();
        }
        if (printPrincipalVariation) {
            printPrincipalVariation();
        }

        if (exportEvaluations) {
            exportEvaluations();
        }

    }

    private void printSummary() {
        out.printf("----------------------------------------------------------------------------\n\n");

        out.printf("Moves played by engine: %s\n", reportModel.engineName);
        out.printf("Visited  Regular Nodes: %8d\n", reportModel.visitedRNodesTotal);
        out.printf("Visited         QNodes: %8d\n", reportModel.visitedQNodesTotal);
        out.printf("Visited  Total   Nodes: %8d\n", reportModel.visitedNodesTotal);
        out.printf("Evaluated        Nodes: %8d\n", reportModel.evaluatedGamesCounterTotal);
        out.printf("Max              Depth: %8d\n", reportModel.maxSearchRLevel);
        out.printf("Max             QDepth: %8d\n", reportModel.maxSearchQLevel);
        out.printf("\n");
    }

    private void printVisitedNodes() {
        out.printf("Visited Nodes\n");

        // Marco superior de la tabla
        out.printf(" ________");
        IntStream.range(0, reportModel.maxSearchRLevel).forEach(depth -> out.printf("_____________________"));
        IntStream.range(0, reportModel.maxSearchQLevel).forEach(depth -> out.printf("____________"));
        out.printf("______________");
        out.printf("_______________");
        out.printf("_____________");
        out.printf("______________");
        out.printf("\n");

        // Nombre de las columnas
        out.printf("| Move   ");
        IntStream.range(0, reportModel.maxSearchRLevel).forEach(depth -> out.printf("|     Level %2d       ", depth + 1));
        IntStream.range(0, reportModel.maxSearchQLevel).forEach(depth -> out.printf("| QLevel %2d ", depth + 1));
        out.printf("| Evaluations ");
        out.printf("| UEvaluations ");
        out.printf("| Collisions ");
        out.printf("| PCollisions ");
        out.printf("|\n");

        // Cuerpo
        for (SearchesReportModel.SearchReportModelDetail moveDetail : reportModel.moveDetails) {
            out.printf("| %6s ", moveDetail.move);
            IntStream.range(0, reportModel.maxSearchRLevel).forEach(depth -> out.printf("| %7d / %8d ", moveDetail.visitedRNodesCounters[depth], moveDetail.expectedRNodesCounters[depth]));
            IntStream.range(0, reportModel.maxSearchQLevel).forEach(depth -> out.printf("|   %7d ", moveDetail.visitedQNodesCounters[depth]));
            out.printf("| %11d ", moveDetail.evaluatedGamesCounter);
            out.printf("| %12d ", moveDetail.distinctEvaluatedGamesCounter);
            out.printf("| %10d ", moveDetail.distinctEvaluatedGamesCounterCollisions);
            out.printf("| %11d ", moveDetail.collisionPercentage);
            out.printf("|\n");
        }

        // Totales
        out.printf("|--------");
        IntStream.range(0, reportModel.maxSearchRLevel).forEach(depth -> out.printf("---------------------"));
        IntStream.range(0, reportModel.maxSearchQLevel).forEach(depth -> out.printf("------------"));
        out.printf("--------------");
        out.printf("---------------");
        out.printf("-------------");
        out.printf("--------------");
        out.printf("|\n");
        out.printf("| SUM    ");
        IntStream.range(0, reportModel.maxSearchRLevel).forEach(depth -> out.printf("| %7d / %8d ", reportModel.visitedRNodesCounters[depth], reportModel.expectedRNodesCounters[depth]));
        IntStream.range(0, reportModel.maxSearchQLevel).forEach(depth -> out.printf("|   %7d ", reportModel.visitedQNodesCounter[depth]));
        out.printf("| %11d ", reportModel.evaluatedGamesCounterTotal);
        out.printf("| %12d ", reportModel.evaluatedGamesCounterTotal);
        out.printf("| %10d ", reportModel.evaluatedGamesCounterTotal);
        out.printf("| %11d ", reportModel.evaluatedGamesCounterTotal);
        out.printf("|\n");


        // Marco inferior de la tabla
        out.printf(" ---------");
        IntStream.range(0, reportModel.maxSearchRLevel).forEach(depth -> out.printf("---------------------"));
        IntStream.range(0, reportModel.maxSearchQLevel).forEach(depth -> out.printf("------------"));
        out.printf("--------------");
        out.printf("---------------");
        out.printf("-------------");
        out.printf("--------------");
        out.printf("\n\n");
    }

    private void printCutoff() {
        out.printf("Cutoff per search level (higher is better)\n");

        // Marco superior de la tabla
        out.printf(" ________");
        IntStream.range(0, reportModel.maxSearchRLevel).forEach(depth -> out.printf("___________"));
        out.printf("\n");

        // Nombre de las columnas
        out.printf("| Move   ");
        IntStream.range(0, reportModel.maxSearchRLevel).forEach(depth -> out.printf("| Level %2d ", depth + 1));
        out.printf("|\n");

        // Cuerpo
        for (SearchesReportModel.SearchReportModelDetail moveDetail : reportModel.moveDetails) {
            out.printf("| %6s ", moveDetail.move);
            IntStream.range(0, reportModel.maxSearchRLevel).forEach(depth -> out.printf("| %6d %% ", moveDetail.cutoffPercentages[depth]));
            out.printf("|\n");
        }

        // Marco inferior de la tabla
        out.printf(" --------");
        IntStream.range(0, reportModel.maxSearchRLevel).forEach(depth -> out.printf("-----------"));
        out.printf("\n\n");
    }

    private void printPrincipalVariation() {
        out.printf("Principal Variations\n");


        // Nombre de las columnas
        out.printf("Move\n");

        // Cuerpo
        for (SearchesReportModel.SearchReportModelDetail moveDetail : reportModel.moveDetails) {
            out.printf("%6s:", moveDetail.move);
            out.printf(" %s; Points = %d ", moveDetail.principalVariation, moveDetail.points);
            if (moveDetail.points == GameEvaluator.WHITE_WON || moveDetail.points == GameEvaluator.BLACK_WON) {
                out.printf(" MATE");
            }
            out.printf("\n");
        }
    }

    private void exportEvaluations() {
        int fileCounter = 0;
        // Cuerpo
        for (SearchesReportModel.SearchReportModelDetail moveDetail : reportModel.moveDetails) {
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

    public SearchesReport withMoveResults(List<SearchMoveResult> searchMoveResults){
        this.reportModel = SearchesReportModel.collectStatics(this.engineName, searchMoveResults);
        return this;
    }

    public SearchesReport withReportModel(SearchesReportModel searchesReportModel) {
        this.reportModel = searchesReportModel;
        return this;
    }

}
