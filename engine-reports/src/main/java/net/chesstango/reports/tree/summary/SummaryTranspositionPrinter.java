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
        out.println("\n Transposition Statistics");

        // Marco superior de la tabla
        out.printf(" _______________________________________");
        out.printf("___________");
        out.printf("_______________");
        out.printf("_______________");
        out.printf("\n");


        // Nombre de las columnas
        out.printf("| ENGINE NAME                           ");
        out.printf("| SEARCHES ");
        out.printf("| Hits         ");
        out.printf("| Replaces     ");
        out.printf("|\n");

        // Cuerpo
        reportRows.forEach(row -> {
            out.printf("| %37s ", row.searchGroupName);
            out.printf("| %8d ", row.searches);
            out.printf("| %12d ", row.hitsTotal);
            out.printf("| %12d ", row.replacesTotal);
            out.printf("|\n");
        });

        // Marco inferior de la tabla
        out.printf(" ---------------------------------------");
        out.printf("-----------");
        out.printf("---------------");
        out.printf("---------------");
        out.printf("\n");
    }
}
