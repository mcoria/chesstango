package net.chesstango.search.smart.statistics;

import java.util.Set;

/**
 * @author Mauricio Coria
 */
public record EvaluationStatistics(long evaluationsCounter, Set<EvaluationEntry> evaluations) {
}
