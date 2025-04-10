package net.chesstango.board.position;

import java.util.Iterator;

/**
 * @author Mauricio Coria
 */
public interface GameHistoryReader {
    Iterator<GameHistoryRecord> iterator();

    Iterator<GameHistoryRecord> iteratorReverse();

    GameHistoryRecord peekLastRecord();

    boolean isEmpty();
}
