package net.chesstango.engine.polyglot;

/**
 * http://hgm.nubati.net/book_format.html
 * <p>
 * A Polyglot book is a series of "entries" of 16 bytes
 * <p>
 * key    uint64
 * move   uint16
 * weight uint16
 * learn  uint32
 * All integers are stored highest byte first (regardless of size)
 * <p>
 * The entries are ordered according to key. Lowest key first.
 *
 * @author Mauricio Coria
 */

public record PolyglotEntry(long key, int from_file, int from_rank, int to_file, int to_rank, int weight) {

    @Override
    public String toString() {
        char fromFileChar = (char) ('a' + from_file);
        char toFileChar = (char) ('a' + to_file);

        return String.format("%c%d %c%d %d", fromFileChar, from_rank + 1, toFileChar, to_rank + 1, weight);
    }
}
