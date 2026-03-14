package net.chesstango.reports.search;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.reports.Printer;
import net.chesstango.reports.PrinterTxtTable;
import net.chesstango.reports.search.pv.PrincipalVariationModel;

import java.io.PrintStream;
import java.util.List;

/**
 * @author Mauricio Coria
 */
@Setter
@Accessors(chain = true)
class SummaryPrincipalVariationPrinter implements Printer {
    private List<PrincipalVariationModel> reportRows;

    private PrintStream out;

    @Override
    public SummaryPrincipalVariationPrinter print() {
        out.println("\n Principal Variation Statistics");

        PrinterTxtTable printerTxtTable = new PrinterTxtTable(3).setOut(out);

        printerTxtTable.setTitles("ENGINE NAME", "SEARCHES", "PV Accuracy");
        reportRows.forEach(row -> {
            printerTxtTable.addRow(row.searchGroupName,
                    Integer.toString(row.searches),
                    String.format("%d%%", row.pvAccuracyAvgPercentageTotal));
        });
        printerTxtTable.print();

        return this;
    }
}
