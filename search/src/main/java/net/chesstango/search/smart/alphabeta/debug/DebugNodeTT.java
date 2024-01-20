package net.chesstango.search.smart.alphabeta.debug;

import net.chesstango.search.smart.transposition.TranspositionBound;

/**
 * @author Mauricio Coria
 */
public record DebugNodeTT(long hash_requested,
                          String tableName,
                          long hash,
                          int depth,
                          long movesAndValue,
                          TranspositionBound bound) {
}
