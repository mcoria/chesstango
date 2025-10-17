package net.chesstango.reports.detail.nodes;

import java.io.PrintStream;
import java.util.stream.IntStream;

/**
 * @author Mauricio Coria
 */
public class CutoffPrinter {

    private final PrintStream out;
    private final NodesModel reportModel;

    public CutoffPrinter(PrintStream out, NodesModel reportModel) {
        this.out = out;
        this.reportModel = reportModel;
    }

    public void printCutoff() {
        out.printf("Cutoff per search level (higher is better)\n");

        // Marco superior de la tabla
        out.printf(" ________");
        IntStream.range(0, reportModel.maxSearchRLevel).forEach(depth -> out.printf("____________"));
        IntStream.range(0, reportModel.maxSearchQLevel).forEach(depth -> out.printf("____________"));
        out.printf("____________");
        out.printf("\n");

        // Nombre de las columnas
        out.printf("| Move   ");
        IntStream.range(0, reportModel.maxSearchRLevel).forEach(depth -> out.printf("| RLevel %2d ", depth + 1));
        IntStream.range(0, reportModel.maxSearchQLevel).forEach(depth -> out.printf("| QLevel %2d ", depth + 1));
        out.printf("| Cutoff    ");
        out.printf("|\n");

        // Cuerpo
        for (NodesModel.SearchesReportModelDetail moveDetail : reportModel.moveDetails) {
            out.printf("| %6s ", moveDetail.move);
            IntStream.range(0, reportModel.maxSearchRLevel).forEach(depth -> out.printf("|  %6d %% ", moveDetail.cutoffRPercentages[depth]));
            IntStream.range(0, reportModel.maxSearchQLevel).forEach(depth -> out.printf("|  %6d %% ", moveDetail.cutoffQPercentages[depth]));
            out.printf("|  %6d %% ", moveDetail.cutoffPercentageTotal);
            out.printf("|\n");
        }

        // Totales
        out.printf("|--------");
        IntStream.range(0, reportModel.maxSearchRLevel).forEach(depth -> out.printf("|-----------"));
        IntStream.range(0, reportModel.maxSearchQLevel).forEach(depth -> out.printf("|-----------"));
        out.printf("|-----------");
        out.printf("|\n");

        out.printf("| SUM    ");
        IntStream.range(0, reportModel.maxSearchRLevel).forEach(depth -> out.printf("|  %6d %% ", reportModel.cutoffRPercentages[depth]));
        IntStream.range(0, reportModel.maxSearchQLevel).forEach(depth -> out.printf("|  %6d %% ", reportModel.cutoffQPercentages[depth]));
        out.printf("|  %6d %% ", reportModel.cutoffPercentageTotal);
        out.printf("|\n");


        // Marco inferior de la tabla
        out.printf(" --------");
        IntStream.range(0, reportModel.maxSearchRLevel).forEach(depth -> out.printf("------------"));
        IntStream.range(0, reportModel.maxSearchQLevel).forEach(depth -> out.printf("------------"));
        out.printf("------------");
        out.printf("\n\n");
    }

}
