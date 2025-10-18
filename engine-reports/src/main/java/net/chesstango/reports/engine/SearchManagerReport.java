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
public class SearchManagerReport implements Report {
    private List<SearchManagerModel> reportModels = new LinkedList<>();

    @Override
    public Report printReport(PrintStream out) {
        return null;
    }

    public SearchManagerReport withMoveResults(String searchesName, List<SearchResponse> searchResponses) {
        reportModels.add(SearchManagerModel.collectStatics(searchesName, searchResponses));
        return this;
    }
}
