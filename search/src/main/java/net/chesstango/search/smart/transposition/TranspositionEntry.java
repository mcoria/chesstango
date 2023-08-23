package net.chesstango.search.smart.transposition;

/**
 * @author Mauricio Coria
 */
public interface TranspositionEntry {

    long getHash();

    int getValue();

    TranspositionType getType();

    void loadValues(TranspositionEntry theEntry);
}
