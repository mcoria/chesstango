package net.chesstango.engine;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.search.SearchResultByDepth;

/**
 * @author Mauricio Coria
 */
public class FivePercentage implements TimeMgmt {
    @Override
    public int getTimeOut(Game game, int wTime, int bTime, int wInc, int bInc) {
        return Color.WHITE.equals(game.getPosition().getCurrentTurn()) ?
                calculateTimeOut(wTime, wInc) : calculateTimeOut(bTime, bInc);
    }

    @Override
    public boolean keepSearching(int timeOut, SearchResultByDepth searchResultByDepth) {
        return timeOut - searchResultByDepth.getTimeSearching() >= searchResultByDepth.getTimeSearchingLastDepth() * 2;
    }

    protected int calculateTimeOut(int time, int inc) {
        int allocatedTime = time / 20 + inc / 2;
        if (allocatedTime > time) {
            return time / 20;
        }
        return allocatedTime;
    }
}
