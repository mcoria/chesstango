package net.chesstango.engine.timemgmt;

import net.chesstango.board.Color;
import net.chesstango.board.Game;

/**
 * @author Mauricio Coria
 */
public class FivePercentage implements TimeMgmt {
    @Override
    public int getSearchTime(Game game, int wTime, int bTime, int wInc, int bInc) {
        return Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ?
                calculateTime(wTime, wInc) : calculateTime(bTime, bInc);
    }

    protected int calculateTime(int time, int inc) {
        return time / 20 + inc / 2;
    }
}
