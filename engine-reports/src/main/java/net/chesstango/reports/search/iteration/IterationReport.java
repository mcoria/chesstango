package net.chesstango.reports.search.iteration;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.reports.Report;
import net.chesstango.search.SearchResult;

import java.io.PrintStream;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class IterationReport implements Report {

    @Setter
    @Accessors(chain = true)
    private IterationModel reportModel;

    @Setter
    @Accessors(chain = true)
    private String reportTitle = "BoardReport";

    private PrintStream out;

    @Override
    public IterationReport printReport(PrintStream out) {
        this.out = out;
        print();
        return this;
    }

    public IterationReport withMoveResults(List<SearchResult> searchResults) {
        this.reportModel = new IterationModel().collectStatistics(this.reportTitle, searchResults);
        return this;
    }

    void print() {
        new HeaderPrinter()
                .setReportModel(reportModel)
                .setOut(out)
                .print();


        new IterationPrinter()
                .setReportModel(reportModel)
                .setOut(out)
                .print();
    }
}
