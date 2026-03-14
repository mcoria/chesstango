package net.chesstango.reports.engine;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.reports.Printer;
import net.chesstango.reports.PrinterTxtTable;

import java.io.PrintStream;

/**
 *
 * @author Mauricio Coria
 */
public class SearchManagerPrinter implements Printer {
    private PrintStream out;

    @Setter
    @Accessors(chain = true)
    private SearchManagerModel reportModel;

    @Override
    public SearchManagerPrinter setOut(PrintStream out) {
        this.out = out;
        return this;
    }

    @Override
    public SearchManagerPrinter print() {
        out.printf("--------------------------------------------------------------------------------------------------------------------------------------------------------%n");
        out.printf(" Search Manager Statistics%n");

        PrinterTxtTable printerTxtTable = new PrinterTxtTable(5).setOut(out);

        printerTxtTable.setTitles("Move", "OpenBook", "Tree", "Tablebase", "SearchTime(ms)");

        reportModel.moveDetail.forEach(moveDetail -> {
            printerTxtTable.addRow(
                    moveDetail.move,
                    moveDetail.type == SearchManagerModel.MoveType.OpenBook ? moveDetail.value : "",
                    moveDetail.type == SearchManagerModel.MoveType.Tree ? moveDetail.value : "",
                    moveDetail.type == SearchManagerModel.MoveType.Tablebase ? moveDetail.value : "",
                    Long.toString(moveDetail.searchTime)
            );
        });

        printerTxtTable.setBottomRow(
                "COUNT",
                Integer.toString(reportModel.searchByOpenBookCounter),
                Integer.toString(reportModel.searchByTreeCounter),
                Integer.toString(reportModel.searchByTablebaseCounter),
                Long.toString(reportModel.searchTimeTotal)
        );

        printerTxtTable.print();


        return this;
    }
}
