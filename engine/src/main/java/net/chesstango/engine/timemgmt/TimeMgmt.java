package net.chesstango.engine.timemgmt;

import net.chesstango.board.Game;
import net.chesstango.search.SearchResultByDepth;

/**
 * @author Mauricio Coria
 */
public interface TimeMgmt {
    int getTimeOut(Game game, int wTime, int bTime, int wInc, int bInc);

    boolean keepSearching(int timeOut, SearchResultByDepth searchResultByDepth);
}
