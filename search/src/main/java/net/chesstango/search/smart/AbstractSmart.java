package net.chesstango.search.smart;

import net.chesstango.board.Game;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;

/**
 * @author Mauricio Coria
 */
public abstract class AbstractSmart implements SearchMove {
    protected boolean keepProcessing = true;

    @Override
    public void stopSearching() {
        keepProcessing = false;
    }

    public abstract SearchMoveResult searchBestMove(Game game, SearchContext context);
}
