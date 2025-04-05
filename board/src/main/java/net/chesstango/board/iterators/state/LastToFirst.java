package net.chesstango.board.iterators.state;

import net.chesstango.board.position.GameStateReader;

/**
 * @author Mauricio Coria
 */
public class LastToFirst implements StateIterator {

    private GameStateReader lastState;

    public LastToFirst(GameStateReader lastState) {
        this.lastState = lastState;
    }

    @Override
    public boolean hasNext() {
        return lastState != null;
    }

    @Override
    public GameStateReader next() {
        GameStateReader returnState = lastState;
        lastState = lastState.getPreviousState();
        return returnState;
    }
}
