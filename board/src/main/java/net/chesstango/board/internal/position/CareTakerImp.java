package net.chesstango.board.internal.position;

import net.chesstango.board.position.CareTaker;
import net.chesstango.board.position.CareTakerRecord;

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
    private final Deque<CareTakerRecord> previousStates = new ArrayDeque<>();

    @Override
    public Iterator<CareTakerRecord> iterator() {
        return previousStates.iterator();
    }

    @Override
    public Iterator<CareTakerRecord> iteratorReverse() {
        return previousStates.descendingIterator();
    }

    @Override
    public CareTakerRecord peekLastRecord() {
        return previousStates.peek();
    }

    @Override
    public void push(CareTakerRecord careTakerRecord) {
        previousStates.push(careTakerRecord);
    }

    @Override
    public CareTakerRecord pop() {
        return previousStates.pop();
    }
}
