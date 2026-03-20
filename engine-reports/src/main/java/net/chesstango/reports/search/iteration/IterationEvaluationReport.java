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
public class IterationEvaluationReport implements Report {

    @Setter
    @Accessors(chain = true)
    private IterationEvaluationModel reportModel;

    @Setter
    @Accessors(chain = true)
    private String reportTitle = "BoardReport";

    private PrintStream out;

    @Override
    public IterationEvaluationReport printReport(PrintStream out) {
        this.out = out;
        print();
        return this;
    }

    public IterationEvaluationReport withMoveResults(List<SearchResult> searchResults) {
        this.reportModel = new IterationEvaluationModel().collectStatistics(this.reportTitle, searchResults);
        return this;
    }

    void print() {
        new HeaderPrinter()
                .setReportModel(reportModel)
                .setOut(out)
                .print();


        new IterationEvaluationPrinter()
                .setReportModel(reportModel)
                .setOut(out)
                .print();
    }
}
