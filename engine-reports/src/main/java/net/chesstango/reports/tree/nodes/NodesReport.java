package net.chesstango.reports.tree.nodes;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.reports.Report;
import net.chesstango.search.SearchResult;

import java.io.PrintStream;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class NodesReport implements Report {
    private boolean printCutoffStatistics;
    private boolean printNodesVisitedStatistics;

    @Setter
    @Accessors(chain = true)
    private NodesModel reportModel;

    private PrintStream out;

    @Setter
    @Accessors(chain = true)
    private String reportTitle = "NodesReport";


    @Override
    public NodesReport printReport(PrintStream output) {
        out = output;
        print();
        return this;
    }

    protected void print() {
        printSummary();

        if (printNodesVisitedStatistics) {
            new NodesPrinter(out, reportModel).printVisitedNodes();
        }

        if (printCutoffStatistics) {
            new CutoffPrinter(out, reportModel).printCutoff();
        }

    }

    private void printSummary() {
        out.print("--------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        out.printf("NodesReport: %s\n\n", reportModel.searchGroupName);
        out.printf("Searches              : %8d\n", reportModel.searches);
        out.printf("Max             RDepth: %8d\n", reportModel.maxSearchRLevel);
        out.printf("Max             QDepth: %8d\n", reportModel.maxSearchQLevel);
        out.printf("Visited         RNodes: %8d\n", reportModel.visitedRNodesTotal);
        out.printf("Visited         QNodes: %8d\n", reportModel.visitedQNodesTotal);
        out.printf("Visited          Nodes: %8d\n", reportModel.visitedNodesTotal);
        out.printf("Executed         Moves: %8d\n", reportModel.executedMovesTotal);
        out.printf("Cutoff                : %7d%%\n", reportModel.cutoffPercentageTotal);
        out.print("\n");
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
        this.reportModel = NodesModel.collectStatistics(this.reportTitle, searchResults);
        return this;
    }

}
