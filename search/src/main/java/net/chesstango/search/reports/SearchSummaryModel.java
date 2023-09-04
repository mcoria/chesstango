package net.chesstango.search.reports;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class SearchSummaryModel {

    @JsonProperty("sessionid")
    String sessionid;

    @JsonProperty("duration")
    long duration;

    @JsonProperty("searches")
    long searches;

    @JsonProperty("success")
    long success;

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

    @JsonProperty("evaluatedGamesCounterTotal")
    long evaluatedGamesCounterTotal;

    @JsonProperty("executedMovesTotal")
    int executedMovesTotal;


    @JsonProperty("summaryModeDetailList")
    List<SearchSummaryModeDetail> summaryModeDetailList = new LinkedList<>();

    public static class SearchSummaryModeDetail {

        @JsonProperty("move")
        public String move;

        @JsonProperty("pv")
        public String pv;

        @JsonProperty("evaluation")
        public int evaluation;

    }


    public static SearchSummaryModel collectStatics(String sessionId, EpdSearchReportModel epdSearchReportModel, SearchesReportModel searchesReportModel) {
        SearchSummaryModel model = new SearchSummaryModel();

        model.sessionid = sessionId;
        model.duration = epdSearchReportModel.duration;
        model.searches = epdSearchReportModel.searches;
        model.success = epdSearchReportModel.success;
        model.successRate = epdSearchReportModel.successRate;

        model.maxSearchRLevel = searchesReportModel.maxSearchRLevel;
        model.maxSearchQLevel = searchesReportModel.maxSearchQLevel;

        model.visitedRNodesTotal = searchesReportModel.visitedRNodesTotal;
        model.visitedQNodesTotal = searchesReportModel.visitedQNodesTotal;
        model.visitedNodesTotal = searchesReportModel.visitedNodesTotal;
        model.evaluatedGamesCounterTotal = searchesReportModel.evaluatedGamesCounterTotal;
        model.executedMovesTotal = searchesReportModel.executedMovesTotal;

        searchesReportModel.moveDetails.forEach(searchReportModelDetail -> {
            SearchSummaryModeDetail searchSummaryModeDetail = new SearchSummaryModeDetail();
            searchSummaryModeDetail.move = searchReportModelDetail.move;
            searchSummaryModeDetail.pv = searchReportModelDetail.principalVariation;
            searchSummaryModeDetail.evaluation = searchReportModelDetail.evaluation;
            model.summaryModeDetailList.add(searchSummaryModeDetail);
        });

        return model;
    }
}


