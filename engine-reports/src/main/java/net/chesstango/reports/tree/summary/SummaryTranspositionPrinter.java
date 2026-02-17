package net.chesstango.reports.tree.summary;

import net.chesstango.reports.tree.transposition.TranspositionModel;

import java.io.PrintStream;
import java.util.List;

/**
 * Este reporte resume las sessiones de engine Tango
 *
 * @author Mauricio Coria
 */
public class SummaryTranspositionPrinter {
    private final List<TranspositionModel> reportRows;
    private final PrintStream out;

    public SummaryTranspositionPrinter(PrintStream out, List<TranspositionModel> reportRows) {
        this.out = out;
        this.reportRows = reportRows;
    }

    public void printStatics() {
        out.println("\n Cutoff per search level (higher is better)");

        // Marco superior de la tabla
        out.printf(" ____________________________________");
        out.printf("___________");
        out.printf("___________");
        out.printf("___________");
        out.printf("\n");


        // Nombre de las columnas
        out.printf("|ENGINE NAME                        ");
        out.printf("| SEARCHES ");
        out.printf("| HITS     ");
        out.printf("| REPLACES ");
        out.printf("|\n");

        // Cuerpo
        reportRows.forEach(row -> {
            out.printf("|%35s", row.searchGroupName);
            out.printf("| %8d ", row.searches);
            out.printf("| %8d ", row.hitsTotal);
            out.printf("| %8d ", row.replacesTotal);
            out.printf("|\n");
        });

        // Marco inferior de la tabla
        out.printf(" ------------------------------------");
        out.printf("-----------");
        out.printf("-----------");
        out.printf("-----------");
        out.printf("\n");
    }
}
