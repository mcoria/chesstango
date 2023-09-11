package net.chesstango.search.reports;

import net.chesstango.board.moves.Move;
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

        Move bestMove = searchMoveResult.getBestMove();
        reportModelDetail.id = searchMoveResult.getEpdID();
        reportModelDetail.move = String.format("%s%s", bestMove.getFrom().getSquare(), bestMove.getTo().getSquare());
        reportModelDetail.evaluation = searchMoveResult.getEvaluation();
        reportModelDetail.principalVariation = getPrincipalVariation(searchMoveResult.getPrincipalVariation());

        moveDetails.add(reportModelDetail);
    }



    private static String getPrincipalVariation(List<Move> principalVariation) {
        if (principalVariation == null) {
            return "-";
        } else {
            StringBuilder sb = new StringBuilder();
            for (Move move : principalVariation) {
                sb.append(String.format("%s ", String.format("%s%s", move.getFrom().getSquare(), move.getTo().getSquare())));
            }
            return sb.toString();
        }
    }
}
