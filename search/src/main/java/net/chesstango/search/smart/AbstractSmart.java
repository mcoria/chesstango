package net.chesstango.search.smart;

import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;

/**
 * @author Mauricio Coria
 */
public interface AbstractSmart {

    SearchMoveResult searchBestMove(Game game, SearchContext context);

    void stopSearching();
}
