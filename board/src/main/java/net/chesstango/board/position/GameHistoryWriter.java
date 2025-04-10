package net.chesstango.board.position;

/**
 * @author Mauricio Coria
 */
public interface GameHistoryWriter {
    void push(GameHistoryRecord gameHistoryRecord);

    GameHistoryRecord pop();
}
