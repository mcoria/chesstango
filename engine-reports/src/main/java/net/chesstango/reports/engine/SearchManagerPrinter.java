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
        out.print("--------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        out.print(" Search Manager Statistics\n");

        PrinterTxtTable printerTxtTable = new PrinterTxtTable(4).setOut(out);

        printerTxtTable.setTitles("Move", "OpenBook", "Tree", "Tablebase");

        reportModel.moveDetail.forEach(moveDetail -> {
            printerTxtTable.addRow(
                    moveDetail.move,
                    moveDetail.type == SearchManagerModel.MoveType.OpenBook ? "X" : "",
                    moveDetail.type == SearchManagerModel.MoveType.Tree ? "X" : "",
                    moveDetail.type == SearchManagerModel.MoveType.Tablebase ? "X" : ""
            );
        });

        printerTxtTable.setBottomRow(
                "SUM",
                Integer.toString(reportModel.searchByOpenBookCounter),
                Integer.toString(reportModel.searchByTreeCounter),
                Integer.toString(reportModel.searchByTablebaseCounter)
        );

        printerTxtTable.print();


        return this;
    }
}
