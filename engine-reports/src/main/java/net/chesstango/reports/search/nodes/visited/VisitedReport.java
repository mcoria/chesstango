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
public class VisitedReport implements Report {
    private boolean printCutoffStatistics;
    private boolean printNodesVisitedStatistics;

    @Setter
    @Accessors(chain = true)
    private VisitedModel reportModel;

    @Setter
    @Accessors(chain = true)
    private String reportTitle = "NodesReport";

    private PrintStream out;


    @Override
    public VisitedReport printReport(PrintStream output) {
        out = output;
        print();
        return this;
    }


    public VisitedReport withCutoffStatistics() {
        this.printCutoffStatistics = true;
        return this;
    }

    public VisitedReport withNodesVisitedStatistics() {
        this.printNodesVisitedStatistics = true;
        return this;
    }

    public VisitedReport withMoveResults(List<SearchResult> searchResults) {
        this.reportModel = new VisitedModel().collectStatistics(this.reportTitle, searchResults);
        return this;
    }

    void print() {
        new HeaderPrinter()
                .setReportModel(reportModel)
                .setOut(out)
                .print();

        if (printNodesVisitedStatistics) {
            new VisitedPrinter()
                    .setReportModel(reportModel)
                    .setOut(out)
                    .print();
        }

        if (printCutoffStatistics) {
            new VisitedPercentagesPrinter()
                    .setReportModel(reportModel)
                    .setOut(out)
                    .print();
        }
    }
}
