package net.chesstango.board.iterators.state;

import net.chesstango.board.position.GameState;

/**
 * @author Mauricio Coria
 */
public class LastToFirst implements StateIterator {



    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public GameState next() {
        return null;
    }
}
