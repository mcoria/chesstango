package net.chesstango.reports.search.pv;

import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.reports.Model;
import net.chesstango.search.PrincipalVariation;
import net.chesstango.search.SearchResult;
import net.chesstango.search.SearchResultByDepth;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class PrincipalVariationModel implements Model<List<SearchResult>> {
    public String searchGroupName;

    public int searches;

    /**
     * Promedio de promedio
     */
    public int pvCompletePercentage;

    public List<PrincipalVariationReportModelDetail> moveDetails;

    public static class PrincipalVariationReportModelDetail {
        public String id;

        public String move;

        public int evaluation;

        public String principalVariation;

        public boolean pvComplete;

        public int maxDepth;

        public int selDepth;
    }

    @Override
    public PrincipalVariationModel collectStatistics(String searchGroupName, List<SearchResult> searchResults) {
        this.searchGroupName = searchGroupName;

        this.load(searchResults);

        return this;
    }

    private void load(List<SearchResult> searchResults) {
        moveDetails = new LinkedList<>();

        searchResults.forEach(this::loadModelDetail);

        long pvCompletes = this.moveDetails.stream().filter(reportModelDetail -> reportModelDetail.pvComplete).count();

        this.pvCompletePercentage = !searchResults.isEmpty() ? (int) (pvCompletes * 100 / searchResults.size()) : 0;
    }

    private void loadModelDetail(SearchResult searchResult) {
        PrincipalVariationReportModelDetail reportModelDetail = new PrincipalVariationReportModelDetail();

        List<SearchResultByDepth> searchResultByDepths = searchResult.getSearchResultByDepths();

        SearchResultByDepth lastSearchResultByDepth = searchResultByDepths.getLast();

        Move bestMove = searchResult.getBestMove();
        reportModelDetail.id = searchResult.getId();
        reportModelDetail.move = SimpleMoveEncoder.INSTANCE.encode(bestMove);
        reportModelDetail.evaluation = searchResult.getBestEvaluation();

        List<Move> pvMoves = searchResult.getPrincipalVariation().stream().map(PrincipalVariation::move).toList();
        reportModelDetail.principalVariation = String.format("%s%s", SimpleMoveEncoder.INSTANCE.encode(pvMoves), searchResult.isPvComplete() ? "" : " truncated");

        reportModelDetail.pvComplete = searchResult.isPvComplete();

        reportModelDetail.maxDepth = lastSearchResultByDepth.getDepth();

        reportModelDetail.selDepth = pvMoves.size() >= reportModelDetail.maxDepth ? pvMoves.size() - reportModelDetail.maxDepth : 0;

        moveDetails.add(reportModelDetail);

        this.searches++;
    }
}
