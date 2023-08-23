package net.chesstango.search.smart.transposition;

/**
 * @author Mauricio Coria
 */
public interface TTable<T extends TranspositionEntry> {
    T get(long hash);

    void put(long hash, T entry);

}
