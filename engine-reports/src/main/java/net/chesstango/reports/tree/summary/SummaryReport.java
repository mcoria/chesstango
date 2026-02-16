package net.chesstango.reports.tree.summary;

import net.chesstango.reports.Report;
import net.chesstango.reports.tree.nodes.NodesModel;
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
    private boolean printTranspositionStatistics;

    private PrintStream out;

    @Override
    public SummaryReport printReport(PrintStream output) {
        out = output;
        print();
        return this;
    }

    public SummaryReport addSearchesByTreeSummaryModel(String searchGroupName, List<SearchResult> searches) {
        summaryModels.add(SummaryModel.collectStatics(searchGroupName, searches));
        return this;
    }

    private void print() {
        if (printNodesVisitedStatistics) {
            List<NodesModel> reportRows = summaryModels
                    .stream()
                    .map(SummaryModel::getNodesModel)
                    .toList();
            new SummaryNodesPrinter(out, reportRows)
                    .printNodesVisitedStaticsByType()
                    .printNodesVisitedStatics()
                    .printNodesVisitedStaticsAvg();
        }

        if (printCutoffStatistics) {
            List<NodesModel> reportRows = summaryModels
                    .stream()
                    .map(SummaryModel::getNodesModel)
                    .toList();
            new SummaryCutoffPrinter(out, reportRows)
                    .printCutoffStatics();
        }

        if (printTranspositionStatistics) {
            new SummaryTranspositionPrinter(out, summaryModels)
                    .printStatics();
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

    public SummaryReport withTranspositionStatistics() {
        this.printTranspositionStatistics = true;
        return this;
    }

}
