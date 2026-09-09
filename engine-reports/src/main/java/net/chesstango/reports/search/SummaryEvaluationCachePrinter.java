package net.chesstango.reports.search;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.reports.Printer;
import net.chesstango.reports.PrinterTxtTable;
import net.chesstango.reports.search.evalcache.EvaluationCacheModel;

import java.io.PrintStream;
import java.util.List;

/**
 * Este reporte resume las sessiones de engine Tango
 *
 * @author Mauricio Coria
 */
public class SummaryEvaluationCachePrinter implements Printer {

    @Setter
    @Accessors(chain = true)
    private List<EvaluationCacheModel> reportRows;

    @Setter
    @Accessors(chain = true)
    private PrintStream out;


    @Override
    public SummaryEvaluationCachePrinter print() {
        out.println("\n Evaluation Cache Statistics");

        PrinterTxtTable printerTxtTable = new PrinterTxtTable(7).setOut(out);

        printerTxtTable.setTitles("ENGINE NAME", "SEARCHES", "Reads Node", "Reads NHits", "Reads Comparator", "Reads CHits", "Fill Avg");
        reportRows.forEach(row -> {
            printerTxtTable.addRow(row.searchGroupName,
                    Integer.toString(row.searches),
                    Long.toString(row.readNodesTotal),
                    String.format("%d (%2d%%)", row.readNodeHitsTotal, row.readNodeHitsPercentageTotal),
                    Long.toString(row.readComparatorsTotal),
                    String.format("%d (%2d%%)", row.readComparatorHitsTotal, row.readComparatorHitsPercentageTotal),
                    String.format("%2d%%", row.fillPercentageAvg));
        });
        printerTxtTable.print();

        return this;
    }
}
