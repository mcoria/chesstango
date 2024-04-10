package net.chesstango.search.reports.pv;

import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.search.PrincipalVariation;
import net.chesstango.search.SearchMoveResult;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class PrincipalVariationReportModel {
    public String reportTitle;

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


    public static PrincipalVariationReportModel collectStatics(String reportTitle, List<SearchMoveResult> searchMoveResults) {
        PrincipalVariationReportModel principalVariationReportModel = new PrincipalVariationReportModel();

        principalVariationReportModel.reportTitle = reportTitle;

        principalVariationReportModel.load(searchMoveResults);

        principalVariationReportModel.pvAccuracyAvgPercentageTotal = principalVariationReportModel.moveDetails.stream().mapToInt(reportModelDetail -> reportModelDetail.pvAccuracyPercentage).sum() / principalVariationReportModel.moveDetails.size();

        return principalVariationReportModel;
    }

    private void load(List<SearchMoveResult> searchMoveResults) {
        moveDetails = new LinkedList<>();

        searchMoveResults.forEach(this::loadModelDetail);
    }

    private void loadModelDetail(SearchMoveResult searchMoveResult) {
        PrincipalVariationReportModelDetail reportModelDetail = new PrincipalVariationReportModelDetail();

        SimpleMoveEncoder simpleMoveEncoder = new SimpleMoveEncoder();

        Move bestMove = searchMoveResult.getBestMove();
        reportModelDetail.id = searchMoveResult.getEpdID();
        reportModelDetail.move = simpleMoveEncoder.encode(bestMove);
        reportModelDetail.evaluation = searchMoveResult.getBestEvaluation();
        reportModelDetail.principalVariation = String.format("%s %s", simpleMoveEncoder.encodeMoves(searchMoveResult.getPrincipalVariation().stream().map(PrincipalVariation::move).toList()), searchMoveResult.isPvComplete() ? "" : "truncated");
        reportModelDetail.pvAccuracyPercentage = (100 * searchMoveResult.getSearchByDepthPvCompleteCounter() / searchMoveResult.getSearchByDepthCounter());

        moveDetails.add(reportModelDetail);
    }

}
