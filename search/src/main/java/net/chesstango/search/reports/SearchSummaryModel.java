package net.chesstango.search.reports;


import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Mauricio Coria
 */
public class SearchSummaryModel {

    @JsonProperty("duration")
    private long duration;

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

    public static SearchSummaryModel collectStatics(EdpSearchReportModel edpSearchReportModel, SearchesReportModel searchesReportModel) {
        SearchSummaryModel model = new SearchSummaryModel();

        model.duration =  edpSearchReportModel.duration;
        model.searches = edpSearchReportModel.searches;
        model.success = edpSearchReportModel.success;
        model.successRate = edpSearchReportModel.successRate;

        model.maxSearchRLevel = searchesReportModel.maxSearchRLevel;
        model.maxSearchQLevel = searchesReportModel.maxSearchQLevel;

        model.visitedRNodesTotal = searchesReportModel.visitedRNodesTotal;
        model.visitedQNodesTotal = searchesReportModel.visitedQNodesTotal;
        model.visitedNodesTotal = searchesReportModel.visitedNodesTotal;
        model.evaluatedGamesCounterTotal = searchesReportModel.evaluatedGamesCounterTotal;

        return model;
    }
}


