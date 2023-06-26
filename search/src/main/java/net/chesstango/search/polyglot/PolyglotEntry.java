package net.chesstango.search.polyglot;

import net.chesstango.board.Square;

/**
 * key    uint64
 * <p>
 * move   uint16
 * weight uint16
 * learn  uint32
 */

public record PolyglotEntry(Square from, Square to, int weight) {
    //public Square from;
    //public Square to;
    //public byte promotion;
    //public int weight;

    @Override
    public String toString() {
        return String.format("%s%s %d", from, to, weight);
    }
}
