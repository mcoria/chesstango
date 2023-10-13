package net.chesstango.engine.timemgmt;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.search.SearchInfo;

/**
 * @author Mauricio Coria
 */
public class FivePercentage implements TimeMgmt {
    @Override
    public int getTimeOut(Game game, int wTime, int bTime, int wInc, int bInc) {
        return Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ?
                calculateTimeOut(wTime, wInc) : calculateTimeOut(bTime, bInc);
    }

    @Override
    public boolean keepSearching(int timeOut, SearchInfo searchInfo) {
        return timeOut - searchInfo.timeSearching() >= searchInfo.timeSearchingLastDepth() * 2;
    }

    protected int calculateTimeOut(int time, int inc) {
        int allocatedTime = time / 20 + inc / 2;
        if (allocatedTime > time) {
            return time / 20;
        }
        return allocatedTime;
    }
}
