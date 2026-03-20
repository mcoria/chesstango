package net.chesstango.reports.search.iteration;

import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.reports.Model;
import net.chesstango.search.SearchResult;
import net.chesstango.search.SearchResultByDepth;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class IterationEvaluationModel implements Model<List<SearchResult>> {
    public String searchGroupName;

    public int searches;
    public int maxIteration;
    public int evaluationStdDevAvg;

    public static class IterationModelDetail {
        String id;

        String move;
        int[] evaluations;
        int maxIteration;
        int minEvaluation;
        int maxEvaluation;
        int evaluationStdDev;
    }

    public List<IterationModelDetail> iterationModelDetails;

    @Override
    public IterationEvaluationModel collectStatistics(String searchGroupName, List<SearchResult> searchResults) {
        this.searchGroupName = searchGroupName;

        this.load(searchResults);

        this.evaluationStdDevAvg = (int) iterationModelDetails.stream().mapToInt(detail -> detail.evaluationStdDev).average().orElse(0);

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

        int[] evalArray = searchResult.getSearchResultByDepths().stream().map(SearchResultByDepth::getBestEvaluation).mapToInt(Integer::intValue).toArray();

        IntSummaryStatistics evalSummaryStatistics = Arrays.stream(evalArray).summaryStatistics();

        boardModelModelDetail.minEvaluation = evalSummaryStatistics.getMin();
        boardModelModelDetail.maxEvaluation = evalSummaryStatistics.getMax();

        double mean = evalSummaryStatistics.getAverage();
        long count = evalSummaryStatistics.getCount();

        // 2. Calculate the sum of squared differences from the mean
        double sumOfSquaredDifferences = Arrays.stream(evalArray)
                .mapToDouble(x -> Math.pow(x - mean, 2)) // Use mapToDouble for precision
                .sum();

        // 3. Calculate the variance and then the standard deviation
        // Use 'count' for population standard deviation, or 'count - 1' for sample
        double variance = sumOfSquaredDifferences / count;
        double standardDeviation = Math.sqrt(variance);
        boardModelModelDetail.evaluationStdDev = (int) standardDeviation;

        if (this.maxIteration < boardModelModelDetail.maxIteration) {
            this.maxIteration = boardModelModelDetail.maxIteration;
        }

        this.iterationModelDetails.add(boardModelModelDetail);
    }
}
