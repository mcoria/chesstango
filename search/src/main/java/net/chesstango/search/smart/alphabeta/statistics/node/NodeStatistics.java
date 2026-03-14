package net.chesstango.search.smart.alphabeta.statistics.node;

import java.io.Serializable;

/**
 * @author Mauricio Coria
 */
public record NodeStatistics(long rootNodeCounter,
                             long interiorNodeCounter,
                             long quiescenceCounter,
                             long terminalNodeCounter,
                             long[] expectedNodesCounters,
                             long[] visitedNodesCounters) implements Serializable {
}
