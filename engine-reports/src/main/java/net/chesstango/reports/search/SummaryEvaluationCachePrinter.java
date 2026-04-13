package net.chesstango.reports.search;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.reports.Printer;
import net.chesstango.reports.PrinterTxtTable;
import net.chesstango.reports.search.evaluation.cache.EvaluationCacheModel;

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

        printerTxtTable.setTitles("ENGINE NAME", "SEARCHES", "Evaluations", "Cache Hits", "Read Cache", "Read Cache Hits", "Fill Avg");
        reportRows.forEach(row -> {
            printerTxtTable.addRow(row.searchGroupName,
                    Integer.toString(row.searches),
                    Long.toString(row.evaluationsCounterTotal),
                    String.format("%d (%2d%%)", row.evaluationsCacheHitsCounterTotal, row.evaluationsCacheHitsPercentageTotal),
                    Long.toString(row.readFromCacheCounterTotal),
                    String.format("%d (%2d%%)", row.readFromCacheHitsCounterTotal, row.readFromCacheHitsPercentageTotal),
                    String.format("%2d%%", row.fillPercentageAvg));
        });
        printerTxtTable.print();

        return this;
    }
}
