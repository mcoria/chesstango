package net.chesstango.search.reports;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.reports.pv_ui.PrintPrincipalVariation;

import java.io.PrintStream;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class PrincipalVariationReport {

    @Setter
    @Accessors(chain = true)
    private String engineName = "Tango";

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
            new PrintPrincipalVariation(out, reportModel).printPrincipalVariation();
    }


    public PrincipalVariationReport withMoveResults(List<SearchMoveResult> searchMoveResults) {
        this.reportModel = PrincipalVariationReportModel.collectStatics(this.engineName, searchMoveResults);
        return this;
    }
}