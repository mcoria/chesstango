package net.chesstango.search.smart.statistics.node;

import java.io.Serializable;

/**
 * @author Mauricio Coria
 */
public record NodeStatistics(long rootNodeCounter,
                             long interiorNodeCounter,
                             long quiescenceCounter,
                             long leafCounter,
                             long terminalNodeCounter,
                             long loopNodeCounter,
                             long egtbCounter,

                             long[] expectedNodesCounters,
                             long[] visitedNodesCounters) implements Serializable {

}
