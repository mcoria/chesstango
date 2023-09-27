package net.chesstango.engine.timemgmt;

import net.chesstango.board.Color;
import net.chesstango.board.Game;

/**
 * @author Mauricio Coria
 */
public class ProportionalMoves implements TimeMgmt {
    @Override
    public int getSearchTime(Game game, int wTime, int bTime, int wInc, int bInc) {
        int numberOfPieces = Long.bitCount(game.getChessPosition().getAllPositions());

        int time = 0;
        if(Color.WHITE.equals(game.getChessPosition().getCurrentTurn())){
            time = calculateTime(wTime, wInc, numberOfPieces);
        } else {
            time = calculateTime(bTime, bInc, numberOfPieces);
        }

        return time;
    }

    protected int calculateTime(int time, int inc, int numberOfPieces) {
        double proportion = (numberOfPieces / 32d) * 40d;
        return (int) (time / proportion) + inc;
    }
}
