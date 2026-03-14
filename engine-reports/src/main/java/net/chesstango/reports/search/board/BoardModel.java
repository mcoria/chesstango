package net.chesstango.reports.search.board;

import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.reports.Model;
import net.chesstango.search.SearchResult;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class BoardModel implements Model<List<SearchResult>> {
    public String searchGroupName;

    public int searches;
    public long executedMovesTotal;
    public long searchTimeTotal;

    public static class BoardModelModelDetail {
        String id;

        String move;
        long executedMoves;
        long searchTime;
    }

    public List<BoardModelModelDetail> boardModelModelDetails;

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

    }

    private void loadModelDetail(SearchResult searchResult) {
        BoardModelModelDetail boardModelModelDetail = new BoardModelModelDetail();

        Move bestMove = searchResult.getBestMove();
        boardModelModelDetail.id = searchResult.getId();
        boardModelModelDetail.move = bestMove != null ? SimpleMoveEncoder.INSTANCE.encode(bestMove) : "";

        boardModelModelDetail.executedMoves = searchResult.getExecutedMoves();
        boardModelModelDetail.searchTime = searchResult.getTimeSearching();

        executedMovesTotal += boardModelModelDetail.executedMoves;
        searchTimeTotal += boardModelModelDetail.searchTime;

        this.boardModelModelDetails.add(boardModelModelDetail);
    }
}
