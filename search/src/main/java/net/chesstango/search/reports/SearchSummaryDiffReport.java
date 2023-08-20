package net.chesstango.search.reports;

import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.PrintStream;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Mauricio Coria
 */
public class SearchSummaryDiffReport {

    @Setter
    @Accessors(chain = true)
    private SearchSummaryDiffReportModel reportModel;
    private PrintStream out;

    public SearchSummaryDiffReport printReport(PrintStream output) {
        out = output;
        print();
        return this;
    }

    private static final String lineaSuperiorFmt =  "_____________________";
    private static final String lineaInferiorFmt = "---------------------";
    private static final String durationFmt = "| %11d (%3d%%) ";

    private void print() {
        SearchSummaryModel baseLineSearchSummary = reportModel.baseLineSearchSummary;
        List<SearchSummaryModel> searchSummaryList = reportModel.searchSummaryList;
        List<SearchSummaryDiffReportModel.SearchSummaryDiff> searchSummaryDiffs = reportModel.searchSummaryDiffs;

        out.printf("Suite: %s\n", reportModel.suiteName);

        // Marco superior de la tabla
        out.printf(" __________");
        out.printf(lineaSuperiorFmt);
        IntStream.range(0, reportModel.elements).forEach(i -> out.printf(lineaSuperiorFmt));
        out.printf("\n");

        // Nombre de las columnas
        out.printf("| Metric   ");
        out.printf("|  %s  ", baseLineSearchSummary.sessionid);
        searchSummaryList.forEach(summary -> out.printf("|  %s  ", summary.sessionid));
        out.printf("|\n");

        // Nombre de las columnas
        out.printf("| Duration ");
        out.printf(durationFmt, baseLineSearchSummary.duration, 100);
        IntStream.range(0, reportModel.elements).forEach(i -> out.printf(durationFmt, searchSummaryList.get(i).duration, searchSummaryDiffs.get(i).durationPercentage()));
        out.printf("|\n");

        out.printf(" ----------");
        out.printf(lineaInferiorFmt);
        IntStream.range(0, reportModel.elements).forEach(i -> out.printf(lineaInferiorFmt));
        out.printf("\n\n");
    }


    public SearchSummaryDiffReport withSummaryDiffReportModel(SearchSummaryDiffReportModel reportModel) {
        this.reportModel = reportModel;
        return this;
    }

}
