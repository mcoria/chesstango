package net.chesstango.search.smart.transposition;

/**
 * @author Mauricio Coria
 */
public interface TTable {
    Transposition get(long hash);

    void put(long hash, Transposition entry);

    void clear();
}
