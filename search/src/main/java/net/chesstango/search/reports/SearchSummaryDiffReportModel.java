package net.chesstango.search.reports;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public class SearchSummaryDiffReportModel {

    String suiteName;
    int elements;
    SearchSummaryModel baseLineSearchSummary;
    List<SearchSummaryModel> searchSummaryList;
    List<SearchSummaryDiff> searchSummaryDiffs;
    public record SearchSummaryDiff(int durationPercentage, boolean sameSearches, int visitedRNodesPercentage, int visitedQNodesPercentage){}

    public static SearchSummaryDiffReportModel createModel(String suiteName, SearchSummaryModel baseLineSearchSummary, List<SearchSummaryModel> searchSummaryList) {
        SearchSummaryDiffReportModel reportModel = new SearchSummaryDiffReportModel();

        reportModel.suiteName = suiteName;
        reportModel.elements = searchSummaryList.size();
        reportModel.baseLineSearchSummary = baseLineSearchSummary;
        reportModel.searchSummaryList = searchSummaryList;
        reportModel.searchSummaryDiffs = searchSummaryList
                .stream()
                .map(searchSummary-> calculateDiff(baseLineSearchSummary, searchSummary))
                .toList();


        return reportModel;
    }

    private static SearchSummaryDiff calculateDiff(SearchSummaryModel baseLineSearchSummary, SearchSummaryModel searchSummary) {
        int durationPercentage = (int) ((searchSummary.duration * 100) / baseLineSearchSummary.duration);
        boolean sameSearches = searchSummary.searches == baseLineSearchSummary.searches;
        int visitedRNodesPercentage = (int) ((searchSummary.visitedRNodesTotal * 100) / baseLineSearchSummary.visitedRNodesTotal);
        int visitedQNodesPercentage = (int) ((searchSummary.visitedQNodesTotal * 100) / baseLineSearchSummary.visitedQNodesTotal);
        return new SearchSummaryDiff(durationPercentage, sameSearches, visitedRNodesPercentage, visitedQNodesPercentage);
    }
}
