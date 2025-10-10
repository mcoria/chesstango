package net.chesstango.search.smart.features.statistics.evaluation;

import java.io.Serializable;
import java.util.Set;

/**
 * @author Mauricio Coria
 */
public record EvaluationStatistics(long evaluationsCounter, long cacheHitsCounter,
                                   Set<EvaluationEntry> evaluations) implements Serializable {
}
