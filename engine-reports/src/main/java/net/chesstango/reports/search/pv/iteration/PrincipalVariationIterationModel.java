package net.chesstango.reports.search.pv.iteration;

import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.reports.Model;
import net.chesstango.search.SearchResult;
import net.chesstango.search.SearchResultByDepth;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class PrincipalVariationIterationModel implements Model<List<SearchResult>> {
    public String searchGroupName;

    public int searches;
    public int maxIteration;
    public int pvCompletePercentage;

    public List<PrincipalVariationIterationModelDetail> modelDetails;

    public static class PrincipalVariationIterationModelDetail {
        String id;

        String move;

        int maxIteration;

        boolean[] pvComplete;

        int pvCompletePercentage;
    }

    @Override
    public PrincipalVariationIterationModel collectStatistics(String searchGroupName, List<SearchResult> searchResults) {
        this.searchGroupName = searchGroupName;

        this.load(searchResults);

        return this;
    }

    private void load(List<SearchResult> searchResults) {
        modelDetails = new LinkedList<>();

        searchResults.forEach(this::loadModelDetail);

        this.searches = modelDetails.size();
        this.maxIteration = modelDetails.stream().mapToInt(detail -> detail.maxIteration).max().orElse(0);
        this.pvCompletePercentage = (int) modelDetails.stream().mapToInt(detail -> detail.pvCompletePercentage).average().orElse(0);
    }

    private void loadModelDetail(SearchResult searchResult) {
        PrincipalVariationIterationModelDetail modelDetail = new PrincipalVariationIterationModelDetail();

        Move bestMove = searchResult.getBestMove();
        modelDetail.id = searchResult.getId();
        modelDetail.move = SimpleMoveEncoder.INSTANCE.encode(bestMove);
        modelDetail.maxIteration = searchResult.getMaxSearchDepth();

        List<Boolean> completeList = searchResult.getSearchResultByDepths()
                .stream()
                .map(SearchResultByDepth::isPvComplete)
                .toList();
        modelDetail.pvComplete = new boolean[completeList.size()];
        for (int i = 0; i < completeList.size(); i++) {
            modelDetail.pvComplete[i] = completeList.get(i);
        }

        modelDetail.pvCompletePercentage = (int) completeList.stream().mapToInt(b -> b ? 100 : 0).average().orElse(0);

        modelDetails.add(modelDetail);
    }
}
