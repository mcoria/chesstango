package net.chesstango.reports.detail.pv;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.reports.Report;
import net.chesstango.search.SearchResult;

import java.io.PrintStream;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class PrincipalVariationReport implements Report {

    @Setter
    @Accessors(chain = true)
    private String reportTitle = "PrincipalVariationReport";

    @Setter
    @Accessors(chain = true)
    private PrincipalVariationModel reportModel;

    private PrintStream out;

    @Override
    public PrincipalVariationReport printReport(PrintStream output) {
        out = output;
        print();
        return this;
    }

    protected void print() {
        out.printf("--------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        out.printf("--------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        out.printf("PrincipalVariationReport: %s\n\n", reportModel.reportTitle);

        new PrincipalVariationPrinter(out, reportModel).printPrincipalVariation();
    }


    public PrincipalVariationReport withMoveResults(List<SearchResult> searchResults) {
        this.reportModel = PrincipalVariationModel.collectStatics(this.reportTitle, searchResults);
        return this;
    }
}
