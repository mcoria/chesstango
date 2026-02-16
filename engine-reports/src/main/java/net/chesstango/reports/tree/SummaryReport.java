package net.chesstango.reports.tree;

import net.chesstango.reports.Report;
import net.chesstango.search.SearchResult;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

/**
 * Este reporte resume las sessiones de engine Tango
 *
 * @author Mauricio Coria
 */
public class SummaryReport implements Report {
    /**
     * Observar que es una lista a diferencia de detail
     */
    private final List<SummaryModel> summaryModels = new LinkedList<>();

    private boolean printNodesVisitedStatistics;
    private boolean printCutoffStatistics;
    private PrintStream out;

    @Override
    public SummaryReport printReport(PrintStream output) {
        out = output;
        print();
        return this;
    }


    public SummaryReport addSearchesByTreeSummaryModel(String searchesName, List<SearchResult> searches) {
        summaryModels.add(SummaryModel.collectStatics(searchesName, searches));
        return this;
    }

    private void print() {
        if (printNodesVisitedStatistics) {
            new SummaryNodesPrinter(out, summaryModels)
                    .printNodesVisitedStaticsByType()
                    .printNodesVisitedStatics()
                    .printNodesVisitedStaticsAvg();
        }


        if (printCutoffStatistics) {
            new SummaryCutoffPrinter(out, summaryModels)
                    .printCutoffStatics();
        }
    }


    public SummaryReport withNodesVisitedStatistics() {
        this.printNodesVisitedStatistics = true;
        return this;
    }

    public SummaryReport withCutoffStatistics() {
        this.printCutoffStatistics = true;
        return this;
    }

}
