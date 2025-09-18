package net.chesstango.search.smart.features.statistics.node;

import java.io.Serializable;

/**
 * @author Mauricio Coria
 */
public record NodeStatistics(int[] expectedNodesCounters, int[] visitedNodesCounters) implements Serializable {
}
