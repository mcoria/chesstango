package net.chesstango.search;

/**
 * @author Mauricio Coria
 */
public enum Bound  {
    // Ver TranspositionEntry.compareTo() -> TranspositionTailMoveComparator.compare
    // Ver RootMoveEvaluation.compareTo() -> RootMoveSorter.getSortedMovesByLastMoveEvaluations()
    UPPER_BOUND, EXACT, LOWER_BOUND;

}
