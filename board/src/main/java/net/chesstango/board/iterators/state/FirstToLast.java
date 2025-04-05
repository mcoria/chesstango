package net.chesstango.board.iterators.state;

import net.chesstango.board.position.GameStateReader;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author Mauricio Coria
 */
public class FirstToLast implements StateIterator {

    private final Deque<GameStateReader> states = new ArrayDeque<>();

    public FirstToLast(GameStateReader lastState) {
        states.push(lastState);
        while (lastState.getPreviousState() != null) {
            lastState = lastState.getPreviousState();
            states.push(lastState);
        }
    }

    @Override
    public boolean hasNext() {
        return !states.isEmpty();
    }

    @Override
    public GameStateReader next() {
        return states.pop();
    }
}
