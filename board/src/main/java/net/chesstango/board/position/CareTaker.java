package net.chesstango.board.position;

import java.util.Iterator;

/**
 * @author Mauricio Coria
 */
public interface CareTaker {
    Iterator<GameStateHistory> stateIterator();

    Iterator<GameStateHistory> stateIteratorReverse();

    GameStateHistory peekLastState();

    void storeHistory(GameStateHistory gameStateHistory);

    GameStateHistory popHistory();
}
