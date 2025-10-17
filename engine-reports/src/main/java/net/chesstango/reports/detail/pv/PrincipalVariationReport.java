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
    private PrincipalVariationModel reportModel;

    @Setter
    @Accessors(chain = true)
    private String reportTitle = "PrincipalVariationReport";


    @Override
    public PrincipalVariationReport printReport(PrintStream output) {
        new PrincipalVariationPrinter(output, reportModel).printPrincipalVariation();
        return this;
    }


    public PrincipalVariationReport withMoveResults(List<SearchResult> searchResults) {
        this.reportModel = PrincipalVariationModel.collectStatics(this.reportTitle, searchResults);
        return this;
    }
}
