package net.chesstango.search.smart;

import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;

/**
 * @author Mauricio Coria
 */
public interface SearchSmart {

    SearchMoveResult search(Game game, SearchContext context);

    void stopSearching();
}
