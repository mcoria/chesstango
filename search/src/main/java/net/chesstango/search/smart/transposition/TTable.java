package net.chesstango.search.smart.transposition;

/**
 * @author Mauricio Coria
 */
public interface TTable<T extends TranspositionEntry> {
    T read(long hash);

    void write(long hash, T entry);

}
