package net.chesstango.search.alphabeta.statistics.sorter;

import java.io.Serializable;

/**
 * @author Mauricio Coria
 */
public record SorterStatistics(long failHighFirstCounter, long failHighCounter) implements Serializable {
}
