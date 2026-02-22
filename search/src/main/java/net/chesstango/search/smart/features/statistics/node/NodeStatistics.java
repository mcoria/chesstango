package net.chesstango.search.smart.features.statistics.node;

import java.io.Serializable;

/**
 * @author Mauricio Coria
 */
public record NodeStatistics(long[] expectedNodesCounters, long[] visitedNodesCounters) implements Serializable {
}
