package net.chesstango.board.position;

import java.util.Iterator;

/**
 * @author Mauricio Coria
 */
public interface CareTaker {
    Iterator<CareTakerRecord> iterator();

    Iterator<CareTakerRecord> iteratorReverse();

    CareTakerRecord peekLastState();

    void push(CareTakerRecord careTakerRecord);

    CareTakerRecord pop();
}
