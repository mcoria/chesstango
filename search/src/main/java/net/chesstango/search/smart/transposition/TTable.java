package net.chesstango.search.smart.transposition;

/**
 * @author Mauricio Coria
 */
public interface TTable {
    TranspositionEntry getForWrite(long hash);

    TranspositionEntry getForRead(long hash);

    void clear();
}
