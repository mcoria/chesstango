package net.chesstango.tools.search.reports.epd;

import net.chesstango.tools.search.EpdSearchResult;
import net.chesstango.search.SearchResult;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class EpdSearchReportModel {
    public String reportTitle;

    public int searches;
    public int success;
    public int successRate;
    public int depthAccuracyPct;
    public List<String> failedEntries;

    public long duration;

    public static EpdSearchReportModel collectStatistics(String reportTitle, List<EpdSearchResult> epdEntries) {
        EpdSearchReportModel reportModel = new EpdSearchReportModel();

        List<SearchResult> searchResults = epdEntries.stream().map(EpdSearchResult::getSearchResult).toList();

        reportModel.reportTitle = reportTitle;

        reportModel.searches = epdEntries.size();

        reportModel.success = (int) epdEntries.stream().filter(EpdSearchResult::isSearchSuccess).count();

        reportModel.depthAccuracyPct = (int) epdEntries.stream().mapToInt(EpdSearchResult::getDepthAccuracyPct).average().orElse(0);

        reportModel.successRate = ((100 * reportModel.success) / reportModel.searches);

        reportModel.duration = searchResults.stream().mapToLong(SearchResult::getTimeSearching).sum();

        reportModel.failedEntries = new ArrayList<>();

        epdEntries.stream()
                .filter(edpEntry -> !edpEntry.isSearchSuccess())
                .forEach(edpEntry ->
                        reportModel.failedEntries.add(
                                String.format("Fail [%s] - best move found %s",
                                        edpEntry.getText(),
                                        edpEntry.getBestMoveFound()
                                )
                        ));

        return reportModel;
    }
}
