package net.chesstango.engine;

import net.chesstango.board.moves.Move;
import net.chesstango.search.SearchResult;

/**
 * @author Mauricio Coria
 */
public record SearchByTreeResult(Move move, SearchResult searchResult) implements SearchResponse {
}
