package net.chesstango.search.smart.transposition;

/**
 * @author Mauricio Coria
 */
public interface TTable {

    TranspositionEntry read(long hash);

    TranspositionEntry write(long hash, int searchDepth, long movesAndValue, TranspositionBound bound);

    void clear();
}
