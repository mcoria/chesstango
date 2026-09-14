package net.chesstango.reports.search.board;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.reports.Printer;
import net.chesstango.reports.PrinterTxtTable;

import java.io.PrintStream;

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

        PrinterTxtTable printerTxtTable = new PrinterTxtTable(5).setOut(out);

        printerTxtTable.setTitles("Move", "Moves", "Depth", "FHF", "Time(ms)");
        reportModel.boardModelModelDetails.forEach(moveDetail -> {
            printerTxtTable.addRow(
                    moveDetail.move,
                    Long.toString(moveDetail.executedMoves),
                    String.format("%.1f", moveDetail.exploredDepth),
                    Integer.toString(moveDetail.failHighPercentage),
                    Long.toString(moveDetail.searchTime)
            );
        });


        printerTxtTable.setBottomRow(
                "SUM",
                Long.toString(reportModel.executedMovesTotal),
                String.format("%.1f", reportModel.exploredDepthAvg),
                Integer.toString(reportModel.failHighPercentageAvg),
                Long.toString(reportModel.searchTimeTotal)
        );

        printerTxtTable.print();

        return this;
    }
}
