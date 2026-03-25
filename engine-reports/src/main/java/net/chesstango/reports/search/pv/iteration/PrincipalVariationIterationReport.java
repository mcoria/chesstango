package net.chesstango.reports.search.pv.iteration;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.reports.Report;
import net.chesstango.search.SearchResult;

import java.io.PrintStream;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class PrincipalVariationIterationReport implements Report {

    @Setter
    @Accessors(chain = true)
    private PrincipalVariationIterationModel reportModel;

    @Setter
    @Accessors(chain = true)
    private String reportTitle = "PrincipalVariationIterationReport";


    @Override
    public PrincipalVariationIterationReport printReport(PrintStream output) {
        new PrincipalVariationIterationPrinter()
                .setReportModel(reportModel)
                .setOut(output)
                .print();
        return this;
    }


    public PrincipalVariationIterationReport withMoveResults(List<SearchResult> searchResults) {
        this.reportModel = new PrincipalVariationIterationModel().collectStatistics(this.reportTitle, searchResults);
        return this;
    }
}
