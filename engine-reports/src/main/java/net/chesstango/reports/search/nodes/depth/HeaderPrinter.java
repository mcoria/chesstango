package net.chesstango.reports.search.nodes.depth;

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
    private NodesDepthModel reportModel;

    @Setter
    @Accessors(chain = true)
    private PrintStream out;

    @Override
    public HeaderPrinter print() {
        out.printf("--------------------------------------------------------------------------------------------------------------------------------------------------------%n");
        out.printf("NodesDepthReport      : %s%n%n", reportModel.searchGroupName);
        out.printf("Searches              : %10d%n", reportModel.searches);
        out.printf("Max              Depth: %10d%n", reportModel.maxDepth);
        out.printf("Visited          Nodes: %10d%n", reportModel.visitedNodesTotal);
        out.printf("Expected         Nodes: %10d%n", reportModel.expectedNodesTotal);
        out.printf("Cutoff                : %10d %%%n%n", reportModel.cutoffPercentageTotal);
        out.printf("%n");

        return this;
    }

}
