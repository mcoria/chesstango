package net.chesstango.tools.search.reports.summary;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public class SummaryDiffReportModel {

    String suiteName;
    int elements;
    SummaryModel baseLineSearchSummary;
    List<SummaryModel> searchSummaryList;
    List<SearchSummaryDiff> searchSummaryDiffs;

    public record SearchSummaryDiff(int durationPercentage,
                                    boolean sameSearches,
                                    int evaluationCoincidencePercentage,
                                    int visitedRNodesPercentage,
                                    int visitedQNodesPercentage,
                                    int visitedNodesPercentage,
                                    int evaluatedGamesPercentage,
                                    int executedMovesPercentage
    ) {
    }

    public static SummaryDiffReportModel createModel(String suiteName, SummaryModel baseLineSearchSummary, List<SummaryModel> searchSummaryList) {
        SummaryDiffReportModel reportModel = new SummaryDiffReportModel();

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

    private static SearchSummaryDiff calculateDiff(SummaryModel baseLineSearchSummary, SummaryModel searchSummary) {
        int durationPercentage = (int) ((searchSummary.duration * 100) / baseLineSearchSummary.duration);
        boolean sameSearches = searchSummary.searches == baseLineSearchSummary.searches;
        int visitedRNodesPercentage = (int) ((searchSummary.visitedRNodesTotal * 100) / baseLineSearchSummary.visitedRNodesTotal);
        int visitedQNodesPercentage = baseLineSearchSummary.visitedQNodesTotal != 0 ? (int) ((searchSummary.visitedQNodesTotal * 100) / baseLineSearchSummary.visitedQNodesTotal) : 0;
        int visitedNodesPercentage = (int) ((searchSummary.visitedNodesTotal * 100) / baseLineSearchSummary.visitedNodesTotal);
        int evaluatedGamesPercentage = (int) ((searchSummary.evaluationCounterTotal * 100) / baseLineSearchSummary.evaluationCounterTotal);
        int executedMovesPercentage = (int) ((searchSummary.executedMovesTotal * 100) / baseLineSearchSummary.executedMovesTotal);

        int evaluationCoincidences = 0;
        List<SummaryModel.SearchSummaryModeDetail> baseLineSummaryModeDetailListModeDetail = baseLineSearchSummary.searchDetailList;
        List<SummaryModel.SearchSummaryModeDetail> summaryModeDetailListModeDetail = searchSummary.searchDetailList;
        int baseLineSearches = baseLineSummaryModeDetailListModeDetail.size();
        int searches = summaryModeDetailListModeDetail.size();
        for (int i = 0; i < Math.min(baseLineSearches,searches) ; i++) {
            SummaryModel.SearchSummaryModeDetail baseMoveDetail = baseLineSummaryModeDetailListModeDetail.get(i);
            SummaryModel.SearchSummaryModeDetail moveDetail = summaryModeDetailListModeDetail.get(i);

            if(baseMoveDetail.evaluation == moveDetail.evaluation){
                evaluationCoincidences++;
            }
        }

        int evaluationCoincidencePercentage = (evaluationCoincidences * 100) / baseLineSearches;

        return new SearchSummaryDiff(durationPercentage, sameSearches, evaluationCoincidencePercentage, visitedRNodesPercentage, visitedQNodesPercentage, visitedNodesPercentage, evaluatedGamesPercentage, executedMovesPercentage);
    }
}


