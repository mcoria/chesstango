package net.chesstango.reports.tree.transposition;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.reports.Report;
import net.chesstango.reports.tree.SummaryReport;
import net.chesstango.reports.tree.nodes.NodesModel;
import net.chesstango.search.SearchResult;

import java.io.PrintStream;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class TranspositionReport implements Report {

    @Setter
    @Accessors(chain = true)
    private TranspositionModel transpositionModel;

    @Setter
    @Accessors(chain = true)
    private String reportTitle = "TranspositionReport";

    private PrintStream out;

    @Override
    public TranspositionReport printReport(PrintStream output) {
        out = output;
        print();
        return this;
    }

    private void print() {
        printSummary();

        new TranspositionPrinter(out, transpositionModel)
                .print();

    }

    public TranspositionReport withMoveResults(List<SearchResult> searchResults) {
        this.transpositionModel = TranspositionModel.collectStatistics(this.reportTitle, searchResults);
        return this;
    }

    private void printSummary() {
        out.print("--------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        out.printf("TranspositionReport: %s\n\n", transpositionModel.reportTitle);
        out.printf("Searches              : %8d\n", transpositionModel.searches);
        out.printf("Hits                  : %8d\n", transpositionModel.hitsTotal);
        out.printf("Replaces              : %8d\n", transpositionModel.replacesTotal);
        out.print("\n");
    }
}
