package net.chesstango.search.smart.transposition;

/**
 * @author Mauricio Coria
 */
public interface TTable {
    TranspositionEntry get(long hash);

    TranspositionEntry allocate(long hash);

    void clear();
}
