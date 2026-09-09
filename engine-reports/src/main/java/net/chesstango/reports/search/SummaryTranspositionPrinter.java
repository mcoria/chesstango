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

        PrinterTxtTable printerTxtTable = new PrinterTxtTable(10).setOut(out);

        printerTxtTable.setTitles("ENGINE NAME", "SEARCHES", "Reads Node", "Read NHits", "Writes", "Updates", "OverWrites", "Reads Comparator", "Read CHits", "Fill Avg");
        reportRows.forEach(row -> {
            printerTxtTable.addRow(row.searchGroupName,
                    Integer.toString(row.searches),
                    Long.toString(row.readsNodeTotal),
                    String.format("%d (%2d%%)", row.readNodeHitsTotal, row.readNodeHitPercentageTotal),

                    Long.toString(row.writesTotal),
                    String.format("%d (%2d%%)", row.updatesTotal, row.updatesPercentageTotal),
                    String.format("%d (%2d%%)", row.overWritesTotal, row.overWritesPercentageTotal),

                    Long.toString(row.readComparatorTotal),
                    String.format("%d (%2d%%)", row.readComparatorHitsTotal, row.readComparatorHitPercentageTotal),

                    String.format("%2d%%", row.mapFillPercentageAvg)
            );
        });
        printerTxtTable.print();

        return this;
    }
}
