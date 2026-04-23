package net.chesstango.reports.search.evaluation.iteration;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.reports.Report;
import net.chesstango.search.SearchResult;

import java.io.PrintStream;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class EvaluationIterationReport implements Report {

    @Setter
    @Accessors(chain = true)
    private EvaluationIterationModel reportModel;

    @Setter
    @Accessors(chain = true)
    private String reportTitle = "EvaluationIterationReport";

    private PrintStream out;

    @Override
    public EvaluationIterationReport printReport(PrintStream out) {
        this.out = out;
        print();
        return this;
    }

    public EvaluationIterationReport withMoveResults(List<SearchResult> searchResults) {
        this.reportModel = new EvaluationIterationModel().collectStatistics(this.reportTitle, searchResults);
        return this;
    }

    void print() {
        new HeaderPrinter()
                .setReportModel(reportModel)
                .setOut(out)
                .print();


        new EvaluationIterationPrinter()
                .setReportModel(reportModel)
                .setOut(out)
                .print();
    }
}
