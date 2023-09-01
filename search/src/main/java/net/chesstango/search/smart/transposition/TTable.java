package net.chesstango.search.smart.transposition;

/**
 * @author Mauricio Coria
 */
public interface TTable {
    TranspositionEntry get(long hash);

    void put(long key, TranspositionEntry tableEntry);

    void clear();
}
