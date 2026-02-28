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

        PrinterTxtTable printerTxtTable = new PrinterTxtTable(7).setOut(out);

        printerTxtTable.setTitles("ENGINE NAME", "SEARCHES", "Reads", "Read Hits", "Writes", "Updates", "OverWrites");
        reportRows.forEach(row -> {
            printerTxtTable.addRow(row.searchGroupName,
                    Integer.toString(row.searches),
                    Long.toString(row.readsTotal),
                    String.format("%d (%2d%%)", row.readHitsTotal, row.readHitPercentageTotal),
                    Long.toString(row.writesTotal),
                    String.format("%d (%2d%%)", row.updatesTotal, row.updatesPercentageTotal),
                    String.format("%d (%2d%%)", row.overWritesTotal, row.overWritesPercentageTotal));
        });
        printerTxtTable.print();

        return this;
    }
}
