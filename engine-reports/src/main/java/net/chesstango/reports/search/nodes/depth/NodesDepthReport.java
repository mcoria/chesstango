package net.chesstango.reports.search.nodes.depth;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.reports.Report;
import net.chesstango.search.SearchResult;

import java.io.PrintStream;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class NodesDepthReport implements Report {
    private boolean printCutoffStatistics;
    private boolean printNodesVisitedStatistics;

    @Setter
    @Accessors(chain = true)
    private NodesDepthModel reportModel;

    @Setter
    @Accessors(chain = true)
    private String reportTitle = "NodesReport";

    private PrintStream out;


    @Override
    public NodesDepthReport printReport(PrintStream output) {
        out = output;
        print();
        return this;
    }


    public NodesDepthReport withCutoffStatistics() {
        this.printCutoffStatistics = true;
        return this;
    }

    public NodesDepthReport withNodesVisitedStatistics() {
        this.printNodesVisitedStatistics = true;
        return this;
    }

    public NodesDepthReport withMoveResults(List<SearchResult> searchResults) {
        this.reportModel = new NodesDepthModel().collectStatistics(this.reportTitle, searchResults);
        return this;
    }

    void print() {
        new HeaderPrinter()
                .setReportModel(reportModel)
                .setOut(out)
                .print();

        if (printNodesVisitedStatistics) {
            new NodesDepthPrinter()
                    .setReportModel(reportModel)
                    .setOut(out)
                    .print();
        }

        if (printCutoffStatistics) {
            new CutoffPrinter()
                    .setReportModel(reportModel)
                    .setOut(out)
                    .print();
        }
    }
}
