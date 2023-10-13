package net.chesstango.engine.timemgmt;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;

/**
 * @author Mauricio Coria
 */
public class FivePercentage implements TimeMgmt {
    @Override
    public int getTimeOut(Game game, int wTime, int bTime, int wInc, int bInc) {
        return Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ?
                calculateTime(wTime, wInc) : calculateTime(bTime, bInc);
    }

    @Override
    public boolean timePredicate(SearchMoveResult searchResult) {
        return true;
    }

    protected int calculateTime(int time, int inc) {
        return time / 20 + inc / 2;
    }
}
