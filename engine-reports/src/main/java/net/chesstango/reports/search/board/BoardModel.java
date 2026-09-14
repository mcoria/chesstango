package net.chesstango.reports.search.board;

import net.chesstango.board.moves.Move;
import net.chesstango.reports.Model;
import net.chesstango.search.SearchResult;
import net.chesstango.search.alphabeta.statistics.sorter.SorterStatistics;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class BoardModel implements Model<List<SearchResult>> {
    public String searchGroupName;

    public int searches;
    public long executedMovesTotal;
    public float exploredDepthAvg;
    public int failHighPercentageAvg;
    public long searchTimeTotal;

    public static class BoardModelDetail {
        String id;
        String move;
        long executedMoves;
        float exploredDepth;
        int failHighPercentage;
        long searchTime;
    }

    public List<BoardModelDetail> boardModelModelDetails;

    @Override
    public BoardModel collectStatistics(String searchGroupName, List<SearchResult> searchResults) {
        this.searchGroupName = searchGroupName;

        this.load(searchResults);

        return this;
    }

    private void load(List<SearchResult> searchResults) {
        this.searches = searchResults.size();

        this.boardModelModelDetails = new LinkedList<>();

        searchResults.forEach(this::loadModelDetail);

        this.exploredDepthAvg = (float) boardModelModelDetails
                .stream()
                .mapToDouble(detail -> detail.exploredDepth)
                .average()
                .orElse(0f);

        this.executedMovesTotal = searchResults
                .stream()
                .mapToLong(SearchResult::getExecutedMoves)
                .sum();

        this.searchTimeTotal = searchResults
                .stream()
                .mapToLong(SearchResult::getTimeSearching)
                .sum();

        long failHighFirstSum = searchResults
                .stream()
                .map(SearchResult::getSorterStatistics)
                .mapToLong(SorterStatistics::failHighFirstCounter)
                .sum();

        long failHighSum = searchResults
                .stream()
                .map(SearchResult::getSorterStatistics)
                .mapToLong(SorterStatistics::failHighCounter )
                .sum();

        this.failHighPercentageAvg = failHighSum > 0 ? Math.toIntExact(100 * failHighFirstSum / failHighSum) : 0;
    }

    private void loadModelDetail(SearchResult searchResult) {
        SorterStatistics sorterStatistics = searchResult.getSorterStatistics();

        BoardModelDetail boardModelModelDetail = new BoardModelDetail();

        Move bestMove = searchResult.getBestMove();
        boardModelModelDetail.id = searchResult.getId();
        boardModelModelDetail.move = bestMove != null ? bestMove.coordinateEncoding() : "";
        boardModelModelDetail.executedMoves = searchResult.getExecutedMoves();
        boardModelModelDetail.exploredDepth = searchResult.getExploredDepth();
        boardModelModelDetail.searchTime = searchResult.getTimeSearching();
        boardModelModelDetail.failHighPercentage = sorterStatistics.failHighCounter() > 0 ? Math.toIntExact(100 * sorterStatistics.failHighFirstCounter() / sorterStatistics.failHighCounter()) : 0;

        this.boardModelModelDetails.add(boardModelModelDetail);
    }
}
