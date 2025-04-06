package net.chesstango.board.position;

/**
 * @author Mauricio Coria
 */
public interface CareTakerWriter {
    void push(CareTakerRecord careTakerRecord);

    CareTakerRecord pop();
}
