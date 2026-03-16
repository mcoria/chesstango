package net.chesstango.reports.search.nodes.types;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.reports.Report;
import net.chesstango.search.SearchResult;

import java.io.PrintStream;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class NodesTypesReport implements Report {
    @Setter
    @Accessors(chain = true)
    private NodesTypesModel reportModel;

    @Setter
    @Accessors(chain = true)
    private String reportTitle = "NodesReport";

    private PrintStream out;


    @Override
    public NodesTypesReport printReport(PrintStream output) {
        out = output;
        print();
        return this;
    }

    public NodesTypesReport withMoveResults(List<SearchResult> searchResults) {
        this.reportModel = new NodesTypesModel().collectStatistics(this.reportTitle, searchResults);
        return this;
    }

    void print() {
        new HeaderPrinter()
                .setReportModel(reportModel)
                .setOut(out)
                .print();


        new NodesTypesPrinter()
                .setReportModel(reportModel)
                .setOut(out)
                .print();
    }
}
