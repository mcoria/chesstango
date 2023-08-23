package net.chesstango.search.smart.transposition;

/**
 * @author Mauricio Coria
 */
public interface TTable<T extends TranspositionEntry> {
    boolean read(long hash, T entry);

    void write(long hash, T entry);



}
