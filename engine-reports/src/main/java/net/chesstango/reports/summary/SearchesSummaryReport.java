package net.chesstango.reports.summary;

import net.chesstango.reports.Report;
import net.chesstango.search.SearchResult;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Este reporte resume las sessiones de engine Tango
 *
 * @author Mauricio Coria
 */
public class SearchesSummaryReport implements Report {
    private final List<SearchesSummaryModel> searchesSummaryModels = new LinkedList<>();
    private boolean printNodesVisitedStatistics;
    private boolean printCutoffStatistics;
    private PrintStream out;

    @Override
    public SearchesSummaryReport printReport(PrintStream output) {
        out = output;
        print();
        return this;
    }


    public void addSearchesByTreeSummaryModel(String searchesName, List<SearchResult> searches) {
        searchesSummaryModels.add(SearchesSummaryModel.collectStatics(searchesName, searches));
    }

    private void print() {
        if (printNodesVisitedStatistics) {
            new NodesPrinter(out, searchesSummaryModels)
                    .printNodesVisitedStaticsByType()
                    .printNodesVisitedStatics()
                    .printNodesVisitedStaticsAvg();
        }


        if (printCutoffStatistics) {
            new CutoffPrinter(out, searchesSummaryModels)
                    .printCutoffStatics();
        }
    }


    public SearchesSummaryReport withNodesVisitedStatistics() {
        this.printNodesVisitedStatistics = true;
        return this;
    }

    public SearchesSummaryReport withCutoffStatistics() {
        this.printCutoffStatistics = true;
        return this;
    }

}
