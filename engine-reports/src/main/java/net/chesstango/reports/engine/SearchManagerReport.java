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
        new SearchManagerPrinter()
                .setReportModel(reportModel)
                .setOut(out)
                .print();
        return this;
    }

    public SearchManagerReport withMoveResults(List<SearchResponse> searchResponses) {
        reportModel = SearchManagerModel.collectStatics(this.reportTitle, searchResponses);
        return this;
    }
}
