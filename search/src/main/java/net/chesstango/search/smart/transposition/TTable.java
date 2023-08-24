package net.chesstango.search.smart.transposition;

/**
 * @author Mauricio Coria
 */
public interface TTable {
    boolean read(long hash, TranspositionEntry entry);

    void write(long hash, TranspositionEntry entry);

}
