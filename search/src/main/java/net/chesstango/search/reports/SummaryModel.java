package net.chesstango.search.reports;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class SummaryModel {

    @JsonProperty("sessionid")
    String sessionid;

    @JsonProperty("duration")
    long duration;

    @JsonProperty("searches")
    int searches;

    @JsonProperty("success")
    int success;

    @JsonProperty("successRate")
    int successRate;

    @JsonProperty("maxSearchRLevel")
    int maxSearchRLevel;

    @JsonProperty("maxSearchQLevel")
    int maxSearchQLevel;

    @JsonProperty("visitedRNodesTotal")
    long visitedRNodesTotal;

    @JsonProperty("visitedQNodesTotal")
    long visitedQNodesTotal;

    @JsonProperty("visitedNodesTotal")
    long visitedNodesTotal;

    @JsonProperty("cutoffPercentageTotal")
    int cutoffPercentageTotal;

    @JsonProperty("evaluatedGamesCounterTotal")
    long evaluatedGamesCounterTotal;

    @JsonProperty("executedMovesTotal")
    long executedMovesTotal;


    @JsonProperty("searchDetail")
    List<SearchSummaryModeDetail> searchDetailList = new LinkedList<>();

    public static class SearchSummaryModeDetail {

        @JsonProperty("id")
        public String id;

        @JsonProperty("move")
        public String move;
        @JsonProperty("pv")
        public String pv;

        @JsonProperty("evaluation")
        public int evaluation;

    }


    public static SummaryModel collectStatics(String sessionId,
                                              EpdSearchReportModel epdSearchReportModel,
                                              NodesReportModel nodesReportModel,
                                              EvaluationReportModel evaluationReportModel) {
        SummaryModel model = new SummaryModel();

        model.sessionid = sessionId;
        model.duration = epdSearchReportModel.duration;
        model.searches = epdSearchReportModel.searches;
        model.success = epdSearchReportModel.success;
        model.successRate = epdSearchReportModel.successRate;

        model.maxSearchRLevel = nodesReportModel.maxSearchRLevel;
        model.maxSearchQLevel = nodesReportModel.maxSearchQLevel;

        model.visitedRNodesTotal = nodesReportModel.visitedRNodesTotal;
        model.visitedQNodesTotal = nodesReportModel.visitedQNodesTotal;
        model.visitedNodesTotal = nodesReportModel.visitedNodesTotal;
        model.cutoffPercentageTotal = nodesReportModel.cutoffPercentageTotal;
        model.evaluatedGamesCounterTotal = evaluationReportModel.evaluatedGamesCounterTotal;
        model.executedMovesTotal = nodesReportModel.executedMovesTotal;

        nodesReportModel.moveDetails.forEach(searchReportModelDetail -> {
            SearchSummaryModeDetail searchSummaryModeDetail = new SearchSummaryModeDetail();
            searchSummaryModeDetail.id = searchReportModelDetail.id;
            searchSummaryModeDetail.move = searchReportModelDetail.move;
            searchSummaryModeDetail.pv = searchReportModelDetail.principalVariation;
            searchSummaryModeDetail.evaluation = searchReportModelDetail.evaluation;
            model.searchDetailList.add(searchSummaryModeDetail);
        });

        return model;
    }
}


