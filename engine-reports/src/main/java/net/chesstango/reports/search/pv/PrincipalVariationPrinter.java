package net.chesstango.reports.search.pv;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.reports.Printer;
import net.chesstango.reports.PrinterTxtTable;

import java.io.PrintStream;

/**
 * @author Mauricio Coria
 */
class PrincipalVariationPrinter implements Printer {

    @Setter
    @Accessors(chain = true)
    private PrincipalVariationModel reportModel;

    @Setter
    @Accessors(chain = true)
    private PrintStream out;

    @Override
    public PrincipalVariationPrinter print() {
        out.printf("--------------------------------------------------------------------------------------------------------------------------------------------------------%n");
        out.printf("PrincipalVariationReport: %s%n%n", reportModel.searchGroupName);
        out.printf("Searches                    : %10d%n", reportModel.searches);
        out.printf("PV Accuracy average         : %10d%%%n%n", reportModel.pvAccuracyAvgPercentageTotal);

        out.printf("Principal Variations%n");
        PrinterTxtTable printerTxtTable = new PrinterTxtTable(5).setOut(out);
        printerTxtTable.setTextAlignment(PrinterTxtTable.TextAlignment.LEFT, PrinterTxtTable.TextAlignment.RIGHT, PrinterTxtTable.TextAlignment.RIGHT, PrinterTxtTable.TextAlignment.LEFT, PrinterTxtTable.TextAlignment.LEFT);
        printerTxtTable.setTitles("Move", "Evaluation", "pvAccuracy", "PV", "ID");
        reportModel.moveDetails.forEach(row -> {
            printerTxtTable.addRow(
                    row.move,
                    Integer.toString(row.evaluation),
                    String.format("%d%%", row.pvAccuracyPercentage),
                    row.principalVariation,
                    row.id != null ? row.id : "");
        });
        printerTxtTable.print();

        return this;
    }
}
