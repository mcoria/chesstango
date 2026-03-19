package net.chesstango.reports.search.iteration;

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
public class IterationModel implements Model<List<SearchResult>> {
    public String searchGroupName;

    public int searches;
    public int maxIteration;

    public static class IterationModelDetail {
        String id;

        String move;
        int[] evaluations;
        int maxIteration;
        int minEvaluation;
        int maxEvaluation;
        int evaluationWidth;
    }

    public List<IterationModelDetail> iterationModelDetails;

    @Override
    public IterationModel collectStatistics(String searchGroupName, List<SearchResult> searchResults) {
        this.searchGroupName = searchGroupName;

        this.load(searchResults);

        return this;
    }

    private void load(List<SearchResult> searchResults) {
        this.searches = searchResults.size();

        this.iterationModelDetails = new LinkedList<>();

        searchResults.forEach(this::loadModelDetail);
    }

    private void loadModelDetail(SearchResult searchResult) {
        IterationModelDetail boardModelModelDetail = new IterationModelDetail();

        Move bestMove = searchResult.getBestMove();
        boardModelModelDetail.id = searchResult.getId();
        boardModelModelDetail.move = bestMove != null ? SimpleMoveEncoder.INSTANCE.encode(bestMove) : "";

        boardModelModelDetail.evaluations = searchResult.getSearchResultByDepths().stream().map(SearchResultByDepth::getBestEvaluation).mapToInt(Integer::intValue).toArray();
        boardModelModelDetail.maxIteration = boardModelModelDetail.evaluations.length;
        boardModelModelDetail.minEvaluation = searchResult.getSearchResultByDepths().stream().map(SearchResultByDepth::getBestEvaluation).mapToInt(Integer::intValue).min().orElse(0);
        boardModelModelDetail.maxEvaluation = searchResult.getSearchResultByDepths().stream().map(SearchResultByDepth::getBestEvaluation).mapToInt(Integer::intValue).max().orElse(0);
        boardModelModelDetail.evaluationWidth = boardModelModelDetail.maxEvaluation - boardModelModelDetail.minEvaluation;

        if(this.maxIteration < boardModelModelDetail.maxIteration) {
            this.maxIteration = boardModelModelDetail.maxIteration;
        }

        this.iterationModelDetails.add(boardModelModelDetail);
    }
}
