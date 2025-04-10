package net.chesstango.board.internal.position;

import net.chesstango.board.position.GameHistory;
import net.chesstango.board.position.GameHistoryRecord;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

/**
 * @author Mauricio Coria
 */
public class GameHistoryImp implements GameHistory {

    /**
     * Stack to hold previous game states.
     */
    private final Deque<GameHistoryRecord> previousStates = new ArrayDeque<>();

    @Override
    public Iterator<GameHistoryRecord> iterator() {
        return previousStates.iterator();
    }

    @Override
    public Iterator<GameHistoryRecord> iteratorReverse() {
        return previousStates.descendingIterator();
    }

    @Override
    public GameHistoryRecord peekLastRecord() {
        return previousStates.peek();
    }

    @Override
    public boolean isEmpty() {
        return previousStates.isEmpty();
    }

    @Override
    public void push(GameHistoryRecord gameHistoryRecord) {
        previousStates.push(gameHistoryRecord);
    }

    @Override
    public GameHistoryRecord pop() {
        return previousStates.pop();
    }
}
