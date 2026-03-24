package net.chesstango.reports.search.evaluation.cache;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.reports.Report;
import net.chesstango.reports.search.evaluation.ExportEvaluations;
import net.chesstango.search.SearchResult;

import java.io.PrintStream;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class EvaluationCacheReport implements Report {
    private boolean printEvaluationsStatistics;

    @Setter
    @Accessors(chain = true)
    private String reportTitle = "EvaluationCacheReport";

    @Setter
    @Accessors(chain = true)
    private EvaluationCacheModel reportModel;

    private PrintStream out;

    @Override
    public EvaluationCacheReport printReport(PrintStream output) {
        out = output;
        print();
        return this;
    }

    private void print() {
        new EvaluationCachePrinter()
                .setOut(out)
                .setReportModel(reportModel)
                .print();

    }

    public EvaluationCacheReport withEvaluationsStatistics() {
        this.printEvaluationsStatistics = true;
        return this;
    }


    public EvaluationCacheReport withMoveResults(List<SearchResult> searchResults) {
        this.reportModel = new EvaluationCacheModel().collectStatistics(this.reportTitle, searchResults);
        return this;
    }

}
