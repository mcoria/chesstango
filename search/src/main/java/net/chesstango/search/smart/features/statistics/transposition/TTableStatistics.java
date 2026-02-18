package net.chesstango.search.smart.features.statistics.transposition;

import java.io.Serializable;

/**
 * Statistics for transposition table operations during chess position search.
 * <p>
 * This record captures metrics about transposition table usage, including both read and write operations.
 * Transposition tables are used to cache previously evaluated positions to avoid redundant computation
 * during the search process.
 * </p>
 *
 * @param readHits    The number of successful reads where a position was found in the transposition table
 * @param reads       The total number of read attempts (both hits and misses) from the transposition table
 * @param overWrites  The number of times an existing entry in the transposition table was replaced with a new entry
 * @param writes      The total number of write operations to the transposition table
 * 
 * @author Mauricio Coria
 */
public record TTableStatistics(long readHits,

                               long reads,

                               long overWrites,

                               long writes) implements Serializable {
}
