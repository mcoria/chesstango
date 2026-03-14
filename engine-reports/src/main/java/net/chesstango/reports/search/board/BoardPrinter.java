package net.chesstango.reports.search.board;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.reports.Printer;
import net.chesstango.reports.PrinterTxtTable;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

public class BoardPrinter implements Printer {

    @Setter
    @Accessors(chain = true)
    private BoardModel reportModel;

    @Setter
    @Accessors(chain = true)
    private PrintStream out;

    @Override
    public BoardPrinter print() {
        out.print("Board Statistics\n");

        PrinterTxtTable printerTxtTable = new PrinterTxtTable(3).setOut(out);

        List<String> tmp = new LinkedList<>();
        tmp.add("Move");
        tmp.add("Exec Moves");
        tmp.add("SearchTime(ms)");

        printerTxtTable.setTitles(tmp.toArray(new String[0]));

        reportModel.boardModelModelDetails.forEach(moveDetail -> {
            List<String> tmpRow = new LinkedList<>();

            tmpRow.add(String.format("%s", moveDetail.move));

            tmpRow.add(String.format("%d", moveDetail.executedMoves));

            tmpRow.add(String.format("%d", moveDetail.searchTime));

            printerTxtTable.addRow(tmpRow.toArray(new String[0]));
        });

        tmp = new LinkedList<>();

        tmp.add("SUM");

        tmp.add(String.format("%d", reportModel.executedMovesTotal));

        tmp.add(String.format("%d", reportModel.searchTimeTotal));

        printerTxtTable.setBottomRow(tmp.toArray(new String[0]));

        printerTxtTable.print();


        return this;
    }
}
