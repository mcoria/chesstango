package net.chesstango.board.representations.polyglot;

import net.chesstango.board.Square;

/**
 * key    uint64
 * <p>
 * move   uint16
 * weight uint16
 * learn  uint32
 */

public class PolyglotEntry {
    Square from;
    Square to;
    byte promotion;
    int weight;

    @Override
    public String toString() {
        return String.format("%s%s %d", from, to, weight);
    }
}
