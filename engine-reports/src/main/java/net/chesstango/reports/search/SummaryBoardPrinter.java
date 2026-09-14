package net.chesstango.reports.search;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.reports.Printer;
import net.chesstango.reports.PrinterTxtTable;
import net.chesstango.reports.search.board.BoardModel;

import java.io.PrintStream;
import java.util.List;

/**
 * Este reporte resume las sessiones de engine Tango
 *
 * @author Mauricio Coria
 */
public class SummaryBoardPrinter implements Printer {

    @Setter
    @Accessors(chain = true)
    private List<BoardModel> reportRows;

    @Setter
    @Accessors(chain = true)
    private PrintStream out;


    @Override
    public SummaryBoardPrinter print() {
        out.println("\n Board Statistics");

        PrinterTxtTable printerTxtTable = new PrinterTxtTable(6).setOut(out);

        printerTxtTable.setTitles("ENGINE NAME", "SEARCHES", "MOVES", "DEPTH AVG", "FHF %", "Time(ms)");
        reportRows.forEach(row -> {
            printerTxtTable.addRow(row.searchGroupName,
                    Integer.toString(row.searches),
                    Long.toString(row.executedMovesTotal),
                    String.format("%.1f", row.exploredDepthAvg),
                    Integer.toString(row.failHighPercentageAvg),
                    Long.toString(row.searchTimeTotal));
        });
        printerTxtTable.print();

        return this;
    }
}
