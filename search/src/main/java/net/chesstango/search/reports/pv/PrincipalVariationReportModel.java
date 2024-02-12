package net.chesstango.search.reports.pv;

import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.search.SearchMoveResult;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class PrincipalVariationReportModel {
    public String reportTitle;

    public List<PrincipalVariationReportModelDetail> moveDetails;


    public static class PrincipalVariationReportModelDetail {
        public String id;

        public String move;
        public String principalVariation;
        public int searchByDepthPvPercentage;

        public int evaluation;
    }


    public static PrincipalVariationReportModel collectStatics(String reportTitle, List<SearchMoveResult> searchMoveResults) {
        PrincipalVariationReportModel principalVariationReportModel = new PrincipalVariationReportModel();

        principalVariationReportModel.reportTitle = reportTitle;

        principalVariationReportModel.load(searchMoveResults);

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
        reportModelDetail.principalVariation = String.format("%s %s", simpleMoveEncoder.encodeMoves(searchMoveResult.getPrincipalVariation()), searchMoveResult.isPvComplete() ? "" : "truncated");
        reportModelDetail.searchByDepthPvPercentage = (100 * searchMoveResult.getSearchByDepthPvCompleteCounter() / searchMoveResult.getSearchByDepthCounter());

        moveDetails.add(reportModelDetail);
    }

}
