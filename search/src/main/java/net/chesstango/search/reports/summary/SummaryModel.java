package net.chesstango.search.reports.summary;


import com.fasterxml.jackson.annotation.JsonProperty;
import net.chesstango.search.EpdSearchResult;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.reports.EpdSearchReportModel;
import net.chesstango.search.reports.evaluation.EvaluationReportModel;
import net.chesstango.search.reports.nodes.NodesReportModel;
import net.chesstango.search.reports.pv.PrincipalVariationReportModel;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

    @JsonProperty("executedMovesTotal")
    long executedMovesTotal;

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

    @JsonProperty("evaluationCounterTotal")
    long evaluationCounterTotal;

    @JsonProperty("evaluationCollisionPercentageTotal")
    int evaluationCollisionPercentageTotal;

    @JsonProperty("pvAccuracyAvgPercentage")
    int pvAccuracyAvgPercentageTotal;

    @JsonProperty("searchDetail")
    List<SearchSummaryModeDetail> searchDetailList = new LinkedList<>();

    public static class SearchSummaryModeDetail {
        @JsonProperty("id")
        public String id;

        @JsonProperty("move")
        public String move;

        @JsonProperty("pv")
        public String pv;

        @JsonProperty("pvAccuracyPercentage")
        public int pvAccuracyPercentage;

        @JsonProperty("evaluation")
        public int evaluation;
    }


    public static SummaryModel collectStatics(String sessionId,
                                              List<EpdSearchResult> epdSearchResults,
                                              EpdSearchReportModel epdSearchReportModel,
                                              NodesReportModel nodesReportModel,
                                              EvaluationReportModel evaluationReportModel,
                                              PrincipalVariationReportModel principalVariationReportModel) {

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
        model.executedMovesTotal = nodesReportModel.executedMovesTotal;
        model.cutoffPercentageTotal = nodesReportModel.cutoffPercentageTotal;
        model.evaluationCounterTotal = evaluationReportModel.evaluationCounterTotal;
        model.evaluationCollisionPercentageTotal = evaluationReportModel.evaluationCollisionPercentageTotal;
        model.pvAccuracyAvgPercentageTotal = principalVariationReportModel.pvAccuracyAvgPercentageTotal;

        Map<String, PrincipalVariationReportModel.PrincipalVariationReportModelDetail> pvMap = new HashMap<>();
        principalVariationReportModel.moveDetails.forEach(pvMoveDetail -> pvMap.put(pvMoveDetail.id, pvMoveDetail));

        epdSearchResults.stream().map(epdSearchResult -> {
            SearchSummaryModeDetail searchSummaryModeDetail = new SearchSummaryModeDetail();
            SearchMoveResult searchMoveResult = epdSearchResult.searchResult();
            PrincipalVariationReportModel.PrincipalVariationReportModelDetail pvDetail = pvMap.get(epdSearchResult.epdEntry().id);

            searchSummaryModeDetail.id = epdSearchResult.epdEntry().id;
            searchSummaryModeDetail.move = epdSearchResult.bestMoveFoundStr();
            searchSummaryModeDetail.pv = pvDetail.principalVariation;
            searchSummaryModeDetail.pvAccuracyPercentage = pvDetail.pvAccuracyPercentage;
            searchSummaryModeDetail.evaluation = searchMoveResult.getBestEvaluation();
            return searchSummaryModeDetail;
        }).forEach(model.searchDetailList::add);


        return model;
    }
}


