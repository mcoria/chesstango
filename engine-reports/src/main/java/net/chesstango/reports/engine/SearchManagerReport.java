package net.chesstango.reports.engine;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.engine.SearchResponse;
import net.chesstango.reports.Report;

import java.io.PrintStream;
import java.util.List;

/**
 *
 * @author Mauricio Coria
 */
public class SearchManagerReport implements Report {
    private SearchManagerModel reportModel;

    @Setter
    @Accessors(chain = true)
    private String reportTitle = "SearchManagerReport";

    @Override
    public Report printReport(PrintStream out) {
        SearchManagerPrinter searchManagerPrinter = new SearchManagerPrinter();
        searchManagerPrinter.setReportModel(reportModel);
        searchManagerPrinter.setOut(out);
        searchManagerPrinter.print();
        return this;
    }

    public SearchManagerReport withMoveResults(List<SearchResponse> searchResponses) {
        reportModel = new SearchManagerModel().collectStatistics(this.reportTitle, searchResponses);
        return this;
    }
}
