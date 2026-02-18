package net.chesstango.reports.search.nodes;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.reports.Printer;

import java.io.PrintStream;

/**
 * @author Mauricio Coria
 */
class HeaderPrinter implements Printer {
    @Setter
    @Accessors(chain = true)
    private NodesModel reportModel;

    @Setter
    @Accessors(chain = true)
    private PrintStream out;

    @Override
    public HeaderPrinter print() {
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

        return this;
    }

}
