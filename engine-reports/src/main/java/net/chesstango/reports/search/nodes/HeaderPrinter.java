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
        out.print("--------------------------------------------------------------------------------------------------------------------------------------------------------%n");
        out.printf("NodesReport: %s%n%n", reportModel.searchGroupName);
        out.printf("Searches              : %10d%n", reportModel.searches);
        out.printf("Max             RDepth: %10d%n", reportModel.maxSearchRLevel);
        out.printf("Max             QDepth: %10d%n", reportModel.maxSearchQLevel);
        out.printf("Visited         RNodes: %10d%n", reportModel.visitedRNodesTotal);
        out.printf("Visited         QNodes: %10d%n", reportModel.visitedQNodesTotal);
        out.printf("Visited          Nodes: %10d%n", reportModel.visitedNodesTotal);
        out.printf("Executed         Moves: %10d%n", reportModel.executedMovesTotal);
        out.printf("Cutoff                : %10d %%%n", reportModel.cutoffPercentageTotal);
        out.printf("%n");

        return this;
    }

}
