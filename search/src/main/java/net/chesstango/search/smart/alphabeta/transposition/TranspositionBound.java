package net.chesstango.search.smart.alphabeta.transposition;

/**
 * @author Mauricio Coria
 */
public enum TranspositionBound {
    // Dejarlos en este orden ver TranspositionEntry.compareTo()
    UPPER_BOUND, EXACT, LOWER_BOUND;
}
