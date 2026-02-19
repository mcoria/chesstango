package net.chesstango.reports.engine;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.reports.Printer;

import java.io.PrintStream;
import java.util.List;

/**
 *
 * @author Mauricio Coria
 */
public class SearchManagerSummaryPrinter implements Printer {
    private PrintStream out;

    @Setter
    @Accessors(chain = true)
    private List<SearchManagerModel> reportModel;

    @Override
    public SearchManagerSummaryPrinter setOut(PrintStream out) {
        this.out = out;
        return this;
    }

    @Override
    public SearchManagerSummaryPrinter print() {
        out.print("--------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        out.print(" Search Manager Statistics\n");

        // Marco superior de la tabla
        out.printf(" __________________________________________________");
        out.printf("____________");
        out.printf("____________");
        out.printf("___________");
        out.printf("\n");

        // Nombre de las columnas

        out.printf("| ENGINE NAME                          ");
        out.printf("| SEARCHES ");
        out.printf("| OpenBook  ");
        out.printf("|   Tree    ");
        out.printf("| Tablebase ");
        out.printf("|\n");

        // Cuerpo
        for (SearchManagerModel row : reportModel) {
            out.printf("| %36s ", row.searchesName);
            out.printf("| %8d ", row.searches);
            out.printf("|  %8d ", row.searchByOpenBookCounter);
            out.printf("|  %8d ", row.searchByTreeCounter);
            out.printf("|  %8d ", row.searchByTablebaseCounter);
            out.printf("|\n");
        }

        // Totales
        out.printf(" --------------------------------------------------");
        out.printf("------------");
        out.printf("------------");
        out.printf("-----------");
        out.printf("\n");

        return this;
    }
}
