package net.chesstango.search.reports;

import net.chesstango.search.EpdSearchResult;
import net.chesstango.search.SearchMoveResult;

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
    public List<String> failedEntries;

    public long duration;

    public static EpdSearchReportModel collectStatistics(String reportTitle, List<EpdSearchResult> edpEntries) {
        EpdSearchReportModel reportModel = new EpdSearchReportModel();

        List<SearchMoveResult> searchMoveResults = edpEntries.stream().map(EpdSearchResult::searchResult).toList();

        reportModel.reportTitle = reportTitle;

        reportModel.searches = edpEntries.size();

        reportModel.success = (int) edpEntries.stream().filter(EpdSearchResult::isSearchSuccess).count();

        reportModel.successRate = ((100 * reportModel.success) / reportModel.searches);

        reportModel.duration = searchMoveResults.stream().mapToLong(SearchMoveResult::getTimeSearching).sum();

        reportModel.failedEntries = new ArrayList<>();

        edpEntries.stream()
                .filter(edpEntry -> !edpEntry.isSearchSuccess())
                .forEach(edpEntry ->
                        reportModel.failedEntries.add(
                                String.format("Fail [%s] - best move found %s",
                                        edpEntry.getText(),
                                        edpEntry.bestMoveFoundStr()
                                )
                        ));

        return reportModel;
    }
}
