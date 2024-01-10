package net.chesstango.search.smart.debug;

import net.chesstango.search.smart.transposition.TranspositionBound;

/**
 * @author Mauricio Coria
 */
public record SearchNodeTT(TranspositionOperationType transpositionOperation,
                           long hash_requested,
                           String tableName,
                           long hash,
                           int depth,
                           long movesAndValue,
                           TranspositionBound bound) {
    public enum TranspositionOperationType {READ, WRITE}
}
