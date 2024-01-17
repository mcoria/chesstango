package net.chesstango.search.reports;

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
        reportModelDetail.evaluation = searchMoveResult.getEvaluation();
        reportModelDetail.principalVariation = getPrincipalVariation(searchMoveResult.getPrincipalVariation());

        moveDetails.add(reportModelDetail);
    }


    private static String getPrincipalVariation(List<Move> principalVariation) {
        SimpleMoveEncoder simpleMoveEncoder = new SimpleMoveEncoder();
        if (principalVariation == null) {
            return "-";
        } else {
            StringBuilder sb = new StringBuilder();
            for (Move move : principalVariation) {
                sb.append(simpleMoveEncoder.encode(move));
                sb.append(" ");
            }
            return sb.toString();
        }
    }
}
