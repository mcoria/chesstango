package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchContext;

/**
 * @author Mauricio Coria
 */
public interface FilterActions {
    void init(Game game, final SearchContext context);

    void close(SearchMoveResult result);

    void stopSearching();
}
