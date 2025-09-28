package net.chesstango.reports.nodes;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.search.SearchResult;

import java.io.PrintStream;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class NodesReport {
    private boolean printCutoffStatistics;
    private boolean printNodesVisitedStatistics;

    @Setter
    @Accessors(chain = true)
    private String reportTitle = "NodesReport";

    @Setter
    @Accessors(chain = true)
    private NodesReportModel reportModel;


    private PrintStream out;

    public NodesReport printReport(PrintStream output) {
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
        out.printf("--------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        out.printf("--------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        out.printf("NodesReport: %s\n\n", reportModel.reportTitle);
        out.printf("Searches              : %8d\n", reportModel.searches);
        out.printf("Max             RDepth: %8d\n", reportModel.maxSearchRLevel);
        out.printf("Max             QDepth: %8d\n", reportModel.maxSearchQLevel);
        out.printf("Visited         RNodes: %8d\n", reportModel.visitedRNodesTotal);
        out.printf("Visited         QNodes: %8d\n", reportModel.visitedQNodesTotal);
        out.printf("Visited          Nodes: %8d\n", reportModel.visitedNodesTotal);
        out.printf("Executed         Moves: %8d\n", reportModel.executedMovesTotal);
        out.printf("Cutoff                : %7d%%\n", reportModel.cutoffPercentageTotal);
        out.printf("\n");
    }

    public NodesReport withCutoffStatistics() {
        this.printCutoffStatistics = true;
        return this;
    }

    public NodesReport withNodesVisitedStatistics() {
        this.printNodesVisitedStatistics = true;
        return this;
    }

    public NodesReport withMoveResults(List<SearchResult> searchResults) {
        this.reportModel = NodesReportModel.collectStatistics(this.reportTitle, searchResults);
        return this;
    }

    public NodesReport withReportModel(NodesReportModel nodesReportModel) {
        this.reportModel = nodesReportModel;
        return this;
    }

}
