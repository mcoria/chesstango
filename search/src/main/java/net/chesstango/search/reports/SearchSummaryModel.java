package net.chesstango.search.reports;


import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Mauricio Coria
 */
public class SearchSummaryModel {

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

    @JsonProperty("evaluatedGamesCounterTotal")
    long evaluatedGamesCounterTotal;

    @JsonProperty("executedMovesTotal")
    long executedMovesTotal;

    public static SearchSummaryModel collectStatics(String sessionId, EpdSearchReportModel epdSearchReportModel, SearchesReportModel searchesReportModel) {
        SearchSummaryModel model = new SearchSummaryModel();

        model.sessionid = sessionId;
        model.duration =  epdSearchReportModel.duration;
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

        return model;
    }
}


