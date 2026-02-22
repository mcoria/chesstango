package net.chesstango.reports.engine;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.reports.Printer;
import net.chesstango.reports.PrinterTxtTable;

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
        out.printf("--------------------------------------------------------------------------------------------------------------------------------------------------------%n");

        out.printf(" Search Manager Statistics%n");

        PrinterTxtTable printerTxtTable = new PrinterTxtTable(5).setOut(out);

        printerTxtTable.setTitles("ENGINE NAME ", "SEARCHES", "OpenBook", "Tree", "Tablebase");

        reportModel.forEach(moveDetail -> {

            printerTxtTable.addRow(moveDetail.searchesName,
                    Long.toString(moveDetail.searches),
                    Integer.toString(moveDetail.searchByOpenBookCounter),
                    Integer.toString(moveDetail.searchByTreeCounter),
                    Integer.toString(moveDetail.searchByTablebaseCounter));
        });

        printerTxtTable.print();

        return this;
    }
}
