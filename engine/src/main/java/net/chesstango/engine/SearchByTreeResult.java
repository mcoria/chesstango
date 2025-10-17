package net.chesstango.engine;

import lombok.Getter;
import net.chesstango.board.moves.Move;
import net.chesstango.search.SearchResult;

/**
 * @author Mauricio Coria
 */
public class SearchByTreeResult implements SearchResponse {

    private Move move;

    @Getter
    private SearchResult searchResult;

    public SearchByTreeResult(Move move, SearchResult searchResult) {
        this.move = move;
        this.searchResult = searchResult;
    }

    @Override
    public Move getMove() {
        return move;
    }
}
