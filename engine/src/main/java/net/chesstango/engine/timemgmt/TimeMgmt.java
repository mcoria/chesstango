package net.chesstango.engine.timemgmt;

import net.chesstango.board.Game;

/**
 * @author Mauricio Coria
 */
public interface TimeMgmt {
    int getSearchTime(Game game, int wTime, int bTime, int wInc, int bInc);
}
