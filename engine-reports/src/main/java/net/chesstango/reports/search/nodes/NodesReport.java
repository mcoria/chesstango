package net.chesstango.reports.search.nodes;

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

    @Setter
    @Accessors(chain = true)
    private String reportTitle = "NodesReport";

    private PrintStream out;


    @Override
    public NodesReport printReport(PrintStream output) {
        out = output;
        print();
        return this;
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
        this.reportModel = new NodesModel().collectStatistics(this.reportTitle, searchResults);
        return this;
    }

    void print() {
        new HeaderPrinter()
                .setReportModel(reportModel)
                .setOut(out)
                .print();

        if (printNodesVisitedStatistics) {
            new NodesPrinter()
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
