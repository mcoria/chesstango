package net.chesstango.search.reports;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.board.representations.EPDEntry;

import java.io.PrintStream;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class EdpSearchReport {

    private PrintStream out;

    @Setter
    @Accessors(chain = true)
    private EdpSearchReportModel reportModel;

    public EdpSearchReport printReport(PrintStream output) {
        out = output;
        print();
        return this;
    }

    private void print() {
        out.printf("----------------------------------------------------------------------------\n\n");
        if (reportModel.failedEntries.isEmpty()) {
            out.println("\tall tests executed successfully !!!!");
        } else {
            for (String failedTest : reportModel.failedEntries) {
                out.printf("\t%s\n", failedTest);
            }
        }

        out.printf("Searches        : %d\n", reportModel.searches);
        out.printf("Success rate    : %d%% \n", reportModel.successRate);
        out.printf("Time taken      : %dms\n", reportModel.duration);
    }

    public EdpSearchReport withEdpEntries(List<EPDSearchResult> edpEntries) {
        this.reportModel = EdpSearchReportModel.collectStatics(edpEntries);
        return this;
    }

}
