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
    private boolean withNodesVisitedPercentageStatistics;
    private boolean withNodesVisitedStatistics;

    @Setter
    @Accessors(chain = true)
    private VisitedModel reportModel;

    @Setter
    @Accessors(chain = true)
    private String reportTitle = "VisitedReport";

    private PrintStream out;


    @Override
    public VisitedReport printReport(PrintStream output) {
        out = output;
        print();
        return this;
    }


    public VisitedReport withNodesVisitedPercentageStatistics() {
        this.withNodesVisitedPercentageStatistics = true;
        return this;
    }

    public VisitedReport withNodesVisitedStatistics() {
        this.withNodesVisitedStatistics = true;
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

        if (withNodesVisitedStatistics) {
            new VisitedPrinter()
                    .setReportModel(reportModel)
                    .setOut(out)
                    .print();
        }

        if (withNodesVisitedPercentageStatistics) {
            new VisitedPercentagesPrinter()
                    .setReportModel(reportModel)
                    .setOut(out)
                    .print();
        }
    }
}
