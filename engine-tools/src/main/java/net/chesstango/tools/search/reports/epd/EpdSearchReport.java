package net.chesstango.tools.search.reports.epd;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.tools.search.EpdSearchResult;

import java.io.PrintStream;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class EpdSearchReport {

    private PrintStream out;

    @Setter
    @Accessors(chain = true)
    private String reportTitle = "EpdSearchReport";

    @Setter
    @Accessors(chain = true)
    private EpdSearchReportModel reportModel;

    public EpdSearchReport printReport(PrintStream output) {
        out = output;
        print();
        return this;
    }

    private void print() {
        out.printf("--------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        out.printf("--------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        out.printf("EpdSearchReport: %s\n\n", reportModel.reportTitle);

        if (reportModel.failedEntries.isEmpty()) {
            out.println("\tall tests executed successfully !!!!");
        } else {
            for (String failedTest : reportModel.failedEntries) {
                out.printf("\t%s\n", failedTest);
            }
        }

        out.printf("Searches        : %d\n", reportModel.searches);
        out.printf("Success rate    : %d%%\n", reportModel.successRate);
        out.printf("Depth Accuracy  : %d%%\n", reportModel.depthAccuracyPct);
        out.printf("Time taken      : %dms\n", reportModel.duration);
    }

    public EpdSearchReport withEdpEntries(List<EpdSearchResult> edpEntries) {
        this.reportModel = EpdSearchReportModel.collectStatistics(reportTitle, edpEntries);
        return this;
    }

}
