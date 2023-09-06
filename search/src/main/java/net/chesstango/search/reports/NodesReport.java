package net.chesstango.search.reports;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.reports.nodesreport_ui.PrintCutoff;
import net.chesstango.search.reports.nodesreport_ui.PrintVisitedNodes;

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
    private String engineName = "Tango";

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
        out.printf("----------------------------------------------------------------------------\n\n");

        out.printf("Moves played by engine: %s\n", reportModel.engineName);
        out.printf("Visited         RNodes: %8d\n", reportModel.visitedRNodesTotal);
        out.printf("Visited         QNodes: %8d\n", reportModel.visitedQNodesTotal);
        out.printf("Visited          Nodes: %8d\n", reportModel.visitedNodesTotal);
        out.printf("Cutoff                : %7d%%\n", reportModel.cutoffPercentageTotal);
        out.printf("Executed         Moves: %8d\n", reportModel.executedMovesTotal);
        out.printf("Max             RDepth: %8d\n", reportModel.maxSearchRLevel);
        out.printf("Max             QDepth: %8d\n", reportModel.maxSearchQLevel);
        out.printf("\n");
    }

    public NodesReport withCutoffStatics() {
        this.printCutoffStatistics = true;
        return this;
    }

    public NodesReport withNodesVisitedStatics() {
        this.printNodesVisitedStatistics = true;
        return this;
    }

    public NodesReport withMoveResults(List<SearchMoveResult> searchMoveResults) {
        this.reportModel = NodesReportModel.collectStatistics(this.engineName, searchMoveResults);
        return this;
    }

    public NodesReport withReportModel(NodesReportModel nodesReportModel) {
        this.reportModel = nodesReportModel;
        return this;
    }

}
