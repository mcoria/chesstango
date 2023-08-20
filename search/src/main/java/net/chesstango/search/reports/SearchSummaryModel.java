package net.chesstango.search.reports;


import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Mauricio Coria
 */
public class SearchSummaryModel {

    @JsonProperty("searches")
    long searches;

    @JsonProperty("success")
    long success;

    @JsonProperty("successRate")
    int successRate;

    @JsonProperty("visitedNodesTotal")
    long visitedNodesTotal;

    @JsonProperty("evaluatedGamesCounterTotal")
    long evaluatedGamesCounterTotal;

    @JsonProperty("maxSearchRLevel")
    int maxSearchRLevel;

    @JsonProperty("maxSearchQLevel")
    int maxSearchQLevel;

    @JsonProperty("visitedRNodesTotal")
    long visitedRNodesTotal;

    @JsonProperty("visitedQNodesTotal")
    long visitedQNodesTotal;

    public static SearchSummaryModel collectStatics(EdpSearchReportModel edpSearchReportModel, SearchesReportModel searchesReportModel) {
        SearchSummaryModel model = new SearchSummaryModel();

        model.searches = edpSearchReportModel.searches;
        model.success = edpSearchReportModel.success;
        model.successRate = edpSearchReportModel.successRate;

        model.visitedNodesTotal = searchesReportModel.visitedNodesTotal;
        model.evaluatedGamesCounterTotal = searchesReportModel.evaluatedGamesCounterTotal;

        model.maxSearchRLevel = searchesReportModel.maxSearchRLevel;
        model.maxSearchQLevel = searchesReportModel.maxSearchQLevel;

        model.visitedRNodesTotal = searchesReportModel.visitedRNodesTotal;
        model.visitedQNodesTotal = searchesReportModel.visitedQNodesTotal;

        return model;
    }
}


