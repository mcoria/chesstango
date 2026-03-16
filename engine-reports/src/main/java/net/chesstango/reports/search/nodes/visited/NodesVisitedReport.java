package net.chesstango.reports.search.nodes.visited;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.reports.Report;
import net.chesstango.search.SearchResult;

import java.io.PrintStream;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class NodesVisitedReport implements Report {
    private boolean printCutoffStatistics;
    private boolean printNodesVisitedStatistics;

    @Setter
    @Accessors(chain = true)
    private NodesVisitedModel reportModel;

    @Setter
    @Accessors(chain = true)
    private String reportTitle = "NodesReport";

    private PrintStream out;


    @Override
    public NodesVisitedReport printReport(PrintStream output) {
        out = output;
        print();
        return this;
    }


    public NodesVisitedReport withCutoffStatistics() {
        this.printCutoffStatistics = true;
        return this;
    }

    public NodesVisitedReport withNodesVisitedStatistics() {
        this.printNodesVisitedStatistics = true;
        return this;
    }

    public NodesVisitedReport withMoveResults(List<SearchResult> searchResults) {
        this.reportModel = new NodesVisitedModel().collectStatistics(this.reportTitle, searchResults);
        return this;
    }

    void print() {
        new HeaderPrinter()
                .setReportModel(reportModel)
                .setOut(out)
                .print();

        if (printNodesVisitedStatistics) {
            new NodesVisitedPrinter()
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
