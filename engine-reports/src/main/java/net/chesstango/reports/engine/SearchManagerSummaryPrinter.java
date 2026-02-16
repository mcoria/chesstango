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
    public void setOut(PrintStream out) {
        this.out = out;
    }

    @Override
    public void print() {
        out.print("\n Search Manager Statistics\n");

        // Marco superior de la tabla
        out.printf(" ________________________________________________");
        out.printf("____________");
        out.printf("____________");
        out.printf("___________");
        out.printf("\n");

        // Nombre de las columnas

        out.printf("| ENGINE NAME                        | SEARCHES ");
        out.printf("| OpenBook  ");
        out.printf("|   Tree    ");
        out.printf("| Tablebase ");
        out.printf("|\n");

        // Cuerpo
        for (SearchManagerModel row : reportModel) {
            out.printf("| %34s | %8d ", row.searchesName, row.searches);
            out.printf("|  %8d ", row.searchByOpenBookCounter);
            out.printf("|  %8d ", row.searchByTreeCounter);
            out.printf("|  %8d ", row.searchByTablebaseCounter);
            out.printf("|\n");
        }

        // Totales
        out.printf(" -----------------------------------------------");
        out.printf("------------");
        out.printf("------------");
        out.printf("------------");
        out.printf("\n");
    }
}
