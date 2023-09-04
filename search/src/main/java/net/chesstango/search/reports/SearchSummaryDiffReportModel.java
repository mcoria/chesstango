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

    public record SearchSummaryDiff(int durationPercentage,
                                    boolean sameSearches,
                                    int visitedRNodesPercentage,
                                    int visitedQNodesPercentage,
                                    int visitedNodesPercentage,
                                    int evaluatedGamesPercentage,
                                    int executedMovesPercentage,
                                    int evaluationCoincidencePercentage) {
    }

    public static SearchSummaryDiffReportModel createModel(String suiteName, SearchSummaryModel baseLineSearchSummary, List<SearchSummaryModel> searchSummaryList) {
        SearchSummaryDiffReportModel reportModel = new SearchSummaryDiffReportModel();

        reportModel.suiteName = suiteName;
        reportModel.elements = searchSummaryList.size();
        reportModel.baseLineSearchSummary = baseLineSearchSummary;
        reportModel.searchSummaryList = searchSummaryList;
        reportModel.searchSummaryDiffs = searchSummaryList
                .stream()
                .map(searchSummary -> calculateDiff(baseLineSearchSummary, searchSummary))
                .toList();


        return reportModel;
    }

    private static SearchSummaryDiff calculateDiff(SearchSummaryModel baseLineSearchSummary, SearchSummaryModel searchSummary) {
        int durationPercentage = (int) ((searchSummary.duration * 100) / baseLineSearchSummary.duration);
        boolean sameSearches = searchSummary.searches == baseLineSearchSummary.searches;
        int visitedRNodesPercentage = (int) ((searchSummary.visitedRNodesTotal * 100) / baseLineSearchSummary.visitedRNodesTotal);
        int visitedQNodesPercentage = baseLineSearchSummary.visitedQNodesTotal != 0 ? (int) ((searchSummary.visitedQNodesTotal * 100) / baseLineSearchSummary.visitedQNodesTotal) : 0;
        int visitedNodesPercentage = (int) ((searchSummary.visitedNodesTotal * 100) / baseLineSearchSummary.visitedNodesTotal);
        int evaluatedGamesPercentage = (int) ((searchSummary.evaluatedGamesCounterTotal * 100) / baseLineSearchSummary.evaluatedGamesCounterTotal);
        int executedMovesPercentage = (int) ((searchSummary.executedMovesTotal * 100) / baseLineSearchSummary.executedMovesTotal);

        int evaluationCoincidences = 0;
        List<SearchSummaryModel.SearchSummaryModeDetail> baseLineSummaryModeDetailListModeDetail = baseLineSearchSummary.summaryModeDetailList;
        List<SearchSummaryModel.SearchSummaryModeDetail> summaryModeDetailListModeDetail = searchSummary.summaryModeDetailList;
        int baseLineSearches = baseLineSummaryModeDetailListModeDetail.size();
        int searches = summaryModeDetailListModeDetail.size();
        for (int i = 0; i < Math.min(baseLineSearches,searches) ; i++) {
            SearchSummaryModel.SearchSummaryModeDetail baseMoveDetail = baseLineSummaryModeDetailListModeDetail.get(i);
            SearchSummaryModel.SearchSummaryModeDetail moveDetail = summaryModeDetailListModeDetail.get(i);

            if(baseMoveDetail.evaluation == moveDetail.evaluation){
                evaluationCoincidences++;
            }
        }

        int evaluationCoincidencePercentage = (evaluationCoincidences * 100) / baseLineSearches;



        return new SearchSummaryDiff(durationPercentage, sameSearches, visitedRNodesPercentage, visitedQNodesPercentage, visitedNodesPercentage, evaluatedGamesPercentage, executedMovesPercentage, evaluationCoincidencePercentage);
    }
}


