package net.chesstango.reports.summary;

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
public class SearchesSummaryReport {
    private final List<SearchesSummaryModel> searchesSummaryModels = new LinkedList<>();
    private boolean printNodesVisitedStatistics;
    private boolean printCutoffStatistics;
    private boolean breakByColor;
    private PrintStream out;

    public SearchesSummaryReport printReport(PrintStream output) {
        out = output;
        print();
        return this;
    }


    public void addSearchesByTreeSummaryModel(String engineName, List<SearchResult> searchesWhite, List<SearchResult> searchesBlack) {
        if (breakByColor) {
            if (!searchesWhite.isEmpty()) {
                searchesSummaryModels.add(SearchesSummaryModel.collectStatics(String.format("%s white", engineName), searchesWhite));
            }
            if (!searchesBlack.isEmpty()) {
                searchesSummaryModels.add(SearchesSummaryModel.collectStatics(String.format("%s black", engineName), searchesBlack));
            }
        } else {
            List<SearchResult> searches = new ArrayList<>();
            searches.addAll(searchesWhite);
            searches.addAll(searchesBlack);

            if (!searches.isEmpty()) {
                searchesSummaryModels.add(SearchesSummaryModel.collectStatics(engineName, searches));
            }
        }
    }

    private void print() {
        if (printNodesVisitedStatistics) {
            new PrintNodesVisitedStatistics(out, searchesSummaryModels)
                    .printNodesVisitedStaticsByType()
                    .printNodesVisitedStatics()
                    .printNodesVisitedStaticsAvg();
        }


        if (printCutoffStatistics) {
            new PrintCutoffStatics(out, searchesSummaryModels)
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

    public SearchesSummaryReport breakByColor() {
        this.breakByColor = true;
        return this;
    }

}
