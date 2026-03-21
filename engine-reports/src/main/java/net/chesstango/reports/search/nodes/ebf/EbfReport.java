package net.chesstango.reports.search.nodes.ebf;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.reports.Report;
import net.chesstango.search.SearchResult;

import java.io.PrintStream;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class EbfReport implements Report {

    @Setter
    @Accessors(chain = true)
    private EbfModel reportModel;

    @Setter
    @Accessors(chain = true)
    private String reportTitle = "NodesReport";

    private PrintStream out;


    @Override
    public EbfReport printReport(PrintStream output) {
        out = output;
        print();
        return this;
    }


    public EbfReport withMoveResults(List<SearchResult> searchResults) {
        this.reportModel = new EbfModel().collectStatistics(this.reportTitle, searchResults);
        return this;
    }

    void print() {
        new HeaderPrinter()
                .setReportModel(reportModel)
                .setOut(out)
                .print();

        new EbfPrinter()
                .setReportModel(reportModel)
                .setOut(out)
                .print();
    }
}
