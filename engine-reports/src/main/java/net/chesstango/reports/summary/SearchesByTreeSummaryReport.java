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
public class SearchesByTreeSummaryReport {
    private final List<SearchesByTreeSummaryModel> searchesByTreeSummaryModels = new LinkedList<>();
    private boolean printNodesVisitedStatistics;
    private boolean printCutoffStatistics;
    private boolean breakByColor;
    private PrintStream out;

    public SearchesByTreeSummaryReport printReport(PrintStream output) {
        out = output;
        print();
        return this;
    }


    public void addSearchesByTreeSummaryModel(String engineName, List<SearchResult> searchesWhite, List<SearchResult> searchesBlack) {
        if (breakByColor) {
            if (!searchesWhite.isEmpty()) {
                searchesByTreeSummaryModels.add(SearchesByTreeSummaryModel.collectStatics(String.format("%s white", engineName), searchesWhite));
            }
            if (!searchesBlack.isEmpty()) {
                searchesByTreeSummaryModels.add(SearchesByTreeSummaryModel.collectStatics(String.format("%s black", engineName), searchesBlack));
            }
        } else {
            List<SearchResult> searches = new ArrayList<>();
            searches.addAll(searchesWhite);
            searches.addAll(searchesBlack);

            if (!searches.isEmpty()) {
                searchesByTreeSummaryModels.add(SearchesByTreeSummaryModel.collectStatics(engineName, searches));
            }
        }
    }

    private void print() {
        if (printNodesVisitedStatistics) {
            new PrintNodesVisitedStatistics(out, searchesByTreeSummaryModels)
                    .printNodesVisitedStaticsByType()
                    .printNodesVisitedStatics()
                    .printNodesVisitedStaticsAvg();
        }


        if (printCutoffStatistics) {
            new PrintCutoffStatics(out, searchesByTreeSummaryModels)
                    .printCutoffStatics();
        }
    }


    public SearchesByTreeSummaryReport withNodesVisitedStatistics() {
        this.printNodesVisitedStatistics = true;
        return this;
    }

    public SearchesByTreeSummaryReport withCutoffStatistics() {
        this.printCutoffStatistics = true;
        return this;
    }

    public SearchesByTreeSummaryReport breakByColor() {
        this.breakByColor = true;
        return this;
    }

}
