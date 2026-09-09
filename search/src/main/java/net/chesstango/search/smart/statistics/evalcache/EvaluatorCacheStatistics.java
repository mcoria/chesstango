package net.chesstango.search.smart.statistics.evalcache;

import java.io.Serializable;

/**
 * @author Mauricio Coria
 */
public record EvaluatorCacheStatistics(long evaluationsCounter,
                                       long evaluationsCacheHitsCounter,

                                       long readsFromCacheCounter,
                                       long readsFromCacheHitsCounter,

                                       int fillPercentage) implements Serializable {
}
