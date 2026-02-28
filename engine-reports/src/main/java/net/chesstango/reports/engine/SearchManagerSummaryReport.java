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
        SearchManagerSummaryPrinter searchManagerPrinter = new SearchManagerSummaryPrinter();
        searchManagerPrinter.setReportModel(reportModelList);
        searchManagerPrinter.setOut(out);
        searchManagerPrinter.print();
        return this;
    }

    public SearchManagerSummaryReport addSearchResponses(String searchesName, List<SearchResponse> searchResponses) {
        reportModelList.add(new SearchManagerModel().collectStatistics(searchesName, searchResponses));
        return this;
    }
}
