package net.chesstango.search.smart.alphabeta.egtb;

import net.chesstango.board.Game;

/**
 * @author Mauricio Coria
 */
public class EndGameTableBaseNull implements EndGameTableBase {

    @Override
    public boolean isProbeAvailable() {
        return false;
    }

    @Override
    public int evaluate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setGame(Game game) {
    }
}
