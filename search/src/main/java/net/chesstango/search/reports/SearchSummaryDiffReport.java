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

    private static final String lineaSuperiorFmt = "_____________________";
    private static final String lineaInferiorFmt = "---------------------";
    private static final String durationFmt = "| %9dms (%3d%%) ";
    private static final String searchesFmt = "| %s %16d ";
    private static final String successRateFmt = "| %17d%% ";
    private static final String successLevelFmt = "| %18d ";
    private static final String visitedNodesFmt = "| %11d (%3d%%) ";
    private static final String evaluatedGamesFmt = "| %11d (%3d%%) ";
    private static final String executedMovesFmt = "| %11d (%3d%%) ";
    private static final String evaluationCoincidencesFmt = "| %17d%% ";
    private static final String cutoffFmt = "| %17d%% ";

    private void print() {
        SearchSummaryModel baseLineSearchSummary = reportModel.baseLineSearchSummary;
        List<SearchSummaryModel> searchSummaryList = reportModel.searchSummaryList;
        List<SearchSummaryDiffReportModel.SearchSummaryDiff> searchSummaryDiffs = reportModel.searchSummaryDiffs;

        out.printf("Suite: %s\n", reportModel.suiteName);

        // Marco superior de la tabla
        out.printf(" ______________");
        out.printf(lineaSuperiorFmt);
        IntStream.range(0, reportModel.elements).forEach(i -> out.printf(lineaSuperiorFmt));
        out.printf("\n");

        // Nombre de las columnas
        out.printf("| Metric       ");
        out.printf("|  %s  ", baseLineSearchSummary.sessionid);
        searchSummaryList.forEach(summary -> out.printf("|  %s  ", summary.sessionid));
        out.printf("|\n");

        // Duration
        out.printf("| Duration     ");
        out.printf(durationFmt, baseLineSearchSummary.duration, 100);
        IntStream.range(0, reportModel.elements).forEach(i -> out.printf(durationFmt, searchSummaryList.get(i).duration, searchSummaryDiffs.get(i).durationPercentage()));
        out.printf("|\n");

        // Searches
        out.printf("| Searches     ");
        out.printf(searchesFmt, " ", baseLineSearchSummary.searches);
        IntStream.range(0, reportModel.elements).forEach(i -> out.printf(searchesFmt, searchSummaryDiffs.get(i).sameSearches() ? " " : "*", searchSummaryList.get(i).searches));
        out.printf("|\n");

        // Searches
        out.printf("| Success      ");
        out.printf(successRateFmt, baseLineSearchSummary.successRate);
        IntStream.range(0, reportModel.elements).forEach(i -> out.printf(successRateFmt, searchSummaryList.get(i).successRate));
        out.printf("|\n");

        out.printf("| Coincidences ");
        out.printf(evaluationCoincidencesFmt, 100);
        IntStream.range(0, reportModel.elements).forEach(i -> out.printf(evaluationCoincidencesFmt, searchSummaryDiffs.get(i).evaluationCoincidencePercentage()));
        out.printf("|\n");

        // RLevel
        out.printf("| Max RLevel   ");
        out.printf(successLevelFmt, baseLineSearchSummary.maxSearchRLevel);
        IntStream.range(0, reportModel.elements).forEach(i -> out.printf(successLevelFmt, searchSummaryList.get(i).maxSearchRLevel));
        out.printf("|\n");

        // QLevel
        out.printf("| Max QLevel   ");
        out.printf(successLevelFmt, baseLineSearchSummary.maxSearchQLevel);
        IntStream.range(0, reportModel.elements).forEach(i -> out.printf(successLevelFmt, searchSummaryList.get(i).maxSearchQLevel));
        out.printf("|\n");

        // RLevel
        out.printf("| Vis RNodes   ");
        out.printf(visitedNodesFmt, baseLineSearchSummary.visitedRNodesTotal, 100);
        IntStream.range(0, reportModel.elements).forEach(i -> out.printf(visitedNodesFmt, searchSummaryList.get(i).visitedRNodesTotal, searchSummaryDiffs.get(i).visitedRNodesPercentage()));
        out.printf("|\n");

        out.printf("| Vis QNodes   ");
        out.printf(visitedNodesFmt, baseLineSearchSummary.visitedQNodesTotal, 100);
        IntStream.range(0, reportModel.elements).forEach(i -> out.printf(visitedNodesFmt, searchSummaryList.get(i).visitedQNodesTotal, searchSummaryDiffs.get(i).visitedQNodesPercentage()));
        out.printf("|\n");

        out.printf("| Vis  Nodes   ");
        out.printf(visitedNodesFmt, baseLineSearchSummary.visitedNodesTotal, 100);
        IntStream.range(0, reportModel.elements).forEach(i -> out.printf(visitedNodesFmt, searchSummaryList.get(i).visitedNodesTotal, searchSummaryDiffs.get(i).visitedNodesPercentage()));
        out.printf("|\n");


        out.printf("| Cutoff       ");
        out.printf(cutoffFmt, baseLineSearchSummary.cutoffPercentageTotal);
        IntStream.range(0, reportModel.elements).forEach(i -> out.printf(cutoffFmt, searchSummaryList.get(i).cutoffPercentageTotal));
        out.printf("|\n");

        out.printf("| Eval Games   ");
        out.printf(evaluatedGamesFmt, baseLineSearchSummary.evaluatedGamesCounterTotal, 100);
        IntStream.range(0, reportModel.elements).forEach(i -> out.printf(evaluatedGamesFmt, searchSummaryList.get(i).evaluatedGamesCounterTotal, searchSummaryDiffs.get(i).evaluatedGamesPercentage()));
        out.printf("|\n");

        out.printf("| Exec Moves   ");
        out.printf(executedMovesFmt, baseLineSearchSummary.executedMovesTotal, 100);
        IntStream.range(0, reportModel.elements).forEach(i -> out.printf(executedMovesFmt, searchSummaryList.get(i).executedMovesTotal, searchSummaryDiffs.get(i).executedMovesPercentage()));
        out.printf("|\n");

        // Marco inferior de la tabla
        out.printf(" --------------");
        out.printf(lineaInferiorFmt);
        IntStream.range(0, reportModel.elements).forEach(i -> out.printf(lineaInferiorFmt));
        out.printf("\n\n");
    }


    public SearchSummaryDiffReport withSummaryDiffReportModel(SearchSummaryDiffReportModel reportModel) {
        this.reportModel = reportModel;
        return this;
    }

}
