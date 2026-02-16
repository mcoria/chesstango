package net.chesstango.reports.tree.summary;

import java.io.PrintStream;
import java.util.List;

/**
 * Este reporte resume las sessiones de engine Tango
 *
 * @author Mauricio Coria
 */
public class SummaryTranspositionPrinter {
    private final List<SummaryModel> reportRows;
    private final PrintStream out;

    public SummaryTranspositionPrinter(PrintStream out, List<SummaryModel> reportRows) {
        this.out = out;
        this.reportRows = reportRows;
    }

    public void printStatics() {
    }
}
