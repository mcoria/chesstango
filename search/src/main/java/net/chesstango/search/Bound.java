package net.chesstango.search;

/**
 * @author Mauricio Coria
 */
public enum Bound {
    // Dejarlos en este orden ver TranspositionEntry.compareTo()
    UPPER_BOUND, EXACT, LOWER_BOUND;
}
