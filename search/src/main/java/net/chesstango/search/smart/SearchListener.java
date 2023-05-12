package net.chesstango.search.smart;

import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;

/**
 * @author Mauricio Coria
 */
public interface SearchListener {
    void init(Game game, final SearchContext context);

    void close(SearchMoveResult result);

}