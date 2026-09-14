package net.chesstango.search.smart.statistics.sorter;

import java.io.Serializable;

/**
 * @author Mauricio Coria
 */
public record SorterStatistics(long failHighFirstCounter, long failHighCounter) implements Serializable {
}
