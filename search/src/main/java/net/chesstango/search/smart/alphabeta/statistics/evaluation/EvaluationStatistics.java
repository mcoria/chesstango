package net.chesstango.search.smart.alphabeta.statistics.evaluation;

import java.io.Serializable;
import java.util.Set;

/**
 * @author Mauricio Coria
 */
public record EvaluationStatistics(long evaluationsCounter,
                                   long evaluationsCacheHitsCounter,
                                   long readFromCacheCounter,
                                   long readFromCacheHitsCounter,
                                   Set<EvaluationEntry> evaluations) implements Serializable {
}
