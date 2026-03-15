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
        out.printf("--------------------------------------------------------------------------------------------------------------------------------------------------------%n");
        out.printf("NodesReport: %s%n%n", reportModel.searchGroupName);
        out.printf("Searches              : %10d%n", reportModel.searches);
        out.printf("Max             RDepth: %10d%n", reportModel.maxSearchRLevel);
        //out.printf("Max             QDepth: %10d%n", reportModel.maxSearchQLevel);
        out.printf("Visited         RNodes: %10d%n", reportModel.visitedRNodesTotal);
        out.printf("Cutoff                : %10d %%%n%n", reportModel.cutoffPercentageTotal);

        out.printf("Root             Nodes: %10d%n", reportModel.rootNodeCounterTotal);
        out.printf("Interior         Nodes: %10d%n", reportModel.interiorNodeCounterTotal);
        out.printf("Quiescence       Nodes: %10d%n", reportModel.quiescenceNodeCounterTotal);
        out.printf("Leaf             Nodes: %10d%n", reportModel.leafNodeCounterTotal);
        out.printf("Terminal         Nodes: %10d%n", reportModel.terminalNodeCounterTotal);
        out.printf("Loop             Nodes: %10d%n", reportModel.loopNodeCounterTotal);
        out.printf("Total            Nodes: %10d%n", reportModel.nodeCounterTotal);


        out.printf("%n");

        return this;
    }

}
