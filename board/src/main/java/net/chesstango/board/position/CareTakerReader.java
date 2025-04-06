package net.chesstango.board.position;

import java.util.Iterator;

/**
 * @author Mauricio Coria
 */
public interface CareTakerReader {
    Iterator<CareTakerRecord> iterator();

    Iterator<CareTakerRecord> iteratorReverse();

    CareTakerRecord peekLastState();
}
