package net.chesstango.board.internal.position;

import net.chesstango.board.position.CareTaker;
import net.chesstango.board.position.GameStateHistory;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

/**
 * @author Mauricio Coria
 */
public class CareTakerImp implements CareTaker {

    /**
     * Stack to hold previous game states.
     */
    private final Deque<GameStateHistory> previousStates = new ArrayDeque<>();

    @Override
    public Iterator<GameStateHistory> stateIterator() {
        return previousStates.iterator();
    }

    @Override
    public Iterator<GameStateHistory> stateIteratorReverse() {
        return previousStates.descendingIterator();
    }

    @Override
    public GameStateHistory peekLastState() {
        return previousStates.peek();
    }

    @Override
    public void storeHistory(GameStateHistory gameStateHistory) {
        previousStates.push(gameStateHistory);
    }

    @Override
    public GameStateHistory popHistory() {
        return previousStates.pop();
    }
}
