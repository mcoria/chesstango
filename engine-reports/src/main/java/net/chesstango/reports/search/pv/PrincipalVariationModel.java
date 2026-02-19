package net.chesstango.reports.search.pv;

import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.reports.Model;
import net.chesstango.search.PrincipalVariation;
import net.chesstango.search.SearchResult;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class PrincipalVariationModel implements Model<List<SearchResult>> {
    public String searchGroupName;

    /**
     * Promedio de promedio
     */
    public int pvAccuracyAvgPercentageTotal;

    public List<PrincipalVariationReportModelDetail> moveDetails;


    public static class PrincipalVariationReportModelDetail {
        public String id;

        public String move;
        public String principalVariation;

        /**
         * Que porcentaje de PVs estan completos del total de busquedas por depth
         */
        public int pvAccuracyPercentage;

        public int evaluation;
    }

    @Override
    public PrincipalVariationModel collectStatistics(String reportTitle, List<SearchResult> searchResults) {
        this.searchGroupName = reportTitle;

        this.load(searchResults);

        this.pvAccuracyAvgPercentageTotal = this.moveDetails.stream().mapToInt(reportModelDetail -> reportModelDetail.pvAccuracyPercentage).sum() / this.moveDetails.size();

        return this;
    }

    private void load(List<SearchResult> searchResults) {
        moveDetails = new LinkedList<>();

        searchResults.forEach(this::loadModelDetail);
    }

    private void loadModelDetail(SearchResult searchResult) {
        PrincipalVariationReportModelDetail reportModelDetail = new PrincipalVariationReportModelDetail();

        SimpleMoveEncoder simpleMoveEncoder = new SimpleMoveEncoder();

        Move bestMove = searchResult.getBestMove();
        reportModelDetail.id = searchResult.getId();
        reportModelDetail.move = simpleMoveEncoder.encode(bestMove);
        reportModelDetail.evaluation = searchResult.getBestEvaluation();
        reportModelDetail.principalVariation = String.format("%s %s", simpleMoveEncoder.encode(searchResult.getPrincipalVariation().stream().map(PrincipalVariation::move).toList()), searchResult.isPvComplete() ? "" : "truncated");
        reportModelDetail.pvAccuracyPercentage = (100 * searchResult.getSearchByDepthPvCompleteCounter() / searchResult.getSearchesByDepthCounter());

        moveDetails.add(reportModelDetail);
    }

}
