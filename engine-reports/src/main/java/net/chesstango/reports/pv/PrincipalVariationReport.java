package net.chesstango.reports.pv;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.search.SearchResult;

import java.io.PrintStream;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class PrincipalVariationReport {

    @Setter
    @Accessors(chain = true)
    private String reportTitle = "PrincipalVariationReport";

    @Setter
    @Accessors(chain = true)
    private PrincipalVariationReportModel reportModel;

    private PrintStream out;

    public PrincipalVariationReport printReport(PrintStream output) {
        out = output;
        print();
        return this;
    }

    protected void print() {
        out.printf("--------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        out.printf("--------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        out.printf("PrincipalVariationReport: %s\n\n", reportModel.reportTitle);

        new PrincipalVariationReportPrinter(out, reportModel).printPrincipalVariation();
    }


    public PrincipalVariationReport withMoveResults(List<SearchResult> searchResults) {
        this.reportModel = PrincipalVariationReportModel.collectStatics(this.reportTitle, searchResults);
        return this;
    }
}
