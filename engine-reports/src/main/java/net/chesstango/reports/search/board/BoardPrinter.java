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

        PrinterTxtTable printerTxtTable = new PrinterTxtTable(4).setOut(out);

        List<String> tmp = new LinkedList<>();
        tmp.add("Move");
        tmp.add("Moves");
        tmp.add("Depth");
        tmp.add("Time(ms)");

        printerTxtTable.setTitles(tmp.toArray(new String[0]));

        reportModel.boardModelModelDetails.forEach(moveDetail -> {
            List<String> tmpRow = new LinkedList<>();

            tmpRow.add(String.format("%s", moveDetail.move));

            tmpRow.add(Long.toString(moveDetail.executedMoves));

            tmpRow.add(String.format("%.1f", moveDetail.exploredDepth));

            tmpRow.add(Long.toString(moveDetail.searchTime));

            printerTxtTable.addRow(tmpRow.toArray(new String[0]));
        });

        tmp = new LinkedList<>();

        tmp.add("SUM");

        tmp.add(Long.toString(reportModel.executedMovesTotal));

        tmp.add(String.format("%.1f", reportModel.exploredDepthAvg));

        tmp.add(Long.toString(reportModel.searchTimeTotal));

        printerTxtTable.setBottomRow(tmp.toArray(new String[0]));

        printerTxtTable.print();


        return this;
    }
}
