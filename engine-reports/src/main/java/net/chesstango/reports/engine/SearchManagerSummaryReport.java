package net.chesstango.reports.engine;

import net.chesstango.engine.SearchResponse;
import net.chesstango.reports.Report;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Mauricio Coria
 */
public class SearchManagerSummaryReport implements Report {
    private List<SearchManagerModel> reportModelList = new LinkedList<>();

    @Override
    public Report printReport(PrintStream out) {
        new SearchManagerSummaryPrinter()
                .setOut(out)
                .setReportModel(reportModelList)
                .print();
        return this;
    }

    public SearchManagerSummaryReport addSearchResponses(String searchesName, List<SearchResponse> searchResponses) {
        reportModelList.add(SearchManagerModel.collectStatics(searchesName, searchResponses));
        return this;
    }
}
