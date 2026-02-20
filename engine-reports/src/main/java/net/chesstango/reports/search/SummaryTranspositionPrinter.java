package net.chesstango.reports.search;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.reports.Printer;
import net.chesstango.reports.PrinterTxtTable;
import net.chesstango.reports.search.transposition.TranspositionModel;

import java.io.PrintStream;
import java.util.List;

/**
 * Este reporte resume las sessiones de engine Tango
 *
 * @author Mauricio Coria
 */
public class SummaryTranspositionPrinter implements Printer {

    @Setter
    @Accessors(chain = true)
    private List<TranspositionModel> reportRows;

    @Setter
    @Accessors(chain = true)
    private PrintStream out;


    @Override
    public SummaryTranspositionPrinter print() {
        out.println("\n Transposition Statistics");

        /*
        // Marco superior de la tabla
        out.printf(" _______________________________________");
        out.printf("___________");
        out.printf("_______________");
        out.printf("_____________________");
        out.printf("_______________");
        out.printf("_____________________");
        out.printf("\n");


        // Nombre de las columnas
        out.printf("| ENGINE NAME                           ");
        out.printf("| SEARCHES ");
        out.printf("| Reads        ");
        out.printf("| Read Hits          ");
        out.printf("| Writes       ");
        out.printf("| OverWrites         ");
        out.printf("|\n");

        // Cuerpo
        reportRows.forEach(row -> {
            out.printf("| %37s ", row.searchGroupName);
            out.printf("| %8d ", row.searches);
            out.printf("| %12d ", row.readsTotal);
            out.printf("| %12d (%2d%%) ", row.readHitsTotal, row.readHitPercentageTotal);
            out.printf("| %12d ", row.writesTotal);
            out.printf("| %12d (%2d%%) ", row.overWritesTotal, row.overWritePercentageTotal);
            out.printf("|\n");
        });

        // Marco inferior de la tabla
        out.printf(" ---------------------------------------");
        out.printf("-----------");
        out.printf("---------------");
        out.printf("---------------------");
        out.printf("---------------");
        out.printf("---------------------");
        out.printf("\n");

         */

        PrinterTxtTable printerTxtTable = new PrinterTxtTable(6).setOut(out);

        printerTxtTable.setTitles("ENGINE NAME", "SEARCHES", "Reads", "Read Hits", "Writes", "OverWrites");
        reportRows.forEach(row -> {
            printerTxtTable.addRow(row.searchGroupName,
                    Integer.toString(row.searches),
                    Long.toString(row.readsTotal),
                    String.format("%d (%2d%%)", row.readHitsTotal, row.readHitPercentageTotal),
                    Long.toString(row.writesTotal),
                    String.format("%d (%2d%%)", row.overWritesTotal, row.overWritePercentageTotal));
        });
        printerTxtTable.print();

        return this;
    }
}
