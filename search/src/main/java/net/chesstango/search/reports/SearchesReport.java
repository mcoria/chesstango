package net.chesstango.search.reports;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.reports.searchesreport_ui.PrintCutoff;
import net.chesstango.search.reports.searchesreport_ui.PrintVisitedNodes;

import java.io.PrintStream;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class SearchesReport {
    private boolean printCutoffStatistics;
    private boolean printNodesVisitedStatistics;

    @Setter
    @Accessors(chain = true)
    private String engineName = "Tango";

    @Setter
    @Accessors(chain = true)
    private SearchesReportModel reportModel;


    private PrintStream out;

    public SearchesReport printReport(PrintStream output) {
        out = output;
        print();
        return this;
    }

    protected void print() {
        printSummary();

        if (printNodesVisitedStatistics) {
            new PrintVisitedNodes(out, reportModel).printVisitedNodes();
        }

        if (printCutoffStatistics) {
            new PrintCutoff(out, reportModel).printCutoff();
        }

    }

    private void printSummary() {
        out.printf("----------------------------------------------------------------------------\n\n");

        out.printf("Moves played by engine: %s\n", reportModel.engineName);
        out.printf("Visited         RNodes: %8d\n", reportModel.visitedRNodesTotal);
        out.printf("Visited         QNodes: %8d\n", reportModel.visitedQNodesTotal);
        out.printf("Visited  Total   Nodes: %8d\n", reportModel.visitedNodesTotal);
        out.printf("Cutoff                : %3d%%\n", reportModel.cutoffPercentageTotal);
        out.printf("Executed         Moves: %8d\n", reportModel.executedMovesTotal);
        out.printf("Max             RDepth: %8d\n", reportModel.maxSearchRLevel);
        out.printf("Max             QDepth: %8d\n", reportModel.maxSearchQLevel);
        out.printf("\n");
    }

    public SearchesReport withCutoffStatics() {
        this.printCutoffStatistics = true;
        return this;
    }

    public SearchesReport withNodesVisitedStatics() {
        this.printNodesVisitedStatistics = true;
        return this;
    }

    public SearchesReport withMoveResults(List<SearchMoveResult> searchMoveResults) {
        this.reportModel = SearchesReportModel.collectStatics(this.engineName, searchMoveResults);
        return this;
    }

    public SearchesReport withReportModel(SearchesReportModel searchesReportModel) {
        this.reportModel = searchesReportModel;
        return this;
    }

}
