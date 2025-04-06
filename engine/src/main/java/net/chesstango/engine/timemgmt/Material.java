package net.chesstango.engine.timemgmt;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.search.SearchResultByDepth;

/**
 * @author Mauricio Coria
 */
public class Material implements TimeMgmt {
    @Override
    public int getTimeOut(Game game, int wTime, int bTime, int wInc, int bInc) {
        int numberOfPieces = Long.bitCount(game.getPosition().getAllPositions());

        int time = 0;
        if (Color.WHITE.equals(game.getPosition().getCurrentTurn())) {
            time = calculateTime(wTime, wInc, numberOfPieces);
        } else {
            time = calculateTime(bTime, bInc, numberOfPieces);
        }

        return time;
    }

    @Override
    public boolean keepSearching(int timeOut, SearchResultByDepth searchResultByDepth) {
        return true;
    }

    protected int calculateTime(int time, int inc, int numberOfPieces) {
        double proportion = (numberOfPieces / 32d) * 40d;
        return (int) (time / proportion) + inc;
    }
}
