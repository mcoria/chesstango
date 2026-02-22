package net.chesstango.search.smart.alphabeta.statistics.node;

import java.io.Serializable;

/**
 * @author Mauricio Coria
 */
public record NodeStatistics(long[] expectedNodesCounters, long[] visitedNodesCounters) implements Serializable {
}
