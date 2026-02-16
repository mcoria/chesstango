package net.chesstango.search.smart.features.statistics.transposition;

import java.io.Serializable;

/**
 * @author Mauricio Coria
 */
public record TTableStatistics(long tableHits,
                               long tableCollisions) implements Serializable {
}
