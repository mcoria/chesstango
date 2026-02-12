package net.chesstango.search.smart.features.transposition;

/**
 * @author Mauricio Coria
 */
public interface TTable {

    TranspositionEntry read(long hash);

    TranspositionEntry write(long hash, TranspositionBound bound, int searchDepth, short move, int value);

    void clear();
}
